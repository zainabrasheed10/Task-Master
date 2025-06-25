package com.example.taskmaster

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class TaskActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val taskTitle = intent.getStringExtra("task_title")
        when (intent.action) {
            "MARK_COMPLETE" -> {
                Toast.makeText(context, "\"$taskTitle\" marked as complete", Toast.LENGTH_SHORT).show()
            }
            "SNOOZE" -> {
                Toast.makeText(context, "\"$taskTitle\" snoozed for 10 min", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
