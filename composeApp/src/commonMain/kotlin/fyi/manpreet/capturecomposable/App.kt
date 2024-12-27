package fyi.manpreet.capturecomposable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {

        val coroutineScope = rememberCoroutineScope()
        val graphicsLayer = rememberGraphicsLayer()
        var text by remember { mutableStateOf("") }
        var capturedBitmap: ImageBitmap? by remember { mutableStateOf(null) }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "To Capture Compose",
                modifier = Modifier.padding(bottom = 16.dp),
            )

            Box(
                modifier = Modifier
                    .drawWithContent {
                        // call record to capture the content in the graphics layer
                        graphicsLayer.record {
                            // draw the contents of the composable into the graphics layer
                            this@drawWithContent.drawContent()
                        }
                        // draw the graphics layer on the visible canvas
                        drawLayer(graphicsLayer)
                    }
            ) {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Enter text") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        capturedBitmap = graphicsLayer.toImageBitmap()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }

            // Separator
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                thickness = 5.dp
            )

            Text(
                text = "Captured Compose",
                modifier = Modifier.padding(bottom = 16.dp),
            )

            // Image Display
            val bitmap = capturedBitmap
            if (bitmap != null) {
                Image(
                    bitmap = bitmap,
                    contentDescription = "Captured Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}