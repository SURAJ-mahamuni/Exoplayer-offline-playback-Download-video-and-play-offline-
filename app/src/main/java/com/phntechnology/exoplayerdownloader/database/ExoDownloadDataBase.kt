package com.phntechnology.exoplayerdownloader.database

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import com.phntechnology.exoplayerdownloader.model.ExoDownloadingHistory
import kotlinx.coroutines.flow.Flow


@Database(
    entities = [ExoDownloadingHistory::class],
    version = 1,
    exportSchema = false
)
abstract class ExoDownloadDataBase : RoomDatabase() {
    abstract fun getExoDownloadDao(): ExoDownloadDao
}

@Dao
interface ExoDownloadDao{

    @Query("select * from ExoDownloadingHistory where videoName=:url")
    fun getCurrentVideoDownloadingStatus(url: String): Flow<List<ExoDownloadingHistory>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addExoDownloadingHistory(exoDownloadingHistory: ExoDownloadingHistory)

    @Query("update ExoDownloadingHistory set notificationFlag=:notificationFlag , downloadingPercent=:downloadingPercent  where contentId=:contentId")
    fun updatePercentExoDownloadingHistory(notificationFlag : Boolean,downloadingPercent : Double,contentId: String)

}