package com.example.lbhacks.models

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.lbhacks.data.Problem

class HomeViewModel: ViewModel() {
    val listOfProblems = mutableStateListOf<Problem>(
        Problem("Binary Search", "Given a sorted array, find the index of a specific element. If the element is not in the " +
                "array, return -1.", "5\n 1 2 3 4 5\n4", "3", "Element 4 is present at the 3rd index."),
        Problem("Binary Search", "Given a sorted array, find the index of a specific element. If the element is not in the " +
                "array, return -1.", "5\n 1 2 3 4 5\n4", "3", "Element 4 is present at the 3rd index."),
        Problem("Binary Search", "Given a sorted array, find the index of a specific element. If the element is not in the " +
                "array, return -1.", "5\n 1 2 3 4 5\n4", "3", "Element 4 is present at the 3rd index."),
    )

    fun removeProblem(index: Int): Unit {
        listOfProblems.removeAt(index)
    }
}