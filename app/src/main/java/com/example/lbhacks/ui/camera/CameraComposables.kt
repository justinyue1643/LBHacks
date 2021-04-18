package com.example.lbhacks.ui.camera

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.lbhacks.models.CameraViewModel

@Preview
@Composable
fun PreviewSuccessDialog() {
    val dummy = CameraViewModel()
    SuccessDialog(dummy)
}


@Preview
@Composable
fun PreviewFailureDialog() {
    val dummy = CameraViewModel()
    FailureDialog(dummy)
}

@Composable
fun SuccessDialog(viewModel: CameraViewModel) {
    AlertDialog(
        onDismissRequest = {viewModel.isSuccess = false},
        title = {Text("Success")},
        text = {Text("You have passed all test cases!")},
        confirmButton = {
            Button(onClick = {}) {
                Text("Go back")
            }
        }
    )
}

@Composable
fun FailureDialog(viewModel: CameraViewModel) {
    AlertDialog(
        onDismissRequest = {viewModel.isSuccess = false},
        title = {Text("Wrong")},
        text = {Text("You missed some test cases...")},
        confirmButton = {
            Button(onClick = {}) {
                Text("Go back")
            }
        }
    )
}
