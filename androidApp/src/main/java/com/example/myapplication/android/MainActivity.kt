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
fun GreetingView(viewModel: ExampleViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {



    Column(modifier = Modifier.fillMaxSize()) {
        Button(onClick = viewModel::getCity) {
            Text(text = "Single request")
        }

        Text(text = viewModel.firstResultText)

        Button(onClick = viewModel::getWeather
        ) {
            Text(text = "Consistently request")
        }

        Text(text = viewModel.secondResultText)

        Button(onClick = viewModel::getSuspendWeather
        ) {
            Text(text = "combined request")
        }

        Text(text = viewModel.thirdResultText)


    }

}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        GreetingView()
    }
}
