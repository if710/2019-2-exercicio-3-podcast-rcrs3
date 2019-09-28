package br.ufpe.cin.android.podcast

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="episodes")
data class ItemFeed(@PrimaryKey val title: String,
                    @ColumnInfo(name = "link") val link: String,
                    @ColumnInfo(name = "pub_date") val pubDate: String,
                    @ColumnInfo(name = "description") val description: String,
                    @ColumnInfo(name = "download_link") val downloadLink: String) {

    override fun toString(): String {
        return "$title, $link, $pubDate, $description, $downloadLink"
    }
}
