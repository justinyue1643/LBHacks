package com.example.lbhacks.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.compose.runtime.getValue;
import androidx.compose.runtime.setValue;
import com.example.lbhacks.data.Problem
import com.example.lbhacks.models.HomeViewModel

@Preview(backgroundColor = 0xffffff, showBackground = true)
@Composable
fun PreviewHome() {
    val context = LocalContext.current
    HomeScreen(navHostController = NavHostController(context))
}

@Composable
fun HomeScreen(navHostController: NavHostController, viewModel: HomeViewModel = viewModel()) {
    var problems = remember { viewModel.listOfProblems }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box() {
            LoadMoreButton()
            for (i in 0..problems.size - 1) {
                ProblemCard(
                    problems[i],
                    onDismissed = {viewModel.removeProblem(i)},
                    onClick = {navigateToCameraScreen(problems[i])})
            }
        }
    }
}

fun navigateToCameraScreen(p: Problem) {

}