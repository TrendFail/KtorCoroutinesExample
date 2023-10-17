package com.example.myapplication.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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
fun GreetingView(
    viewModel: ExampleViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    ) {

    LaunchedEffect(Unit) {
        println("VIEWMODEL: $viewModel")
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Button(onClick = viewModel::stopRequestManually) {
            Text(text = "CancelRequest")
        }
        Text(text = viewModel.requestStatus)

        Button(
            onClick = viewModel::getCountryByCity
        ) {
            Text(text = "Get Country")
        }
        Text(text = viewModel.countyResult)

        Button(
            onClick = viewModel::getWeatherByCity
        ) {
            Text(text = "Get Weather")
        }
        Text(text = viewModel.weatherResult)

        Box(Modifier.size(40.dp).background(Color.Red)) {
            Box(Modifier.size(40.dp).background(Color.Green))
            Box(Modifier.size(20.dp).background(Color.Blue).align(Alignment.BottomEnd))
        }
    }
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        GreetingView()
    }
}
