package com.rideconnect.data.local.dao

import androidx.room.*
import com.rideconnect.data.local.entity.SavedAddressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedAddressDao {
    @Query("SELECT * FROM saved_addresses")
    fun getAllSavedAddresses(): Flow<List<SavedAddressEntity>>

    @Query("SELECT * FROM saved_addresses WHERE type = :type")
    fun getSavedAddressesByType(type: String): Flow<List<SavedAddressEntity>>

    @Query("SELECT * FROM saved_addresses WHERE isDefault = 1 AND type = :type LIMIT 1")
    suspend fun getDefaultAddress(type: String): SavedAddressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedAddress(savedAddress: SavedAddressEntity)

    @Update
    suspend fun updateSavedAddress(savedAddress: SavedAddressEntity)

    @Delete
    suspend fun deleteSavedAddress(savedAddress: SavedAddressEntity)

    @Query("DELETE FROM saved_addresses")
    suspend fun clearAllAddresses()
}
