package com.example.lbhacks.models

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lbhacks.data.Problem
import com.example.lbhacks.network.CameraApi
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    val listOfProblems = mutableStateListOf<Problem>(
        Problem("Binary Search", "Given a sorted array, find the index of a specific element. If the element is not in the " +
                "array, return -1.", "5\n 1 2 3 4 5\n4", "3", "Element 4 is present at the 3rd index."),
        Problem("Two Sum", "Given an array of integers and an integer, find two integers whose sum equals the provided integer." +
                "You may assume that that there will always be a two numbers.", "5\n 1 2 0 4 5\n4", "[4,1]", "4 and 1 equal 5, the provided integer"),
        Problem("Palindrome", "Given a string, return true if the string is a palindrome and false if the string is not a palindrome."
            , "car", "false", "\'c\' and \'r\' are not the same so not a palindrome"),
    )

    val cachedProblems = mutableStateListOf<Problem>()

    fun removeProblem(index: Int): Unit {
        cachedProblems.add(listOfProblems.removeAt(index))
    }

    fun sendSolution(): Unit {

    }

    fun getMoreProblems(): Unit {
        viewModelScope.launch {
            try {
                for (i in 0 until cachedProblems.size) {
                    listOfProblems.add(cachedProblems[i])
                }
                cachedProblems.removeRange(0, cachedProblems.size - 1)
                val response: Map<String, String> = CameraApi.retrofitService.getProblems()
                val newProblem = Problem(response["title"], response["description"], response["sampleInput"], response["sampleOutput"], response["explanation"])

                println("getMoreProblems response: $response")

                listOfProblems.add(newProblem)
            } catch (e: Exception) {
                println("HomeViewModel - getMoreProblems had an error: $e")
            }
        }
    }
}