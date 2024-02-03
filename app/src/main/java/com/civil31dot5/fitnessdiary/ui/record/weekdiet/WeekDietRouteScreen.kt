package com.civil31dot5.fitnessdiary.ui.record.weekdiet

import android.graphics.BitmapFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import com.origeek.imageViewer.viewer.ImageViewer
import com.origeek.imageViewer.viewer.ImageViewerState
import com.origeek.imageViewer.viewer.rememberViewerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File


@Composable
fun WeekDietRoute(
    viewModel: WeekDietViewModel = hiltViewModel()
){
    WeekDietContent(
        weekDietImageFile = viewModel.weekDietImageFile.collectAsState()
    )
}

@Composable
fun WeekDietContent(
    scope: CoroutineScope = rememberCoroutineScope(),
    imageViewerState: ImageViewerState = rememberViewerState(),
    weekDietImageFile: State<File?>
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        if (weekDietImageFile.value == null){
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "loading..."
            )
        }else{
            ImageViewer(
                state = imageViewerState,
                model = BitmapFactory.decodeStream(weekDietImageFile.value!!.inputStream()).asImageBitmap(),
                modifier = Modifier.fillMaxSize(),
                detectGesture = {
                    onTap = { /* 点击事件 */ }
                    onDoubleTap = {
                        scope.launch {
                            imageViewerState.toggleScale(it)
                        }
                    }
                    onLongPress = { /* 长按事件 */ }
                }
            )
        }
    }
}