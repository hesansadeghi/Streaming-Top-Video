package com.example.streamingtopvideo.service


import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.OptIn
import androidx.core.app.NotificationCompat
import androidx.media3.common.util.NotificationUtil
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.offline.Download
import androidx.media3.exoplayer.offline.DownloadManager
import androidx.media3.exoplayer.offline.DownloadNotificationHelper
import androidx.media3.exoplayer.offline.DownloadService
import androidx.media3.exoplayer.scheduler.Scheduler
import com.example.streamingtopvideo.R
import com.example.streamingtopvideo.presentaion.home.StreamingMovie
import com.example.streamingtopvideo.util.Constants.CANCEL_DOWNLOAD
import com.example.streamingtopvideo.util.Constants.CANCEL_REQUEST_ID
import com.example.streamingtopvideo.util.Constants.CHANNEL_ID
import com.example.streamingtopvideo.util.Constants.DOWNLOAD_NOTIFICATION_CHANNEL_ID
import com.example.streamingtopvideo.util.Constants.FOREGROUND_NOTIFICATION_ID
import com.example.streamingtopvideo.util.Constants.MOVIE_MODEL
import com.example.streamingtopvideo.util.Constants.START_DOWNLOAD
import com.example.streamingtopvideo.util.DownloadManagerUtil
import com.example.streamingtopvideo.util.Utils.formatFileSize
import com.example.streamingtopvideo.util.Utils.sendDownloadBroadcast
import kotlin.collections.get


@OptIn(UnstableApi::class)
class DownloadMovieService : DownloadService(
    FOREGROUND_NOTIFICATION_ID,
    DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL,
    CHANNEL_ID,
    R.string.exo_download_notification_channel_name,
    0
) {

    private val streamingMovieMap: MutableMap<String, StreamingMovie> = mutableMapOf()

    companion object {
        var isRunning: Boolean = false
    }

    override fun onCreate() {
        super.onCreate()
        isRunning = true

        startForeground(
            FOREGROUND_NOTIFICATION_ID,
            createInitialNotification()
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            START_DOWNLOAD -> {

                val movie: StreamingMovie? =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        intent.getSerializableExtra(MOVIE_MODEL, StreamingMovie::class.java)
                    } else {
                        @Suppress("DEPRECATION")
                        intent.getSerializableExtra(MOVIE_MODEL) as? StreamingMovie
                    }


                movie?.let {
                    val request =
                        DownloadManagerUtil.buildDownloadRequest(
                            it.id.toString(),
                            it.streamUrl
                        )
                    sendAddDownload(this, DownloadMovieService::class.java, request, false)
                    streamingMovieMap[it.id.toString()] = it
                }
            }

            CANCEL_DOWNLOAD -> {
                val id = intent.getStringExtra(CANCEL_REQUEST_ID)
                id?.let { id ->
                    val manager = getDownloadManager()
                    manager.removeDownload(id)

                    val hasActive = manager.downloadIndex.getDownloads().use { cursor ->
                        generateSequence { if (cursor.moveToNext()) cursor.download else null }.any {
                            it.state == Download.STATE_DOWNLOADING || it.state == Download.STATE_QUEUED
                        }
                    }

                    if (!hasActive) {
                        stopForeground(STOP_FOREGROUND_REMOVE)
                        stopSelf()

                        return START_NOT_STICKY
                    } else {
                        invalidateForegroundNotification()
                    }
                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }


    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
    }

    override fun getDownloadManager(): DownloadManager {
        val manager = DownloadManagerUtil.getDownloadManager(this)
        val helper = DownloadManagerUtil.getDownloadNotificationHelper(this)

        manager.addListener(
            TerminalStateNotificationHelper(
                this,
                helper,
                FOREGROUND_NOTIFICATION_ID + 1,
                streamingMovieMap,
                {

                    val hasActive = downloadManager.currentDownloads.any {
                        it.state == Download.STATE_DOWNLOADING || it.state == Download.STATE_QUEUED
                    }
                    if (!hasActive) {
                        stopForeground(STOP_FOREGROUND_REMOVE)
                        stopSelf()
                    }
                }
            )
        )
        return manager
    }

    override fun getScheduler(): Scheduler? = null


    override fun getForegroundNotification(
        downloads: List<Download>,
        notMetRequirements: Int
    ): Notification {


        val activeDownloads = downloads.filter {
            it.state == Download.STATE_DOWNLOADING || it.state == Download.STATE_QUEUED
        }
        if (activeDownloads.isEmpty()) {
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()

            return NotificationCompat.Builder(this, DOWNLOAD_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_download_notif)
                .setContentTitle("There are no videos in the download queue.")
                .setContentText("")
                .setOngoing(false)
                .setAutoCancel(true)
                .build()
        }


        downloads.forEach { download ->

            sendDownloadBroadcast(this, download)
        }

        val movieTitle = streamingMovieMap[downloads.firstOrNull()?.request?.id]?.title ?: ""


        val cancelIntent = Intent(this, DownloadMovieService::class.java).apply {
            action = CANCEL_DOWNLOAD
            putExtra(CANCEL_REQUEST_ID, downloads.firstOrNull()?.request?.id)
        }
        val cancelPendingIntent = PendingIntent.getService(
            this,
            0,
            cancelIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val megaByteSize = formatFileSize(downloads.firstOrNull()?.bytesDownloaded ?: 0)


        val builder = NotificationCompat.Builder(this, DOWNLOAD_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_download_notif)
            .setContentTitle("Downloading...")
            .setContentText(movieTitle)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .setProgress(100, (downloads.firstOrNull()?.percentDownloaded ?: 0f).toInt(), false)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Downloading $movieTitle\nDownloaded: $megaByteSize")
            )
            .addAction(R.drawable.outline_close_24, "Cancel", cancelPendingIntent)

        return builder.build()
    }


    private fun createInitialNotification(): Notification {
        return NotificationCompat.Builder(this, DOWNLOAD_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_download_notif)
            .setContentTitle("Preparing downloads...")
            .setContentText("")
            .setOngoing(true)
            .build()
    }

    private class TerminalStateNotificationHelper(
        context: Context,
        private val notificationHelper: DownloadNotificationHelper,
        private var nextNotificationId: Int,
        private val streamingMovieMap: MutableMap<String, StreamingMovie>,
        private val checkDownloadIsActive: () -> Unit
    ) : DownloadManager.Listener {

        private val appContext: Context = context.applicationContext

        override fun onDownloadChanged(
            downloadManager: DownloadManager,
            download: Download,
            finalException: Exception?
        ) {
            val movieTitle = streamingMovieMap[download.request.id]?.title ?: ""

            sendDownloadBroadcast(appContext, download)


            val notification = when (download.state) {
                Download.STATE_COMPLETED -> notificationHelper.buildDownloadCompletedNotification(
                    appContext,
                    R.drawable.outline_check_24,
                    null,
                    "The $movieTitle movie was downloaded successfully."
                )

                Download.STATE_FAILED -> notificationHelper.buildDownloadFailedNotification(
                    appContext,
                    R.drawable.outline_close_24,
                    null,
                    "Download failed: $movieTitle"
                )

                else -> return
            }

            NotificationUtil.setNotification(appContext, nextNotificationId++, notification)
        }

        override fun onDownloadRemoved(downloadManager: DownloadManager, download: Download) {
            sendDownloadBroadcast(appContext, download)
            checkDownloadIsActive()
        }
    }
}