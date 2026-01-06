package com.wannacry.stockup.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun DialogBox(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    title: String?,
    description: String?,
    positiveButtonText: String?,
    positiveButtonColor: Color,
    positiveButtonAction: () -> Unit,
    negativeButtonText: String?,
    negativeButtonAction: () -> Unit,
) {

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            if (title?.isNotEmpty() == true) {
                Text(title, fontWeight = FontWeight.Bold)
            }
        },
        text = {
                Text(description.orEmpty())
        },
        confirmButton = {
            TextButton(
                onClick = positiveButtonAction,
                colors = ButtonDefaults.textButtonColors(contentColor = positiveButtonColor)
            ) {
                Text(positiveButtonText.orEmpty(), fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = negativeButtonAction) {
                Text(negativeButtonText.orEmpty())
            }
        }
    )

}