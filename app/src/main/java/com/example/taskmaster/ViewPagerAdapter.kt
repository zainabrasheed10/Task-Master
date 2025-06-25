package com.example.taskmaster

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ViewPagerAdapter(
    private val context: Context,
    private val allTasks: List<TaskTodo>
) : RecyclerView.Adapter<ViewPagerAdapter.PageViewHolder>() {

    inner class PageViewHolder(val recyclerView: RecyclerView) : RecyclerView.ViewHolder(recyclerView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val recyclerView = RecyclerView(context).apply {
            layoutManager = LinearLayoutManager(context)
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        return PageViewHolder(recyclerView)
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        val tasksToShow = when (position) {
            0 -> allTasks
            1 -> allTasks.filter { !it.isCompleted }
            2 -> allTasks.filter { it.isCompleted }
            else -> emptyList()
        }
        holder.recyclerView.adapter = TaskAdapter(context, tasksToShow.toMutableList())
    }

    override fun getItemCount(): Int = 3
}
