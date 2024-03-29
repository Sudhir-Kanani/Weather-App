package com.sk.weatherApp.screens.main.weatherInfo.ui

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sk.weatherApp.screens.main.weatherInfo.ui.currentTab.CurrentWeather
import com.sk.weatherApp.screens.main.weatherInfo.ui.FiveDayTab.FiveDayWeather
import com.sk.weatherApp.screens.main.weatherInfo.viewModel.WeatherViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WeatherInfoScreen(latitude: Double, longitude: Double) {
    val weatherViewModel: WeatherViewModel = hiltViewModel()

    val pagerState = rememberPagerState(pageCount = {
        2
    })
    val coroutineScope = rememberCoroutineScope()

    val tabRowItems = listOf("Current","5 Day forecast")

    Column {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp),
            indicator = { tabPositions ->
                HomeCategoryTabIndicator(
                    modifier = Modifier.tabIndicatorOffset(currentTabPosition = tabPositions[pagerState.currentPage]),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            divider = {
            }
        ) {
            tabRowItems.forEachIndexed { index, item ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                    text = {
                        Text(
                            text = item,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 18.sp
                            )
                        )
                    }
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = true
        ) {
            Log.d("PAGE==>", "$it")
            if (it == 0) {
                CurrentWeather(weatherViewModel,latitude,longitude)
            } else {
                FiveDayWeather(weatherViewModel,latitude,longitude)
            }
        }
    }
}
@Composable
fun HomeCategoryTabIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface
) {
    Spacer(
        modifier
            .padding(horizontal = 50.dp)
            .height(4.dp)
            .background(color, RoundedCornerShape(topStartPercent = 100, topEndPercent = 100))
    )
}