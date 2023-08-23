package com.example.myapplication.httpbuilder

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

abstract class HttpBuilder {

    val client by lazy {
        HttpClient {
            expectSuccess = false
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.INFO
            }
            install(ContentNegotiation) {
                json(
                    Json {
                        isLenient = true
                        ignoreUnknownKeys = true
                        prettyPrint = true
                    }
                )
            }
        }
    }

    fun withAsync(block: suspend CoroutineScope.() -> Unit): Job =
        CoroutineScope(Dispatchers.Default).launch(block = block)


}

suspend inline fun <reified T> HttpBuilder.networkGet(
    url: String,
    parameters: List<Pair<String, Any?>> = emptyList(),
) = flow {
    emit(client.get {
        this.url(url)
        parameters.forEach {
            parameter(it.first, it.second)
        }
        appendTokenAndCity()
    }.body<T>())
}


fun HttpRequestBuilder.appendTokenAndCity(): HttpRequestBuilder = this.apply {
    parameter("auth_token", "some_token")
    parameter("city", "some_city")
}

