package com.rideconnect.data.local.dao

import androidx.room.*
import com.rideconnect.data.local.entity.TripHistoryEntity
import kotlinx.coroutines.flow.Flow
import java.time.ZonedDateTime

@Dao
interface TripHistoryDao {
    @Query("SELECT * FROM trip_history ORDER BY date DESC")
    fun getAllTrips(): Flow<List<TripHistoryEntity>>

    @Query("SELECT * FROM trip_history WHERE id = :tripId")
    suspend fun getTripById(tripId: String): TripHistoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrips(trips: List<TripHistoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: TripHistoryEntity)

    @Query("DELETE FROM trip_history")
    suspend fun clearAllTrips()

    @Query("DELETE FROM trip_history WHERE date < :olderThan")
    suspend fun deleteOldTrips(olderThan: ZonedDateTime)
}
