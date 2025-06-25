package com.example.taskmaster

import android.content.Context
import com.google.gson.Gson

object SharedPrefUtils {
    fun getTasks(context: Context): MutableList<TaskTodo> {
        val prefs = context.getSharedPreferences("myPref", Context.MODE_PRIVATE)
        val json = prefs.getString("task_list", null) ?: return mutableListOf()
        val type = object : com.google.gson.reflect.TypeToken<MutableList<TaskTodo>>() {}.type
        return Gson().fromJson(json, type)
    }
}
