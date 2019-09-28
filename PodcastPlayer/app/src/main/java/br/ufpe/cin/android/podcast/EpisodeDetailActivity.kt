package br.ufpe.cin.android.podcast

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.TextView

class EpisodeDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_episode_detail)

        val title = intent.getStringExtra(EPISODE_TITLE)
        val description = intent.getStringExtra(EPISODE_DESCRIPTION)
        val link = intent.getStringExtra(EPISODE_LINK)


        val titleView = findViewById<TextView>(R.id.episode_title)
        val linkView = findViewById<TextView>(R.id.episode_link)
        val descriptionView = findViewById<TextView>(R.id.episode_description)


        titleView.text = title
        linkView.text = link
        descriptionView.text = description

        descriptionView.movementMethod = ScrollingMovementMethod()
    }

    companion object {
        const val EPISODE_TITLE = "title"
        const val EPISODE_DESCRIPTION = "description"
        const val EPISODE_LINK = "link"
    }
}
