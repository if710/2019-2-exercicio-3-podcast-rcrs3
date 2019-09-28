package br.ufpe.cin.android.podcast.dal.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.ufpe.cin.android.podcast.ItemFeed

@Dao
interface ItemFeedDao  {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(episodes: List<ItemFeed>)

    @Query("SELECT * FROM episodes")
    fun selectAll(): List<ItemFeed>
}