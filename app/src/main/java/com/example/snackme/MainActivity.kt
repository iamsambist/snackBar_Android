package com.example.snackme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.tooling.preview.Preview
import com.example.snackme.ui.theme.SnackMeTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SnackView()
        }
    }
}


@Composable
fun SnackView(modifier: Modifier = Modifier) {
    /* SnackbarHostState used directly in Material3
    *  Attach this SnackbarHostState to Scaffold
    *  SnackBarHost has Modifier parameter to implement the gesture detection
    * */
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var isButtonVisible by remember { mutableStateOf(true) }

    Scaffold(
        snackbarHost = { SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.pointerInput (Unit){
                detectHorizontalDragGestures { change, dragAmount ->
                    if(change.positionChange().x > 20f ){
                        scope.launch {
                            snackbarHostState.currentSnackbarData?.dismiss()
                        }
                    }
                }
            }
        ) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    isButtonVisible = false
                    scope.launch {
                      val result =  snackbarHostState.showSnackbar(
                            message = "You Just Snack Me !!",
                            actionLabel = "UnSnack Me",
                            duration = SnackbarDuration.Short
                        )
                        if(result == SnackbarResult.ActionPerformed){
                            isButtonVisible = true
                        }
                    }
                },
                modifier = Modifier.alpha(if (isButtonVisible) 1f else 0f)
            ) {
                Text(text = "Snack Me")
            }

        }

    }

}

@Preview(showBackground = true)
@Composable
fun SnackViewPreview() {
    SnackMeTheme {
        SnackView()
    }
}