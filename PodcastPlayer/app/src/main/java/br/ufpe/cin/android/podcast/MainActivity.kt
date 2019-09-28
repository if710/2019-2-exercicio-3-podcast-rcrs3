package br.ufpe.cin.android.podcast

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.ufpe.cin.android.podcast.adapters.PodcastAdapter
import br.ufpe.cin.android.podcast.dal.ItemFeedDB
import br.ufpe.cin.android.podcast.dal.dao.ItemFeedDao
import br.ufpe.cin.android.podcast.tasks.PodcastParserTask

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: PodcastAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewManager = LinearLayoutManager(this)
        viewAdapter = PodcastAdapter(emptyList(), this)

        recyclerView = findViewById<RecyclerView>(R.id.recycler_view).apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
        recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        PodcastParserTask(viewAdapter, ItemFeedDB.getDatabase(this)).execute(PODCAST_URL)
    }

    companion object {
        const val PODCAST_URL = "https://s3-us-west-1.amazonaws.com/podcasts.thepolyglotdeveloper.com/podcast.xml"
    }
}
