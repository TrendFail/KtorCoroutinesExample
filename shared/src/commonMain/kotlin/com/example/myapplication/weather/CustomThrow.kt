package com.example.myapplication.weather

class CustomThrow(val msg:String, val code:Int):Throwable(message = msg)