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
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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
                        coerceInputValues = true
                        encodeDefaults = true
                    }
                )
            }
        }
    }

}

inline fun <reified T> HttpBuilder.networkGet(
    url: String,
    parameters: List<Pair<String, Any?>> = emptyList(),
) = flow {
    val response = try {
        client.get {
            this.url(url)
            parameters.forEach {
                parameter(it.first, it.second)
            }
            appendTokenAndCity()
        }
    } catch (e: Throwable) {
        throw BaseNetworkExceptions.NetworkError(url = url, errorMessage = e.message.orEmpty())
    }
    emit(response.checkResponse<T>())
}.flowOn(Dispatchers.Default)

suspend inline fun <reified T> HttpBuilder.networkGetNotFlow(
    url: String,
    parameters: List<Pair<String, Any?>> = emptyList(),
) =
    client.get {
        this.url(url)
        parameters.forEach {
            parameter(it.first, it.second)
        }
        appendTokenAndCity()
    }.body<T>()


fun HttpRequestBuilder.appendTokenAndCity(): HttpRequestBuilder = this.apply {
    parameter("auth_token", "some_token")
    parameter("city", "some_city")
}

fun <T> Flow<T>.handleErrors(error: (BaseNetworkExceptions) -> Unit): Flow<T> = catch {
    if (it is BaseNetworkExceptions) {
        error(it)
    } else {
        error(BaseNetworkExceptions.UnknownError(it))
    }
}

suspend inline fun <reified T> HttpResponse.checkResponse(): T {
    when (this.status.value) {
        in 500 until 1000 -> throw BaseNetworkExceptions.ServerInternalError(
            code = this.status.value,
            errorMessage = this.bodyAsText(),
            url = this.request.url.toString()
        )

        403 -> throw BaseNetworkExceptions.NeedAuthorization(url = this.request.url.toString())
        404 -> throw BaseNetworkExceptions.PageNotFound(url = this.request.url.toString())
        in 200 until 300 -> {
            try {
                return body()
            } catch (e: Throwable) {
                throw BaseNetworkExceptions.IncorrectJson(
                    errorMessage = e.message.orEmpty(),
                    url = this.request.url.toString()
                )
            }
        }
    }
    throw BaseNetworkExceptions.UnknownError(Throwable(message = "not processed error"))
}
