package br.ufpe.cin.android.podcast.dal.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.ufpe.cin.android.podcast.ItemFeed

@Dao
interface ItemFeedDao  {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(episodes: List<ItemFeed>)

    @Query("UPDATE episodes SET episode_path = :episodePath WHERE download_link = :downloadLink")
    fun updateByDownloadLink(downloadLink: String, episodePath: String)

    @Query("SELECT * FROM episodes")
    fun selectAll(): List<ItemFeed>
}