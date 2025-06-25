package com.example.taskmaster

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.graphics.Color
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.google.gson.Gson
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TaskListActivity : AppCompatActivity() {

    //Saving data to shared pref
    private val sharedPref by lazy { getSharedPreferences("myPref", Context.MODE_PRIVATE) }
    private val editor by lazy { sharedPref.edit() }

    fun saveTasksToPrefs(tasks: List<TaskTodo>) {
        val json = Gson().toJson(tasks)
        editor.putString("task_list", json).apply()
    }

     private fun loadTasksFromPrefs(): MutableList<TaskTodo> {
        val json = sharedPref.getString("task_list", null) ?: return mutableListOf()
        val type = object : com.google.gson.reflect.TypeToken<MutableList<TaskTodo>>() {}.type
        return Gson().fromJson(json, type)
    }

    //Notifications...
    private val CHANNEL_ID = "channelId"
    private val CHANNEL_NAME = "channelName"
    private val NOTIFICATION_ID = 100
    private val REQUEST_CODE_NOTIFICATIONS = 101

    //variables to implement recycler-view
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var taskList: MutableList<TaskTodo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_task_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val title = result.data?.getStringExtra("title") ?: return@registerForActivityResult
                val desc = result.data?.getStringExtra("description") ?: ""
                val isCompleted = result.data?.getBooleanExtra("isCompleted", false) ?: false
                val priority = result.data?.getStringExtra("priority") ?: "Low"

                val newTask = TaskTodo(title, desc, isCompleted, priority)
                taskAdapter.addTodo(newTask)
            }
        }

        taskList = loadTasksFromPrefs()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        //Getting task list from recycler view
        val recyclerView = findViewById<RecyclerView>(R.id.ivRecycle)
        taskAdapter = TaskAdapter(this, taskList)
        recyclerView.adapter = taskAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)


        val move = findViewById<Button>(R.id.btnMove)
        move.setOnClickListener {
            Intent(this, AddTaskActivity::class.java).also {
                launcher.launch(it)
            }
        }

        //Check and Request Permission if needed
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_CODE_NOTIFICATIONS)
            }
        }

        onCreateNotificationChannel()
    }

    //Function to create Notification
    private fun onCreateNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel (
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifies when a task is due"
                enableLights(true)
                lightColor = Color.CYAN
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun showTaskDueNotification(task: TaskTodo) {
        val markIntent = Intent(this, TaskActionReceiver::class.java).apply {
            action = "MARK_COMPLETE"
            putExtra("task_title", task.task)
        }
        val markPendingIntent = PendingIntent.getBroadcast(this, 0, markIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val snoozeIntent = Intent(this, TaskActionReceiver::class.java).apply {
            action = "SNOOZE"
            putExtra("task_title", task.task)
        }
        val snoozePendingIntent = PendingIntent.getBroadcast(this, 1, snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_favorite)
            .setContentTitle("Task Due")
            .setContentText("Your task \"${task.task}\" is due!")
            .addAction(R.drawable.ic_all, "Mark Complete", markPendingIntent)
            .addAction(R.drawable.ic_snooze, "Snooze", snoozePendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, notification)
    }


    //functions to implement toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort -> {
                Toast.makeText (this, "Sort clicked", Toast.LENGTH_SHORT).show()
                true
            }

            R.id.action_filter -> {
                Toast.makeText (this, "Filter clicked", Toast.LENGTH_SHORT).show()
                true
            }

            R.id.action_settings -> {
                Toast.makeText (this, "Settings clicked", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}