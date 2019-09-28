package br.ufpe.cin.android.podcast.dal

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.ufpe.cin.android.podcast.ItemFeed
import br.ufpe.cin.android.podcast.dal.dao.ItemFeedDao

@Database(entities = [ItemFeed::class], version = 1)
abstract class ItemFeedDB : RoomDatabase() {
    abstract fun itemFeedDao(): ItemFeedDao

    companion object {
        private var INSTANCE: ItemFeedDB? = null

        fun getDatabase(ctx: Context): ItemFeedDB {
            if (INSTANCE == null) {
                synchronized(ItemFeedDB::class) {
                    INSTANCE = Room.databaseBuilder(
                        ctx.applicationContext,
                        ItemFeedDB::class.java,
                        "episodes")
                        .build()
                }
            }
            return INSTANCE!!
        }
    }
}