package com.civil31dot5.fitnessdiary

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

class TimberReleaseTree: Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.VERBOSE) {
            return
        }

        val throwable = t ?: Exception(message)

        if (priority == Log.ERROR){
            FirebaseCrashlytics.getInstance().apply {
                setCustomKey("priority", priority)
                setCustomKey("message", message)
                setCustomKey("tag", tag ?: "unknown")
            }.also {
                it.recordException(throwable)
            }
        }
    }
}