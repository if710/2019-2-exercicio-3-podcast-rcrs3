package br.ufpe.cin.android.podcast.dal

import java.io.IOException
import java.net.URL

class DownloadXml {
    @Throws(IOException::class)
    fun download(path: String): String {
        val url = URL(path)

        return url.readText()
    }

}