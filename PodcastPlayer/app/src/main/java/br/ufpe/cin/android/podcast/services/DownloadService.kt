package br.ufpe.cin.android.podcast.services

import android.app.IntentService
import android.content.Intent
import android.os.Environment.DIRECTORY_DOWNLOADS
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class DownloadService : IntentService("DownloadService") {
    override fun onHandleIntent(intent: Intent?) {
        val downloadUri = intent!!.data!!
        val root = getExternalFilesDir(DIRECTORY_DOWNLOADS)
        root?.mkdirs()

        val output = File(root, downloadUri.lastPathSegment!!)
        val url = URL(downloadUri.toString())
        val fos = FileOutputStream(output.path)
        val out = BufferedOutputStream(fos)

        val connection = url.openConnection() as HttpURLConnection

        if (output.exists()) {
            output.delete()
        }

        try {
            tryToDownloadFile(connection, out)
        } finally {
            fos.fd.sync()
            out.close()
            connection.disconnect()
        }

        LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(DOWNLOAD_COMPLETE))
    }

    private fun tryToDownloadFile(connection: HttpURLConnection, outputStream: BufferedOutputStream) {
        val inputStream = connection.inputStream
        val buffer = ByteArray(4096)
        val length = inputStream.read(buffer)

        while (length >= 0) {
            outputStream.write(buffer, 0, length)
        }

        outputStream.flush()
    }


    companion object {
        const val DOWNLOAD_COMPLETE = "br.ufpe.cin.android.podcast.DOWNLOAD_COMPLETE"
    }
}