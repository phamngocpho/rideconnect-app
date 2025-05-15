package com.rideconnect.util.constants

object ApiConstants {

    // Endpoints will be retrieved from local.properties
    const val BASE_URL_KEY = "base_url"
    const val GOONG_MAP_API_KEY = "goong_map_api_key"

    // Goong Map API Base URL
    const val GOONG_MAP_BASE_URL = "https://rsapi.goong.io/"

    // Goong Map API Endpoints
    const val GOONG_PLACE_AUTOCOMPLETE_ENDPOINT = "Place/AutoComplete"
    const val GOONG_PLACE_DETAIL_ENDPOINT = "Place/Detail"
    const val GOONG_GEOCODE_ENDPOINT = "Geocode"
    const val GOONG_REVERSE_GEOCODE_ENDPOINT = "Geocode"
    const val GOONG_DIRECTION_ENDPOINT = "Direction"
    const val GOONG_DISTANCE_MATRIX_ENDPOINT = "DistanceMatrix"

    // Auth Endpoints
    const val LOGIN_ENDPOINT = "auth/login"
    const val REGISTER_ENDPOINT = "auth/register"

    // Customer Endpoints
    const val CUSTOMER_DASHBOARD_ENDPOINT = "customer/dashboard"
    const val CUSTOMER_PROFILE_ENDPOINT = "customer/profile"
    const val CUSTOMER_ADDRESS_ENDPOINT = "customer/address"

    // Driver Endpoints
    const val DRIVER_REGISTER_ENDPOINT = "driver/register"
    const val DRIVER_DASHBOARD_ENDPOINT = "driver/dashboard"
    const val DRIVER_PROFILE_ENDPOINT = "drivers/profile"
    const val DRIVER_STATUS_ENDPOINT = "drivers/status"

    // Trip Endpoints
    const val TRIP_CREATE_ENDPOINT = "trips"
    const val TRIP_HISTORY_ENDPOINT = "trips/history"
    const val TRIP_DETAILS_ENDPOINT = "trips/{tripId}"
    const val TRIP_STATUS_ENDPOINT = "trips/{tripId}/status"
    const val TRIP_CANCEL_ENDPOINT = "trip/{tripId}/cancel"

    // Location Endpoints
    const val LOCATION_UPDATE_ENDPOINT = "locations/update"
    const val NEARBY_DRIVERS_ENDPOINT = "locations/nearby-drivers"

    // Payment Endpoints
    const val PAYMENT_METHODS_ENDPOINT = "payment/methods"
    const val PAYMENT_CREATE_ENDPOINT = "payment/create"
    const val PAYMENT_DETAILS_ENDPOINT = "payment/{paymentId}"

    // Rating Endpoints
    const val RATING_CREATE_ENDPOINT = "rating/create"
    const val RATING_USER_ENDPOINT = "ratings/user/{userId}"
    const val RATING_TRIP_ENDPOINT = "ratings/trips/{tripId}"

    // Message Endpoints
    const val CONVERSATIONS_ENDPOINT = "messages/conversations"
    const val MESSAGES_ENDPOINT = "messages"

    // Notification Endpoints
    const val NOTIFICATIONS_ENDPOINT = "notifications"
}
