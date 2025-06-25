package com.example.taskmaster

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class TaskBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive (
        context : Context,
        intent: Intent
    ) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED -> {
                Toast.makeText(context, "Device Rebooted", Toast.LENGTH_SHORT).show()
            }

            Intent.ACTION_TIMEZONE_CHANGED -> {
                Toast.makeText(context, "Time zone changed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}