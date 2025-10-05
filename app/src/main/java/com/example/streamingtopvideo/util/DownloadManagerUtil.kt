package com.example.streamingtopvideo.util


// imports
import android.content.Context
import androidx.annotation.OptIn
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.NoOpCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.offline.DefaultDownloadIndex
import androidx.media3.exoplayer.offline.DefaultDownloaderFactory
import androidx.media3.exoplayer.offline.Download
import androidx.media3.exoplayer.offline.DownloadManager
import androidx.media3.exoplayer.offline.DownloadNotificationHelper
import androidx.media3.exoplayer.offline.DownloadRequest
import com.example.streamingtopvideo.util.Constants.DOWNLOAD_NOTIFICATION_CHANNEL_ID
import java.io.File
import java.util.concurrent.Executors


@OptIn(UnstableApi::class)
class DownloadManagerUtil {

    companion object {

        private var downloadNotificationHelper: DownloadNotificationHelper? = null

        private var downloadManager: DownloadManager? = null
        private var downloadCache: SimpleCache? = null
        private var databaseProvider: StandaloneDatabaseProvider? = null


        fun getDownloadManager(context: Context): DownloadManager {
            if (downloadManager == null) {
                val appContext = context.applicationContext

                if (databaseProvider == null) {
                    databaseProvider = StandaloneDatabaseProvider(appContext)
                }

                if (downloadCache == null) {
                    val downloadDir =
                        File(appContext.getExternalFilesDir(null), "exo_download_cache")
                    downloadDir.mkdirs()
                    downloadCache = SimpleCache(downloadDir, NoOpCacheEvictor(), databaseProvider!!)
                }

                val dataSourceFactory = DefaultHttpDataSource.Factory()
                val cacheDataSourceFactory = CacheDataSource.Factory()
                    .setCache(downloadCache!!)
                    .setUpstreamDataSourceFactory(dataSourceFactory)

                val executor = Executors.newFixedThreadPool(3)
                val downloaderFactory = DefaultDownloaderFactory(cacheDataSourceFactory, executor)
                val downloadIndex = DefaultDownloadIndex(databaseProvider!!)

                downloadManager = DownloadManager(appContext, downloadIndex, downloaderFactory)
            }
            return downloadManager!!
        }


        @OptIn(markerClass = [UnstableApi::class])
        @Synchronized
        fun getDownloadNotificationHelper(context: Context): DownloadNotificationHelper {
            if (downloadNotificationHelper == null) {
                downloadNotificationHelper =
                    DownloadNotificationHelper(context, DOWNLOAD_NOTIFICATION_CHANNEL_ID)
            }
            return downloadNotificationHelper!!
        }

        fun buildDownloadRequest(id: String, url: String): DownloadRequest {
            return DownloadRequest.Builder(id, url.toUri())
                .setMimeType(MimeTypes.APPLICATION_MPD)
                .build()
        }


//        fun buildDownloadRequest(id: String, url: String, groupIndex: Int,trackIndex: Int): DownloadRequest {
//
//            val streamKey = StreamKey(0, groupIndex, trackIndex)
//            val streamKeys = listOf(
//                streamKey
//            )
//
//            return DownloadRequest.Builder(id, url.toUri())
//                .setMimeType(MimeTypes.APPLICATION_MPD)
//                .setStreamKeys(streamKeys)
//                .build()
//        }


        fun getDownloadCache(context: Context): Cache {
            if (downloadCache == null) {
                val downloadContentDirectory = File(context.filesDir, "downloads")
                databaseProvider = StandaloneDatabaseProvider(context)
                downloadCache =
                    SimpleCache(downloadContentDirectory, NoOpCacheEvictor(), databaseProvider!!)
            }
            return downloadCache!!
        }

        fun buildDataSourceFactory(context: Context): DataSource.Factory {
            val defaultDataSourceFactory = DefaultDataSource.Factory(context)
            return CacheDataSource.Factory()
                .setCache(getDownloadCache(context))
                .setUpstreamDataSourceFactory(defaultDataSourceFactory)
                .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
        }

        fun getOfflineMediaItem(context: Context, downloadId: String): MediaItem? {

            Log.e("downloadId", "getOfflineMediaItem   $downloadId")

            val downloadManager = getDownloadManager(context)
            val cursor = downloadManager.downloadIndex.getDownload(downloadId)

            cursor?.let { download ->
                if (download.state == Download.STATE_COMPLETED) {
                    return MediaItem.Builder()
                        .setMediaId(download.request.id)
                        .setUri(download.request.uri)
                        .build()

                }
            }
            return null
        }

    }

}
