package br.ufpe.cin.android.podcast.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.ufpe.cin.android.podcast.EpisodeDetailActivity
import br.ufpe.cin.android.podcast.ItemFeed
import br.ufpe.cin.android.podcast.MainActivity
import br.ufpe.cin.android.podcast.R
import br.ufpe.cin.android.podcast.services.DownloadService
import br.ufpe.cin.android.podcast.services.PodcastPlayerService
import kotlinx.android.synthetic.main.itemlista.view.*

class PodcastAdapter (private var episodes: List<ItemFeed>,
                      private val application: MainActivity):
    RecyclerView.Adapter<PodcastAdapter.ViewHolder>() {
    var isServiceBound: Boolean = false
    var isPaused: Boolean = false

    private var podcastPlayerService: PodcastPlayerService? = null
    private var currentEpisode: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val episodeView =
            LayoutInflater
                .from(application.applicationContext)
                .inflate(R.layout.itemlista, parent, false)

        return ViewHolder(episodeView)
    }

    override fun getItemCount(): Int = episodes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val episode = episodes[position]

        holder.title.text = episode.title
        holder.pubDate.text = episode.pubDate

        setDownloadButtonVisibility(holder, episode)

        setOnItemViewClickListener(holder, episode)
        setOnDownloadButtonClickListener(holder, episode)
        setOnPlayButtonClickListener(holder, episode)
    }

    // Pass a new list of episodes and notify
    fun updateEpisodeList(episodeList: List<ItemFeed>) {
        this.episodes = episodeList
        notifyItemInserted(episodes.size)
    }

    fun setPodCastService(podcastPlayerService: PodcastPlayerService) {
        this.podcastPlayerService = podcastPlayerService
    }

    private fun setDownloadButtonVisibility(holder: ViewHolder, episode: ItemFeed) {
        if (episode.episodePath != null) {
            holder.downloadButton.visibility = View.GONE
        }
    }

    private fun setOnItemViewClickListener(holder: ViewHolder, episode: ItemFeed) {
        holder.itemView.setOnClickListener {
            val intent = Intent(application.applicationContext, EpisodeDetailActivity::class.java)

            intent.putExtra(EpisodeDetailActivity.EPISODE_TITLE, episode.title)
            intent.putExtra(EpisodeDetailActivity.EPISODE_LINK, episode.link)
            intent.putExtra(EpisodeDetailActivity.EPISODE_DESCRIPTION, episode.description)

            application.startActivity(intent)
        }
    }

    private fun setOnDownloadButtonClickListener(holder: ViewHolder, episode: ItemFeed) {
        holder.downloadButton.setOnClickListener {
            val intent = Intent(application.applicationContext, DownloadService::class.java)

            intent.data = Uri.parse(episode.downloadLink)

            application.startService(intent)
        }
    }

    private fun setOnPlayButtonClickListener(holder: ViewHolder, episode: ItemFeed) {
        holder.playButton.setOnClickListener {
            if (isServiceBound) {
                // If its a different episode, set it's data source
                if (currentEpisode == null || !currentEpisode.equals(episode.episodePath!!)) {
                    podcastPlayerService!!.setPodCastDataSource(episode.episodePath!!)
                    currentEpisode = episode.episodePath
                }

                // If the audio was paused, start from where was paused
                if (!podcastPlayerService!!.isPlaying()) {
                    podcastPlayerService!!.playMusic()
                    holder.itemView.item_play_action.setImageResource(R.drawable.pause_icon)
                } else {
                    podcastPlayerService!!.pauseMusic()
                    holder.itemView.item_play_action.setImageResource(R.drawable.play_icon)
                }
            }
        }
    }

    class ViewHolder (episodeView : View) : RecyclerView.ViewHolder(episodeView) {
        val title: TextView = episodeView.item_title
        val pubDate: TextView = episodeView.item_date
        val downloadButton: Button = episodeView.item_action
        val playButton: ImageButton = episodeView.item_play_action
    }
}