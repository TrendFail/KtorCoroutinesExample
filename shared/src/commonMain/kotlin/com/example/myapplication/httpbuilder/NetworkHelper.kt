package com.example.myapplication.httpbuilder

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class NetworkHelper {
    internal fun withAsync(block: suspend CoroutineScope.() -> Unit): Job =
        CoroutineScope(Dispatchers.Default
                + CoroutineExceptionHandler { coroutineContext, throwable ->
            println("SOME ERROR: ${throwable.message}")
        }
        ).launch(block = block)
}