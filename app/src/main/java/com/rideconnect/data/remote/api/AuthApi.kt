package com.rideconnect.data.remote.api

import com.rideconnect.data.remote.dto.request.auth.LoginRequest
import com.rideconnect.data.remote.dto.request.auth.RegisterRequest
import com.rideconnect.data.remote.dto.response.auth.LoginResponse
import com.rideconnect.data.remote.dto.response.auth.RegisterResponse
import com.rideconnect.util.constants.ApiConstants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {

    @POST(ApiConstants.LOGIN_ENDPOINT)
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST(ApiConstants.REGISTER_ENDPOINT)
    suspend fun register(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

}
