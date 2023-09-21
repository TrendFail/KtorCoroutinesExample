package com.example.myapplication.httpbuilder

sealed class BaseNetworkExceptions(url:String) :Throwable(){
    data class IncorrectJson(val url:String, val errorMessage:String):BaseNetworkExceptions(url)
    data class NeedAuthorization(val url:String):BaseNetworkExceptions(url)

    data class ServerInternalError(val code:Int, val errorMessage:String, val url:String): BaseNetworkExceptions(url)

    data class PageNotFound(val url:String):BaseNetworkExceptions(url)

    data class NetworkError(val url:String, val errorMessage:String):BaseNetworkExceptions(url)

    data class UnknownError(val throwable: Throwable):BaseNetworkExceptions("")
}