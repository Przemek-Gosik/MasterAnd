package com.example.masterand.navigation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.util.Collections

//@Composable
//fun CheckCircularButton(
//    onClick: () -> Unit,
//    color: Color,
//    enabled: Boolean = true,
//    modifier: Modifier = Modifier,
//) {
//    Box(
//        modifier = modifier
//            .clip(CircleShape)
//            .background(color)
//            .size(50.dp)
//            .clickable {
//                if (enabled) {
//                    onClick()
//                }
//            },
//        contentAlignment = Alignment.Center
//    ) {
//        Icon(
//            imageVector = Icons.Default.Check,
//            contentDescription = null,
//            tint = Color.White
//        )
//    }
//}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CheckCircularButton(
    onClick: () -> Unit,
    color: Color,
    isVisible: Boolean = true,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = enabled,
        enter = scaleIn(),
        exit = scaleOut(),
        initiallyVisible = enabled
    ) {
        Box(
            modifier = modifier
                .clip(CircleShape)
                .background(color)
                .size(50.dp)
                .clickable(enabled = enabled, onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Check",
                tint = Color.White
            )
        }
    }
}

@Composable
fun CrossCircularButton(
    onClick: () -> Unit,
    color: Color,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(color)
            .size(50.dp)
            .clickable {
                if (enabled) {
                    onClick()
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Clear,
            contentDescription = null,
            tint = Color.White
        )
    }
}

@Composable
fun CircularButton(
    onClick: () -> Unit,
    initialColor: Color,
    targetColor: Color,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    repeatCount: Int = 5
) {
    var animateTrigger by remember { mutableStateOf(false) }
    val color by animateColorAsState(
        targetValue = if (animateTrigger) targetColor else initialColor,
        animationSpec = repeatable(
            iterations = repeatCount,
            animation = tween(durationMillis = 500),
            repeatMode = RepeatMode.Reverse
        )
    )

    LaunchedEffect(Unit) {
        animateTrigger = true
    }

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(color)
            .size(50.dp)
            .border(2.dp, MaterialTheme.colorScheme.outline, CircleShape)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
    }
}

@Composable
fun SelectableColorsRow(
    colors: List<Color>,
    onClick: (Int) -> Unit,
    enabled: Boolean
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        colors.forEachIndexed { index, color ->
            val initialColor = color
            val targetColor = color.copy(alpha = 0.8f)

            CircularButton(
                onClick = { onClick(index) },
                initialColor = initialColor,
                targetColor = targetColor,
            )
        }
    }
}

@Composable
fun SmallCircle(
    targetColor: Color,
    modifier: Modifier = Modifier
) {
    val animatedColor by animateColorAsState(targetValue = targetColor)

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(animatedColor)
            .border(2.dp, MaterialTheme.colorScheme.outline, CircleShape)
            .size(30.dp)
    )
}

@Composable
fun FeedbackCircles(
    feedbackColors: List<Color>
) {
    val rows = 2
    val columns = 2

    Column(
        modifier = Modifier.width(80.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        repeat(rows) { rowIndex ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.width(80.dp)
            ) {
                repeat(columns) { colIndex ->
                    val index = rowIndex * columns + colIndex
                    val color = feedbackColors.getOrElse(index) { Color.White }
                    SmallCircle(targetColor = color)
                }
            }
        }
    }
}

@Composable
fun GameRow(
    selectedColors: List<Color>,
    feedbackColors: List<Color>,
    onClick: (Int) -> Unit,
    onCheckClick: () -> Unit,
    enabled: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {

        SelectableColorsRow(colors = selectedColors, onClick = onClick, enabled = enabled)
        Spacer(modifier = Modifier.width(16.dp))

        CheckCircularButton(
            onClick = { onCheckClick() },
            color = if (enabled) MaterialTheme.colorScheme.primary else Color.Gray,
            enabled = enabled
        )
        Spacer(modifier = Modifier.width(16.dp))
        FeedbackCircles(feedbackColors = feedbackColors)
    }
}

@Composable
fun GameScreen(navController: NavController, colorCount: String, onLogoutClick: () -> Unit) {
    val lColor = colorCount.toInt()
    var selectedColorsList by remember { mutableStateOf<List<List<Color>>>(listOf()) }
    var feedbackColorsList by remember { mutableStateOf<List<List<Color>>>(listOf()) }
    var attemptCount by remember { mutableStateOf(0) }
    var rows by remember { mutableStateOf(0) }

    val potentialColors =
        listOf(
            Color.Red, Color.Blue, Color.DarkGray, Color.Green, Color.Yellow,
            Color.LightGray, Color.Magenta, Color.Cyan,
            Color.White, Color.Black
        )
    val availableColors = remember { potentialColors.take(lColor) }

    var clickable by remember { mutableStateOf(true) }

    val masterSequence by remember { mutableStateOf(selectRandomColors(availableColors)) }
    var showWinMessage by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = "Your score: $attemptCount",
                    modifier = Modifier.padding(bottom = 16.dp, top = 20.dp),
                    fontSize = 36.sp,
                )
            }

            items((rows + 1).coerceAtLeast(1)) { index ->
                val selectedColors = selectedColorsList.getOrNull(index) ?: List(4) { Color.White }
                val feedbackColors = feedbackColorsList.getOrNull(index) ?: List(4) { Color.White }

                GameRow(
                    selectedColors = selectedColors,
                    feedbackColors = feedbackColors,
                    onClick = { buttonIndex ->
                        if (index == attemptCount && clickable) {
                            selectedColorsList = selectedColorsList.toMutableList().apply {
                                if (size <= index) add(List(4) { Color.White })
                                this[index] = selectNextAvailableColor(
                                    availableColors,
                                    selectedColors,
                                    buttonIndex
                                )
                            }
                        }
                    },
                    onCheckClick = {
                        if (index == attemptCount && clickable) {
                            feedbackColorsList = feedbackColorsList.toMutableList().apply {
                                if (size <= index) add(List(4) { Color.White })
                                this[index] =
                                    checkColors(selectedColors, masterSequence, Color.White)
                            }
                            attemptCount++
                            var isWinner =
                                feedbackColorsList.lastOrNull()?.all { it == Color.Red } ?: false
                            if (isWinner) {
                                showWinMessage = true
                            } else {
                                rows++;
                            }
                        }
                    },
                    enabled = selectedColors.all { color -> color != Color.White } && index == attemptCount
                )
            }

            if (!showWinMessage) {
                item {
                    CrossCircularButton(
                        onClick = {
                            selectedColorsList = emptyList()
                            feedbackColorsList = emptyList()
                            attemptCount = 0
                            clickable = true
                        },
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }

            if (showWinMessage) {
                item {
                    Button(onClick = {
                        navController.navigate("ResultScreen/$attemptCount")
                    }) {
                        Text(text = "High Score table")
                    }
                }
            }
        }

        Button(
            onClick = onLogoutClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Text(text = "Logout")
        }
    }
}

fun selectNextAvailableColor(
    availableColors: List<Color>,
    selectedColors: List<Color>,
    buttonIndex: Int
): List<Color> {
    return if (availableColors.filter { it !in selectedColors }.isNotEmpty()) {
        val nextColor = availableColors.first { it !in selectedColors }
        Collections.rotate(availableColors, -1)
        availableColors.toMutableList()
        selectedColors.toMutableList().also { it[buttonIndex] = nextColor }
    } else
        selectedColors.toMutableList().also { it[buttonIndex] = Color.White }
}

fun selectRandomColors(availableColors: List<Color>): List<Color> {
    //return availableColors.shuffled().take(4)
    return listOf(Color.Red, Color.Blue, Color.DarkGray, Color.Green)
}

fun checkColors(
    selectedColors: List<Color>,
    correctColors: List<Color>,
    notFoundColor: Color
): List<Color> {
    val feedback = mutableListOf<Color>()

    for (i in selectedColors.indices) {
        if (selectedColors[i] == correctColors[i]) {
            feedback.add(Color.Red)
        } else if (correctColors.contains(selectedColors[i])) {
            feedback.add(Color.Yellow)
        } else {
            feedback.add(notFoundColor)
        }
    }

    return feedback
}