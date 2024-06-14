package com.example.masterand.navigation.screens


import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.masterand.R

@Composable
fun ProfileScreen(navController: NavController) {
    var name by remember { mutableStateOf<String?>(null) }
    var email by remember { mutableStateOf<String?>(null) }
    var numberOfColors by remember { mutableStateOf<String?>(null) }

    val infiniteTransition = rememberInfiniteTransition(label = "GameTitleAnimationInf")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "GameTitleAnimation"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "MasterAnd",
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier
                .padding(bottom = 48.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    transformOrigin = TransformOrigin.Center
                }
        )

        ProfileImageWithPicker()
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextFieldWithError(
            label = "Enter name",
            errorLabel = "Name can't be empty",
            keyboardType = KeyboardType.Text,
            validator = { isNameInvalid(it) },
            setValue = { name = it },
            getValue = { name }
        )
        OutlinedTextFieldWithError(
            label = "Enter email",
            errorLabel = "Email needs to be in correct format",
            keyboardType = KeyboardType.Email,
            validator = { isEmailInvalid(it) },
            setValue = { email = it },
            getValue = { email }
        )
        OutlinedTextFieldWithError(
            label = "Enter number or colors",
            errorLabel = "Number of colors must be between 5 and 10",
            keyboardType = KeyboardType.Number,
            validator = { isNumberOfColorsValid(it) },
            setValue = { numberOfColors = it },
            getValue = { numberOfColors }
        )
        Button(
            onClick = {
                navController.navigate("GameScreen/${numberOfColors}")
            },
            modifier = Modifier.fillMaxWidth(),
            //enabled = name.value != null && email.value != null && numberOfColors.value != null
            enabled = true
        ) {
            Text("Next")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedTextFieldWithError(
    label: String,
    errorLabel: String,
    keyboardType: KeyboardType,
    validator: (String?) -> Boolean,
    setValue: (String?) -> Unit,
    getValue: () -> String?
) {
    var isError by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.height(80.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = getValue().orEmpty(),
            onValueChange = {
                setValue(it.ifEmpty { null })
                isError = validator(it)
            },
            label = { Text(label) },
            singleLine = true,
            isError = isError,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            supportingText = {
                if (isError) Text(errorLabel)
            },
            trailingIcon = {
                if (isError)
                    Icon(
                        Icons.Filled.Info,
                        "error",
                        tint = MaterialTheme.colorScheme.error
                    )
            }
        )
    }
}

@Composable
private fun ProfileImageWithPicker() {
    val profileImageUri = rememberSaveable { mutableStateOf<Uri?>(null) }
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { selectedUri ->
            if (selectedUri != null) {
                profileImageUri.value = selectedUri
            }
        }
    )

    IconButton(
        onClick = {
            imagePicker.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        },
        modifier = Modifier.offset(x = 50.dp)
    ) {
        Icon(Icons.Filled.Edit, contentDescription = "Select avatar")
    }

    Box {
        val imageModifier = Modifier
            .size(100.dp)
            .clip(CircleShape)
            .align(Alignment.Center)

        Image(
            painter = painterResource(id = R.drawable.ic_question_mark),
            contentDescription = "Profile photo",
            modifier = imageModifier,
            contentScale = ContentScale.Crop
        )

        AsyncImage(
            model = profileImageUri.value,
            contentDescription = "Profile image",
            modifier = imageModifier,
            contentScale = ContentScale.Crop
        )
    }
}

private fun isNameInvalid(value: String?) = value?.isEmpty() ?: false

private fun isEmailInvalid(value: String?) = value?.let {
    !Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}").matches(it.ifEmpty { "" })
} ?: true

private fun isNumberOfColorsValid(value: String?): Boolean {
    val value = value?.toIntOrNull()
    return value == null || value < 5 || value > 10
}