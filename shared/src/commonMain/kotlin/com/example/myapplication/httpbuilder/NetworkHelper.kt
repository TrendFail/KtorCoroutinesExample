package com.example.myapplication.httpbuilder

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class NetworkHelper {
    internal fun withAsync(block: suspend CoroutineScope.() -> Unit): Job =
        CoroutineScope(Dispatchers.Default).launch(block = block)
}