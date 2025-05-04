package com.rideconnect.data.remote.api

import com.rideconnect.data.remote.dto.request.payment.CreatePaymentRequest
import com.rideconnect.data.remote.dto.request.payment.SavePaymentMethodRequest
import com.rideconnect.data.remote.dto.response.payment.PaymentDetailsResponse
import com.rideconnect.data.remote.dto.response.payment.PaymentMethodsResponse
import com.rideconnect.util.constants.ApiConstants
import retrofit2.Response
import retrofit2.http.*

interface PaymentApi {

    @GET(ApiConstants.PAYMENT_METHODS_ENDPOINT)
    suspend fun getPaymentMethods(): Response<PaymentMethodsResponse>

    @POST(ApiConstants.PAYMENT_METHODS_ENDPOINT)
    suspend fun savePaymentMethod(@Body savePaymentMethodRequest: SavePaymentMethodRequest): Response<Unit>

    @DELETE(ApiConstants.PAYMENT_METHODS_ENDPOINT + "/{paymentMethodId}")
    suspend fun deletePaymentMethod(@Path("paymentMethodId") paymentMethodId: String): Response<Unit>

    @POST(ApiConstants.PAYMENT_CREATE_ENDPOINT)
    suspend fun createPayment(@Body createPaymentRequest: CreatePaymentRequest): Response<PaymentDetailsResponse>

    @GET(ApiConstants.PAYMENT_DETAILS_ENDPOINT)
    suspend fun getPaymentDetails(@Path("paymentId") paymentId: String): Response<PaymentDetailsResponse>
}
