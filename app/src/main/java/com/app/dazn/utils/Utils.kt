package com.app.dazn.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.FileNotFoundException
import java.lang.reflect.Type

inline fun <reified T> readJsonFile(context: Context, fileName: String): List<T> {
    if (fileName.isNullOrEmpty()) {
        val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        val type: Type = object : TypeToken<List<T>>() {}.type
        return Gson().fromJson(jsonString, type)
    }else{
        throw NullPointerException()
    }
}


