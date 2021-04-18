package com.example.lbhacks.ui.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.verticalDrag
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lbhacks.data.Problem
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt


private fun Modifier.swipeToDismiss(
    onDismissed: () -> Unit
): Modifier = composed {
    val offsetY = remember { Animatable(0f) }
    pointerInput(Unit) {
        val decay = splineBasedDecay<Float>(this)

        coroutineScope {
            while (true) {
                val pointerId = awaitPointerEventScope { awaitFirstDown().id }
                offsetY.stop()

                val velocityTracker = VelocityTracker()

                awaitPointerEventScope {
                    verticalDrag(pointerId) { change ->
                        launch {
                            offsetY.snapTo(offsetY.value + change.positionChange().y)
                        }

                        velocityTracker.addPosition(change.uptimeMillis, change.position)
                    }
                }

                val velocity = velocityTracker.calculateVelocity().y
                val targetOffsetY = decay.calculateTargetValue(offsetY.value, velocity)

                offsetY.updateBounds(
                    lowerBound = -size.height.toFloat(),
                    upperBound = size.height.toFloat()
                )

                launch {
                    if (targetOffsetY.absoluteValue <= size.height) {
                        offsetY.animateTo(targetValue = 0f, initialVelocity = velocity)
                    } else {
                        offsetY.animateDecay(velocity, decay)
                        onDismissed()
                    }
                }
            }
        }
    }
        .offset {
            IntOffset(0, offsetY.value.roundToInt())
        }
}

@Composable
fun ProblemCard(problem: Problem, onClick: () -> Unit, onDismissed: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.9f)
            .padding(16.dp)
            .swipeToDismiss(onDismissed),
        shape = RoundedCornerShape(10.dp),
        backgroundColor = Color.Yellow,
        elevation = 5.dp,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xffADD8E6))
        ) {
            // Title and Image
            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    problem.title,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp,
                        color = Color.White
                    )
                )

                Spacer(modifier = Modifier.size(10.dp))

                Text(
                    problem.description,
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp
                    )
                )

                Spacer(modifier = Modifier.size(20.dp))

                Text(
                    "Example Input",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                )

                Spacer(modifier = Modifier.size(30.dp))

                Text(
                    problem.sampleInput,
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.White
                    )
                )

                Spacer(modifier = Modifier.size(20.dp))

                Text(
                    "Example Output",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                )

                Spacer(modifier = Modifier.size(30.dp))

                Text(
                    problem.sampleOutput + "\n\n" + problem.explanation,
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.White
                    )
                )
            }

            Button(
                onClick = {onClick()},
                modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)) {
                Text("Solve!")
            }
        }
    }
}

@Composable
fun LoadMoreButton() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {println("clicked")}
        ) {
            Text("Load more problems!")
        }
    }
}