package com.example.myapplication.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.weather.WeatherApi

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    GreetingView()
                }
            }
        }
    }
}

@Composable
fun GreetingView() {

    var firstResultText by remember { mutableStateOf("empty") }
    var secondResultText by remember { mutableStateOf("empty") }
    var thirdResultText by remember { mutableStateOf("empty") }

    Column(modifier = Modifier.fillMaxSize()) {
        Button(onClick = {
            WeatherApi().searchCity(
                cityName = "Moscow",
                onResult = {
                    firstResultText = it?.cityName.orEmpty()
                })
        }
        ) {
            Text(text = "Single request")
        }

        Text(text = firstResultText)

        Button(onClick = {
            WeatherApi().searchCityAndGetWeather(
                cityName = "Moscow",
                onResult = { temperature, unit ->
                    secondResultText = "$temperature, $unit"
                }
            )
        }
        ) {
            Text(text = "Consistently request")
        }

        Text(text = secondResultText)

        Button(onClick = {
            WeatherApi().combinedApi(onResult = {
                thirdResultText = it
            })
        }
        ) {
            Text(text = "combined request")
        }

        Text(text = thirdResultText)


    }

}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        GreetingView()
    }
}
