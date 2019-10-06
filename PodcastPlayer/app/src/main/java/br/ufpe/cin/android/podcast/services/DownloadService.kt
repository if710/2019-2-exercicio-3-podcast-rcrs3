package br.ufpe.cin.android.podcast.services

import android.app.IntentService
import android.content.Intent
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import br.ufpe.cin.android.podcast.dal.ItemFeedDB
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
        if (output.exists()) {
            output.delete()
        }

        val url = URL(downloadUri.toString())
        val fos = FileOutputStream(output.path)
        val out = BufferedOutputStream(fos)

        val connection = url.openConnection() as HttpURLConnection

        try {
            tryToDownloadFile(connection, out)
        } finally {
            fos.fd.sync()
            out.close()
            connection.disconnect()

            ItemFeedDB.getDatabase(applicationContext)
                .itemFeedDao().updateByDownloadLink(downloadUri.toString(), output.path)
        }

        LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(DOWNLOAD_COMPLETE))
    }

    private fun tryToDownloadFile(connection: HttpURLConnection, outputStream: BufferedOutputStream) {
        val inputStream = connection.inputStream
        val buffer = ByteArray(8192)
        var length = inputStream.read(buffer)

        while (length >= 0) {
            outputStream.write(buffer, 0, length)
            length = inputStream.read(buffer)
        }
        outputStream.flush()
    }


    companion object {
        const val DOWNLOAD_COMPLETE = "br.ufpe.cin.android.podcast.DOWNLOAD_COMPLETE"
    }
}