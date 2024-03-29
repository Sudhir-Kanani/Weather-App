package com.sk.weatherApp.screens.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.sk.weatherApp.R
import com.sk.weatherApp.screens.main.MainActivity
import com.sk.weatherApp.ui.theme.Weather_AppTheme
import kotlinx.coroutines.delay

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Weather_AppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    splashUi()

                    LaunchedEffect(key1 = true) {
                        // Wait for 2 seconds
                        delay(2000)

                        val intent = Intent(this@SplashActivity,MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun splashUi() {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val constraints = ConstraintSet {
            val logo = createRefFor("logo")
            val appName = createRefFor("appName")
            val loader = createRefFor("loader")
            constrain(logo) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }

            constrain(loader) {
                bottom.linkTo(parent.bottom,50.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }

            constrain(appName) {
                top.linkTo(logo.bottom,20.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        }
        ConstraintLayout(
            constraintSet = constraints,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "appLogo",
                modifier = Modifier
                    .size(100.dp)
                    .layoutId("logo"),
                contentScale = ContentScale.Crop
            )


            Text(
                text = stringResource(id = R.string.app_name),
                modifier = Modifier.layoutId("appName"),
                textAlign = TextAlign.Center,
                maxLines = 2,
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f),
                    fontSize = 18.sp,
                    FontWeight.Bold
                )
            )

            CircularProgressIndicator( modifier = Modifier.layoutId("loader"))
        }
    }

}