package com.example.lbhacks.ui.camera

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.camera.core.Preview
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.lbhacks.models.CameraViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService




//The Camera preview stuff has an import that also goes by the name of preview so the compose version has to import itself like that
@androidx.compose.ui.tooling.preview.Preview(showBackground = true, backgroundColor = 0xffffff)
@Composable
fun PreviewCameraScreen() {
    val dummyNav = NavHostController(LocalContext.current)
    //CameraScreen(dummyNav)
}

@Composable
fun CameraScreen(ratio: Float, navHostController: NavHostController, outputDirectory: File, success: Boolean, viewModel: CameraViewModel = CameraViewModel(), addingProblem: Boolean = true) {
    var isSuccess = remember { mutableStateOf(false) }
    var isFailure = remember { mutableStateOf(false) }
    
    Column(modifier = Modifier
        .fillMaxHeight(ratio)
        .fillMaxWidth()) {
        CameraPreview(outputDirectory, viewModel, isSuccess, isFailure, addingProblem)


        if (isSuccess.value && success) {
            AlertDialog(
                onDismissRequest = {isSuccess.value = false},
                title = {Text("Success")},
                text = {Text("You have passed all test cases!")},
                confirmButton = {
                    Button(onClick = {isSuccess.value = false}) {
                        Text("Go back")
                    }
                }
            )
        }
        else if (isFailure.value && !success) {
            AlertDialog(
                onDismissRequest = {isFailure.value = false},
                title = {Text("Wrong")},
                text = {Text("You missed some test cases...")},
                confirmButton = {
                    Button(onClick = {isFailure.value = false}) {
                        Text("Go back")
                    }
                }
            )
        }
    }
}

//Not really sure where to put and call this, still need to test
@Composable
fun CameraPreview(outputDirectory: File, viewModel: CameraViewModel, isSuccess: MutableState<Boolean>, isFailure: MutableState<Boolean>, addingProblem: Boolean) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val imageCapture = ImageCapture.Builder().build()

    Box {
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                val executor = ContextCompat.getMainExecutor(ctx)
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    val cameraSelector = CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build()

                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageCapture
                    )
                }, executor)
                previewView
            },
            modifier = Modifier.fillMaxHeight()
        )
        Button(
            border = BorderStroke(5.dp, Color.White),
            shape = RoundedCornerShape(50),
            onClick = {
                val imageCapture = imageCapture ?: return@Button
                val photoFile = File(
                    outputDirectory,
                    SimpleDateFormat(
                        "yyyy-MM-dd-HH-mm-ss-SSS", Locale.US
                    ).format(System.currentTimeMillis()) + ".jpg")
                val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
                imageCapture.takePicture(
                    outputOptions, ContextCompat.getMainExecutor(context), object : ImageCapture.OnImageSavedCallback {
                        override fun onError(exc: ImageCaptureException) {
                            Log.e("TEST", "Photo capture failed: ${exc.message}", exc)
                        }
                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                            val savedUri = Uri.fromFile(photoFile)
                            val msg = "Photo capture succeeded: $savedUri"
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            Log.d("TEST", msg)

                            println("CAMERA SCREEN: ABOUT TO SEND PICTURE")
                            viewModel.getImageSize(photoFile)

                            if (!addingProblem) {
                                isSuccess.value = true
                                isFailure.value = true
                            }

                        }
                    })
            },
            modifier = Modifier
                .width(70.dp)
                .height(70.dp)
                .align(Alignment.BottomCenter)

        )
        {

        }
    }
}