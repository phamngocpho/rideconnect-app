package com.rideconnect.util.constants

object AppConstants {
    // Shared Preferences
    const val PREF_NAME = "RideConnectPrefs"
    const val KEY_AUTH_TOKEN = "auth_token"
    const val KEY_USER_ID = "user_id"
    const val KEY_USER_TYPE = "user_type"
    const val KEY_IS_LOGGED_IN = "is_logged_in"

    // User Types
    const val USER_TYPE_CUSTOMER = "CUSTOMER"
    const val USER_TYPE_DRIVER = "DRIVER"

    // Trip Status
    const val TRIP_STATUS_REQUESTED = "REQUESTED"
    const val TRIP_STATUS_ACCEPTED = "ACCEPTED"
    const val TRIP_STATUS_ARRIVED = "ARRIVED"
    const val TRIP_STATUS_IN_PROGRESS = "IN_PROGRESS"
    const val TRIP_STATUS_COMPLETED = "COMPLETED"
    const val TRIP_STATUS_CANCELLED = "CANCELLED"

    // Location
    const val LOCATION_UPDATE_INTERVAL = 10000L // 10 seconds
    const val LOCATION_FASTEST_INTERVAL = 5000L // 5 seconds
    const val DEFAULT_ZOOM_LEVEL = 15f

    // Permissions
    const val LOCATION_PERMISSION_REQUEST_CODE = 1001

    // WebSocket
    const val WS_RECONNECT_INTERVAL = 5000L // 5 seconds
}
