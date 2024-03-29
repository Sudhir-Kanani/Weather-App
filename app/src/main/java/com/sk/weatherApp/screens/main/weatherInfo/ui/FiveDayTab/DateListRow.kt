package com.sk.weatherApp.screens.main.weatherInfo.ui.FiveDayTab

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.sk.weatherApp.screens.main.weatherInfo.model.fiveDayWeather.list


@Composable
fun DateListRow(
    index: Int,
    item: list,
    selectedData: Int,
    onSelectDate: (index: Int) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .clickable {
                onSelectDate(index)
            },
        colors = CardDefaults.cardColors(
            containerColor = if (selectedData == index) Color.DarkGray else Color.LightGray,
        )
    ) {
        Text(
            text = item.dt_txt, modifier = Modifier.padding(vertical = 15.dp, horizontal = 15.dp),
            style = TextStyle(color = if (selectedData == index) Color.White else Color.Black)
        )
    }
}