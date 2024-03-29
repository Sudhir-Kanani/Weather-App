package com.sk.weatherApp.screens.main.weatherInfo.ui.currentTab

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.sk.weatherApp.R
import com.sk.weatherApp.Util
import com.sk.weatherApp.screens.main.weatherInfo.model.currentWeather.CurrentWeatherModel


@Composable
fun WeatherData(result: CurrentWeatherModel) {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .dashedBorder(1.dp, Color.LightGray, 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.size(20.dp))

        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://openweathermap.org/img/w/${result.weather.get(0).icon}.png")
                .size(Size.ORIGINAL)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.ic_launcher_background),
            error = painterResource(R.drawable.ic_launcher_background),
        )
        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(80.dp)
                .width(80.dp)
        )

        Spacer(modifier = Modifier.size(10.dp))

        Text(
            text = result.weather.get(0).description, modifier = Modifier,
            style = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.Normal,
                fontSize = 15.sp,
            ),
        )

        Spacer(modifier = Modifier.size(10.dp))

        Text(
            text = "${Util.kelvinToCelsius(result.main.temp)} c", modifier = Modifier,
            style = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
            ),
        )
        Spacer(modifier = Modifier.size(20.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                Modifier.weight(1F),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${result.wind.speed} km/h", modifier = Modifier.fillMaxWidth(),
                    style = TextStyle(
                        color = Color.Black,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                    ),
                )
                Text(
                    text = "Wind speed", modifier = Modifier.fillMaxWidth(),
                    style = TextStyle(
                        color = Color.Black,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                    ),
                )
            }
            Column(
                Modifier.weight(1F),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${result.wind.speed} %", modifier = Modifier.fillMaxWidth(),
                    style = TextStyle(
                        color = Color.Black,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                    ),
                )
                Text(
                    text = "Humidity", modifier = Modifier.fillMaxWidth(),
                    style = TextStyle(
                        color = Color.Black,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                    ),
                )
            }

        }
        Spacer(modifier = Modifier.size(20.dp))

    }
}

fun Modifier.dashedBorder(strokeWidth: Dp, color: Color, cornerRadiusDp: Dp) = composed(
    factory = {
        val density = LocalDensity.current
        val strokeWidthPx = density.run { strokeWidth.toPx() }
        val cornerRadiusPx = density.run { cornerRadiusDp.toPx() }

        this.then(
            Modifier.drawWithCache {
                onDrawBehind {
                    val stroke = Stroke(
                        width = strokeWidthPx,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    )

                    drawRoundRect(
                        color = color,
                        style = stroke,
                        cornerRadius = CornerRadius(cornerRadiusPx)
                    )
                }
            }
        )
    }
)