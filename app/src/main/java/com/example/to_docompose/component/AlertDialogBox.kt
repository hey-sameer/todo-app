package com.example.to_docompose.component

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun AlertDialogBox(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    confirmText: String = "Confirm",
    cancelText: String = "Cancel",
    closeDialog: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { closeDialog() },
        title = { Text(text = title) },
        text = { Text(text = message) },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm()
                    closeDialog()
                },
            ) {
                Text(text = confirmText)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = {
                    onCancel()
                    closeDialog()
                },
            ) {
                Text(text = cancelText)
            }
        }
    )
}