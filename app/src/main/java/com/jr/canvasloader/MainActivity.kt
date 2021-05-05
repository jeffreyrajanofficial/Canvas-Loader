package com.jr.canvasloader

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jr.canvasloader.ui.theme.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import kotlin.math.round

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    CustomLoaders()
                }
            }
        }
    }
}

@Preview
@Composable
fun CustomLoaders() {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var sliderValue by remember { mutableStateOf(0.5f) }
        // Slider value have to be in-between 0f to 1.0f
        // Where 0 is 0% and and 1.0f is 100%
        TriangularLoader(sliderValue)
        FullCircularLoader(sliderValue)
        SemiCircularLoader(sliderValue)
        Slider(
            value = sliderValue,
            onValueChange = { sliderValue = it }
        )
    }
}

@Composable
fun TriangularLoader(sliderValue: Float) {
    Canvas(modifier = Modifier
        .width(300.dp)
        .height(150.dp)
        .padding(16.dp)
    ) {
        val path = Path()
        path.moveTo(size.width, 0f)
        path.lineTo(size.width, size.height)
        path.lineTo(0f, size.height)
        clipPath(
            path = path,
            clipOp = ClipOp.Intersect
        ) {
            drawPath(
                path = path,
                brush = SolidColor(Color.LightGray)
            )

            drawRect(
                SolidColor(Color.Green),
                size = Size(
                    sliderValue * size.width,
                    size.height
                )
            )
        }
    }
}

@Composable
fun FullCircularLoader(sliderValue: Float) {
    Canvas(
        modifier = Modifier
            .size(250.dp)
            .padding(16.dp)
    ) {
        drawCircle(
            SolidColor(Color.LightGray),
            size.width / 2,
            style = Stroke(35f)
        )
        // 80% of 360 = (80/100) * 360
        val convertedValue = sliderValue * 360
        drawArc(
            brush = SolidColor(Color.Black),
            startAngle = -90f,
            sweepAngle = convertedValue,
            useCenter = false,
            style = Stroke(35f)
        )
    }
}

@Composable
fun SemiCircularLoader(sliderValue: Float) {
    Canvas(
        modifier = Modifier
            .width(250.dp)
            .height(250.dp)
            .padding(16.dp)
    ) {
        drawArc(
            brush = SolidColor(Color.LightGray),
            startAngle = 120f,
            sweepAngle = 300f,
            useCenter = false,
            style = Stroke(35f, cap = StrokeCap.Round)
        )

        val convertedValue = sliderValue * 300

        drawArc(
            brush = SolidColor(Color.Cyan),
            startAngle = 120f,
            sweepAngle = convertedValue,
            useCenter = false,
            style = Stroke(35f, cap = StrokeCap.Round)
        )

        drawIntoCanvas {
            val paint = Paint().asFrameworkPaint()
            paint.apply {
                isAntiAlias = true
                textSize = 55f
                textAlign = android.graphics.Paint.Align.CENTER
            }
            it.nativeCanvas.drawText(
                "${round(sliderValue * 100).toInt()}%",
                size.width/2,
                size.height/2,
                paint
            )
        }
    }
}
