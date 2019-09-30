package br.ufpe.cin.android.podcast.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class PodcastPlayerService : Service() {
    private val podcastPlayerBinder = PodcastPlayerBinder()

    private var podcastPlayer: MediaPlayer? = null


    override fun onCreate() {
        super.onCreate()

        podcastPlayer = MediaPlayer()

        podcastPlayer?.isLooping = false

        createChannel()
        startForegroundService()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()

        podcastPlayer?.release()
    }


    override fun onBind(intent: Intent?): IBinder? {
        return podcastPlayerBinder
    }

    fun playMusic() {
        if (!podcastPlayer!!.isPlaying) {
            podcastPlayer?.start()
        }
    }

    fun pauseMusic() {
        if (podcastPlayer!!.isPlaying) {
            podcastPlayer?.pause()
        }
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val podcastChannel =
                NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)

            podcastChannel.description = "PodCast player channel"

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(podcastChannel)
        }
    }

    private fun startForegroundService() {
        // cria notificacao na area de notificacoes para usuario voltar p/ Activity
        val notificationIntent = Intent(applicationContext, PodcastPlayerService::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        val notification = NotificationCompat.Builder(
            applicationContext, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setOngoing(true).setContentTitle(NOTIFICATION_CONTENT_TITLE)
            .setContentText(NOTIFICATION_CONTENT_TEXT)
            .setContentIntent(pendingIntent).build()

        // inicia em estado foreground, para ter prioridade na memoria
        // evita que seja facilmente eliminado pelo sistema
        startForeground(NOTIFICATION_ID, notification)
    }

    inner class PodcastPlayerBinder : Binder() {
        internal val service: PodcastPlayerService
        get() = this@PodcastPlayerService
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "1"
        const val NOTIFICATION_CHANNEL_NAME = "Notification channel"
        const val NOTIFICATION_CONTENT_TITLE = "PodCast Service playing"
        const val NOTIFICATION_CONTENT_TEXT = "Click to access the player"
        const val NOTIFICATION_ID = 2
    }
}