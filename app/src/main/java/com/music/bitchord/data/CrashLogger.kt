package com.music.bitchord.data

import android.content.Context
import android.os.Build
import com.music.bitchord.BuildConfig
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Persists fatal exceptions before Android terminates the process. */
object CrashLogger {
    private const val DIRECTORY_NAME = "crash-logs"
    private const val TAG = "PivMusicCrashLogger"

    fun install(context: Context) {
        val appContext = context.applicationContext
        val previous = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            runCatching { write(appContext, thread, throwable) }
            previous?.uncaughtException(thread, throwable)
        }
    }

    fun directory(context: Context): File =
        File(context.filesDir, DIRECTORY_NAME).also { it.mkdirs() }

    private fun externalDirectory(context: Context): File? =
        context.getExternalFilesDir(DIRECTORY_NAME)?.also { it.mkdirs() }

    private fun write(context: Context, thread: Thread, throwable: Throwable) {
        val timestamp = SimpleDateFormat("yyyyMMdd-HHmmss-SSS", Locale.US).format(Date())
        val report = buildString {
            appendLine("PivMusic crash report")
            appendLine("timestamp=$timestamp")
            appendLine("package=${context.packageName}")
            appendLine("versionName=${BuildConfig.VERSION_NAME}")
            appendLine("versionCode=${BuildConfig.VERSION_CODE}")
            appendLine("thread=${thread.name} (${thread.id})")
            appendLine("manufacturer=${Build.MANUFACTURER}")
            appendLine("model=${Build.MODEL}")
            appendLine("device=${Build.DEVICE}")
            appendLine("android=${Build.VERSION.RELEASE} (SDK ${Build.VERSION.SDK_INT})")
            appendLine()
            appendLine("exception=")
            append(throwable.stackTraceToString())
            appendLine()
            appendLine()
            appendLine("all_threads=")
            Thread.getAllStackTraces().forEach { (threadInfo, stack) ->
                appendLine("--- ${threadInfo.name} (${threadInfo.id}) ---")
                stack.forEach { frame -> appendLine("    at $frame") }
            }
        }
        val files = listOfNotNull(
            File(directory(context), "crash-$timestamp.txt"),
            externalDirectory(context)?.let { File(it, "crash-$timestamp.txt") },
        )
        files.forEach { it.writeText(report) }
        android.util.Log.e(TAG, "Fatal crash saved to ${files.first().absolutePath}", throwable)
    }
}
