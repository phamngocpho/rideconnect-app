package com.rideconnect.presentation.screens.customer.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rideconnect.data.remote.api.GoongMapApi
import com.rideconnect.data.remote.dto.response.location.Prediction
import com.rideconnect.domain.model.location.Location
import com.rideconnect.util.location.LocationUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class SearchLocationViewModel @Inject constructor(
    private val goongMapApi: GoongMapApi,
    private val locationUtils: LocationUtils
) : ViewModel() {

    private val _currentLocation = MutableStateFlow<Location?>(null)
    val currentLocation: StateFlow<Location?> = _currentLocation

    private val _searchState = MutableStateFlow<SearchState>(SearchState.Initial)
    val searchState: StateFlow<SearchState> = _searchState

    // Thêm một trạng thái mới cho tìm kiếm vị trí nguồn
    private val _sourceSearchState = MutableStateFlow<SearchState>(SearchState.Initial)
    val sourceSearchState: StateFlow<SearchState> = _sourceSearchState

    private val _events = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    // Biến để theo dõi job tìm kiếm
    private var searchJob: Job? = null
    private var sourceSearchJob: Job? = null

    init {
        getCurrentLocation()
    }

    private fun getCurrentLocation() {
        viewModelScope.launch {
            try {
                if (locationUtils.hasLocationPermission()) {
                    val location = locationUtils.getCurrentLocation()

                    // Sử dụng Reverse Geocoding để lấy địa chỉ cụ thể
                    val latlng = "${location.latitude},${location.longitude}"
                    try {
                        val response = goongMapApi.reverseGeocode(latlng)
                        if (response.isSuccessful && response.body() != null) {
                            val results = response.body()!!.results
                            if (results.isNotEmpty()) {
                                // Cập nhật địa chỉ từ kết quả Reverse Geocoding
                                val address = results[0].formatted_address
                                _currentLocation.value = location.copy(address = address)
                                return@launch
                            }
                        }
                    } catch (e: Exception) {
                        // Xử lý lỗi khi gọi API
                        _events.emit(UiEvent.ShowToast("Không thể lấy địa chỉ: ${e.message}"))
                    }

                    // Nếu không lấy được địa chỉ, vẫn sử dụng location ban đầu
                    _currentLocation.value = location
                } else {
                    // Sử dụng vị trí mặc định nếu không có quyền
                    _currentLocation.value = locationUtils.getDefaultLocation()
                    _events.emit(UiEvent.RequestLocationPermission)
                }
            } catch (e: SecurityException) {
                _currentLocation.value = locationUtils.getDefaultLocation()
                _events.emit(UiEvent.ShowToast("Cần quyền truy cập vị trí để sử dụng tính năng này"))
            } catch (e: Exception) {
                _currentLocation.value = locationUtils.getDefaultLocation()
                _events.emit(UiEvent.ShowToast("Không thể lấy vị trí hiện tại: ${e.message}"))
            }
        }
    }

    fun refreshCurrentLocation() {
        getCurrentLocation()
    }

    // Hàm kiểm tra xem query có đủ từ để tìm kiếm không
    private fun shouldSearch(query: String): Boolean {
        if (query.isBlank()) return false

        // Đếm số từ trong query
        val wordCount = query.trim().split("\\s+".toRegex()).size

        // Nếu có ít nhất 1 từ hoặc query dài hơn 3 ký tự
        return wordCount > 0 && query.trim().length >= 3
    }

    fun searchPlaces(query: String) {
        if (query.isBlank()) {
            _searchState.value = SearchState.Initial
            return
        }

        // Hủy job tìm kiếm trước đó nếu có
        searchJob?.cancel()

        // Chỉ tìm kiếm khi đủ điều kiện
        if (!shouldSearch(query)) return

        searchJob = viewModelScope.launch {
            delay(500) // Delay 500ms để giảm số lần gọi API
            _searchState.value = SearchState.Loading
            try {
                val locationParam = _currentLocation.value?.let { "${it.latitude},${it.longitude}" }
                val response = goongMapApi.getPlaceAutocomplete(
                    input = query,
                    location = locationParam,
                    radius = 50000 // 50km
                )

                if (response.isSuccessful && response.body() != null) {
                    _searchState.value = SearchState.Success(response.body()!!.predictions)
                } else {
                    _searchState.value = SearchState.Error("Không thể tìm kiếm địa điểm")
                }
            } catch (e: Exception) {
                _searchState.value = SearchState.Error(e.message ?: "Lỗi không xác định")
            }
        }
    }

    // Thêm hàm tìm kiếm vị trí nguồn
    fun searchSourceLocation(query: String) {
        if (query.isBlank()) {
            _sourceSearchState.value = SearchState.Initial
            return
        }

        // Hủy job tìm kiếm trước đó nếu có
        sourceSearchJob?.cancel()

        // Chỉ tìm kiếm khi đủ điều kiện
        if (!shouldSearch(query)) return

        sourceSearchJob = viewModelScope.launch {
            delay(500) // Delay 500ms để giảm số lần gọi API
            _sourceSearchState.value = SearchState.Loading
            try {
                val locationParam = _currentLocation.value?.let { "${it.latitude},${it.longitude}" }
                val response = goongMapApi.getPlaceAutocomplete(
                    input = query,
                    location = locationParam,
                    radius = 50000 // 50km
                )

                if (response.isSuccessful && response.body() != null) {
                    _sourceSearchState.value = SearchState.Success(response.body()!!.predictions)
                } else {
                    _sourceSearchState.value = SearchState.Error("Không thể tìm kiếm địa điểm")
                }
            } catch (e: Exception) {
                _sourceSearchState.value = SearchState.Error(e.message ?: "Lỗi không xác định")
            }
        }
    }

    // Thêm hàm chọn vị trí nguồn
    fun selectSourcePlace(placeId: String) {
        viewModelScope.launch {
            try {
                val response = goongMapApi.getPlaceDetail(
                    placeId = placeId
                )

                if (response.isSuccessful && response.body() != null) {
                    val placeDetail = response.body()!!.result
                    val location = Location(
                        latitude = placeDetail.geometry.location.lat,
                        longitude = placeDetail.geometry.location.lng,
                        address = placeDetail.formatted_address,
                        name = placeDetail.name
                    )
                    _currentLocation.value = location
                    _sourceSearchState.value = SearchState.Initial
                }
            } catch (e: Exception) {
                _sourceSearchState.value = SearchState.Error(e.message ?: "Không thể lấy chi tiết địa điểm")
            }
        }
    }

    fun selectPlace(placeId: String, onLocationSelected: (Location) -> Unit) {
        viewModelScope.launch {
            try {
                val response = goongMapApi.getPlaceDetail(
                    placeId = placeId
                )

                if (response.isSuccessful && response.body() != null) {
                    val placeDetail = response.body()!!.result
                    val location = Location(
                        latitude = placeDetail.geometry.location.lat,
                        longitude = placeDetail.geometry.location.lng,
                        address = placeDetail.formatted_address,
                        name = placeDetail.name
                    )
                    onLocationSelected(location)
                } else {
                    _events.emit(UiEvent.ShowToast("Không thể lấy chi tiết địa điểm"))
                }
            } catch (e: Exception) {
                _events.emit(UiEvent.ShowToast("Lỗi: ${e.message}"))
            }
        }
    }

    sealed class SearchState {
        data object Initial : SearchState()
        data object Loading : SearchState()
        data class Success(val predictions: List<Prediction>) : SearchState()
        data class Error(val message: String) : SearchState()
    }

    sealed class UiEvent {
        data class ShowToast(val message: String) : UiEvent()
        data object RequestLocationPermission : UiEvent()
    }
}
