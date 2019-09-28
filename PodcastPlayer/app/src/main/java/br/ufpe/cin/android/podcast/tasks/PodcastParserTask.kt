package br.ufpe.cin.android.podcast.tasks

import android.os.AsyncTask

import android.util.Log
import br.ufpe.cin.android.podcast.ItemFeed
import br.ufpe.cin.android.podcast.Parser
import br.ufpe.cin.android.podcast.adapters.PodcastAdapter
import br.ufpe.cin.android.podcast.dal.DownloadXml
import br.ufpe.cin.android.podcast.dal.ItemFeedDB
import java.io.IOException

class PodcastParserTask(private val adapter: PodcastAdapter, private val db: ItemFeedDB) :
    AsyncTask<String, Int, List<ItemFeed>>() {
    override fun doInBackground(vararg parameters: String?): List<ItemFeed>? {
        try {
            val xml = DownloadXml().download(parameters[0]!!)
            db.itemFeedDao().insert(Parser.parse(xml))
        } catch (e: IOException) {
            Log.e(CONNECTION_TAG, "Could not get list of episodes from the xml")
        }

        return db.itemFeedDao().selectAll()
    }

    // Update list of episodes after downloading it
    override fun onPostExecute(result: List<ItemFeed>) {
        adapter.updateEpisodeList(result)
    }

    companion object {
        const val CONNECTION_TAG = "connectionError"
    }
}