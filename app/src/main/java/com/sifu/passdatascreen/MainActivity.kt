package com.sifu.passdatascreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sifu.passdatascreen.ui.theme.PassDataScreenTheme
import io.github.fletchmckee.liquid.LiquidState
import io.github.fletchmckee.liquid.liquefiable
import io.github.fletchmckee.liquid.liquid
import io.github.fletchmckee.liquid.rememberLiquidState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PassDataScreenTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LiquidGlassScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun LiquidGlassScreen(modifier: Modifier = Modifier) {
    val liquidState = rememberLiquidState()

    // ✅ All customizable parameters as state
    var frost by remember { mutableStateOf(10f) }
    var cornerRadius by remember { mutableStateOf(25f) }
    var refraction by remember { mutableStateOf(1f) }
    var curve by remember { mutableStateOf(0.5f) }
    var edge by remember { mutableStateOf(0.1f) }
    var tintAlpha by remember { mutableStateOf(0.2f) }
    var saturation by remember { mutableStateOf(1.5f) }
    var dispersion by remember { mutableStateOf(0.25f) }
    var showPanel by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {

        // 1️⃣ Background
        Image(
            painter = painterResource(id = R.drawable.lady),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .liquefiable(liquidState)
        )

        // 2️⃣ Glass buttons list
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(20) {
                GlassButton(
                    liquidState = liquidState,
                    frost = frost,
                    cornerRadius = cornerRadius,
                    refraction = refraction,
                    curve = curve,
                    edge = edge,
                    tintAlpha = tintAlpha,
                    saturation = saturation,
                    dispersion = dispersion
                )
            }
        }

        // 3️⃣ Customize button (bottom center)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        ) {
            Button(
                onClick = { showPanel = !showPanel },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White.copy(alpha = 0.3f)
                )
            ) {
                Text(
                    text = if (showPanel) "✕ Close" else "⚙ Customize",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // 4️⃣ Settings panel slides in from bottom
        if (showPanel) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = 90.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = Color.Black.copy(alpha = 0.75f),
                    tonalElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            "Liquid Glass Settings",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        SliderRow("Frost", frost, 0f, 30f) { frost = it }
                        SliderRow("Corner Radius", cornerRadius, 0f, 50f) { cornerRadius = it }
                        SliderRow("Refraction", refraction, 0f, 2f) { refraction = it }
                        SliderRow("Curve", curve, 0f, 1f) { curve = it }
                        SliderRow("Edge", edge, 0f, 1f) { edge = it }
                        SliderRow("Tint Alpha", tintAlpha, 0f, 1f) { tintAlpha = it }
                        SliderRow("Saturation", saturation, 0f, 3f) { saturation = it }
                        SliderRow("Dispersion", dispersion, 0f, 1f) { dispersion = it }

                        // Reset button
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = {
                                frost = 10f
                                cornerRadius = 25f
                                refraction = 1f
                                curve = 0.5f
                                edge = 0.1f
                                tintAlpha = 0.2f
                                saturation = 1.5f
                                dispersion = 0.25f
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                        ) {
                            Text("↺ Reset to Defaults")
                        }
                    }
                }
            }
        }
    }
}

// ✅ Reusable slider row with label and live value
@Composable
fun SliderRow(
    label: String,
    value: Float,
    min: Float,
    max: Float,
    onValueChange: (Float) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, color = Color.White, fontSize = 13.sp)
            Text(
                "%.2f".format(value),
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 13.sp
            )
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = min..max,
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = Color.White.copy(alpha = 0.8f),
                inactiveTrackColor = Color.White.copy(alpha = 0.2f)
            )
        )
    }
}

@Composable
fun GlassButton(
    liquidState: LiquidState,
    frost: Float,
    cornerRadius: Float,
    refraction: Float,
    curve: Float,
    edge: Float,
    tintAlpha: Float,
    saturation: Float,
    dispersion: Float,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .size(160.dp, 52.dp)
            .liquid(liquidState) {
                this.frost = frost.dp
                this.shape = RoundedCornerShape(cornerRadius.toInt())
                this.refraction = refraction
                this.curve = curve
                this.edge = edge
                this.tint = Color.White.copy(alpha = tintAlpha)
                this.saturation = saturation
                this.dispersion = dispersion
            }
            .clickable { }
    ) {
        Text("Click Me", color = Color.White, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PassDataScreenTheme {
        LiquidGlassScreen()
    }
}