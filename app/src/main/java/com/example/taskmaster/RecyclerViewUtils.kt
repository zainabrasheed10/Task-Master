package com.example.taskmaster

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun setupRecyclerView(view: View, tasks: MutableList<TaskTodo>) {
    val recycler = view.findViewById<RecyclerView>(R.id.recyclerView)
    recycler.layoutManager = LinearLayoutManager(view.context)
    recycler.adapter = TaskAdapter(view.context, tasks)
}
