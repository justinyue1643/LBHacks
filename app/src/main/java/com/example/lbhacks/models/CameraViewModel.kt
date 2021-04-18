package com.example.lbhacks.models

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lbhacks.network.CameraApi
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class CameraViewModel: ViewModel() {
    var isSuccess: Boolean by mutableStateOf(false)
    var isUnsuccessful: Boolean by mutableStateOf(false)

    fun getImageSize(f: File) {
        viewModelScope.launch {
            try {
                val imgFile: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), f)
                val imgRequest: MultipartBody.Part = MultipartBody.Part.createFormData("file", f.name, imgFile)

                val response: Map<String, Array<Int>> = CameraApi.retrofitService.sendImageTest(imgRequest)


                for(i in response["size"]!!) {
                    println("yus $i")
                }
                isSuccess = true
            } catch (e: Exception) {
                println("Exception occured in camera model: $e")
                isUnsuccessful = true
            }
        }
    }

    fun addProblem(f: File) {
        viewModelScope.launch {
            try {

            } catch (e: Exception) {

            }
        }
    }

}