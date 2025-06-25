package com.example.taskmaster

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private val context: Context,
    private val todos: MutableList<TaskTodo>
) : RecyclerView.Adapter<TaskAdapter.ToDoViewHolder>() {

    inner class ToDoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTaskTitle)
        val tvDesc: TextView = itemView.findViewById(R.id.tvTaskDesc)
        val cbStatus: CheckBox = itemView.findViewById(R.id.cbComplete)
        val ivPriority: ImageView = itemView.findViewById(R.id.ivPriority)
        val ivDelete: ImageView = itemView.findViewById(R.id.ivDelete)
        val ivShare: ImageView = itemView.findViewById(R.id.ivShare)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return ToDoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        val task = todos[position]

        holder.tvTitle.text = task.task
        holder.tvDesc.text = task.description
        holder.cbStatus.setOnCheckedChangeListener(null)
        holder.cbStatus.isChecked = task.isCompleted

        holder.cbStatus.setOnCheckedChangeListener { _, isChecked ->
            task.isCompleted = isChecked

            // Save updated list
            if (context is TaskListActivity) {
                context.saveTasksToPrefs(todos)

                if (!isChecked) {
                    context.showTaskDueNotification(task)
                }
            }
        }

        // Set priority icon based on string value
        val priorityIcon = when (task.priority) {
            "High" -> R.drawable.ic_action_priority
            "Medium" -> R.drawable.ic_action_medium
            "Low" -> R.drawable.ic_action_low
            else -> R.drawable.ic_action_low
        }
        holder.ivPriority.setImageResource(priorityIcon)

        holder.ivShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "Task from TaskMaster")
                putExtra(Intent.EXTRA_TEXT, "Task: ${task.task}\nDescription: ${task.description}")
            }
            // Use context to start the share intent
            context.startActivity(Intent.createChooser(intent, "Share task via"))
        }

        //delete task
        holder.ivDelete.setOnClickListener {
            deleteTask(position)
        }
    }

    override fun getItemCount(): Int {
        return todos.size
    }

    //Function to add task in the list
    fun addTodo(todo: TaskTodo) {
        todos.add(todo)
        notifyItemInserted(todos.size - 1)

        // Save to SharedPreferences
        if (context is TaskListActivity) {
            context.saveTasksToPrefs(todos)
        }
    }

    //Function to delete task from the list
    private fun deleteTask(position: Int) {
        todos.removeAt(position)
        notifyItemRemoved(position)

        // Save updated list
        if (context is TaskListActivity) {
            context.saveTasksToPrefs(todos)
        }
    }
}
