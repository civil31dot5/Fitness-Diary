package com.civil31dot5.fitnessdiary.ui.utility

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity

fun Context.getActivity(): ComponentActivity? {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is ComponentActivity) {
            return currentContext
        }
        currentContext = currentContext.baseContext
    }
    return null
}