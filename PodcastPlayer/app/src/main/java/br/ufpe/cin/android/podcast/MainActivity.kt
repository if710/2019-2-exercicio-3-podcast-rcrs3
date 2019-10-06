package br.ufpe.cin.android.podcast

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.ufpe.cin.android.podcast.adapters.PodcastAdapter
import br.ufpe.cin.android.podcast.dal.ItemFeedDB
import br.ufpe.cin.android.podcast.services.PodcastPlayerService
import br.ufpe.cin.android.podcast.tasks.PodcastParserTask

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: PodcastAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    internal var podcastPlayerService: PodcastPlayerService? = null
    internal var isBound = false

    private val sConn = object : ServiceConnection {
        override fun onServiceDisconnected(p0: ComponentName?) {
            podcastPlayerService = null
            isBound = false
            viewAdapter.isServiceBound = false
        }

        override fun onServiceConnected(p0: ComponentName?, b: IBinder?) {
            val binder = b as PodcastPlayerService.PodcastPlayerBinder
            podcastPlayerService = binder.service
            viewAdapter.setPodCastService(podcastPlayerService!!)
            isBound = true
            viewAdapter.isServiceBound = true
        }

    }

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

        val intent = Intent(application.applicationContext, PodcastPlayerService::class.java)
        application.startService(intent)
    }

    override fun onStart() {
        super.onStart()
        if (!isBound) {
            val bindIntent = Intent(this, PodcastPlayerService::class.java)
            isBound = bindService(bindIntent,sConn, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        unbindService(sConn)
        super.onStop()
    }

    companion object {
        const val PODCAST_URL = "https://s3-us-west-1.amazonaws.com/podcasts.thepolyglotdeveloper.com/podcast.xml"
    }
}
