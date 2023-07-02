package com.civil31dot5.fitnessdiary.ui.utility

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.civil31dot5.fitnessdiary.ui.theme.FitnessDiaryTheme

@Composable
fun ConfirmDeleteDialog(
    onDeleteClick: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "確認刪除?") },
        confirmButton = {
            TextButton(
                onClick = onDeleteClick
            ) {
                Text("確認")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("取消")
            }
        }
    )
}

@Preview
@Composable
fun PreviewConfirmDeleteDialog() {
    FitnessDiaryTheme {
        ConfirmDeleteDialog()
    }
}