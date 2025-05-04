package com.rideconnect.data.local.dao

import androidx.room.*
import com.rideconnect.data.local.entity.RecentSearchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentSearchDao {
    @Query("SELECT * FROM recent_searches ORDER BY searchTime DESC LIMIT 10")
    fun getRecentSearches(): Flow<List<RecentSearchEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentSearch(search: RecentSearchEntity)

    @Query("DELETE FROM recent_searches")
    suspend fun clearSearchHistory()

    @Query("DELETE FROM recent_searches WHERE id = :searchId")
    suspend fun deleteSearch(searchId: Int)
}
