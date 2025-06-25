package com.example.taskmaster

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class AddTaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val etTitle = findViewById<EditText>(R.id.etTaskTitle)
        val etDesc = findViewById<EditText>(R.id.etTaskDesc)
        val cbCompleted = findViewById<CheckBox>(R.id.cbCompleted)
        val rgPriority = findViewById<RadioGroup>(R.id.rgPriority)
        val ivPriority = findViewById<ImageView>(R.id.ivPriority)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnDelete = findViewById<Button>(R.id.btnDelete)

        rgPriority.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbHigh -> ivPriority.setImageResource(R.drawable.ic_action_priority)
                R.id.rbMedium -> ivPriority.setImageResource(R.drawable.ic_action_medium)
                R.id.rbLow -> ivPriority.setImageResource(R.drawable.ic_action_low)
            }
        }

        //Save button
        btnSave.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val desc = etDesc.text.toString().trim()
            val isCompleted = cbCompleted.isChecked

            val selectedPriority = when (rgPriority.checkedRadioButtonId) {
                R.id.rbHigh -> "High"
                R.id.rbMedium -> "Medium"
                R.id.rbLow -> "Low"
                else -> "Low"
            }

            if (title.isEmpty()) {
                Toast.makeText(this, "Task title is required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val resultIntent = Intent().apply {
                putExtra("title", title)
                putExtra("description", desc)
                putExtra("isCompleted", isCompleted)
                putExtra("priority", selectedPriority)
            }

            setResult(Activity.RESULT_OK, resultIntent)
            Toast.makeText(this, "Task added!", Toast.LENGTH_SHORT).show()
            finish()
        }

        //Delete confirmation dialog
        btnDelete.setOnClickListener {
            showDeleteConfirmation()
        }
    }

    private fun showDeleteConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Confirm Delete")
            .setMessage("Are you sure you want to discard this task?")
            .setPositiveButton("Yes") { _, _ -> finish() }
            .setNegativeButton("No", null)
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort -> {
                Toast.makeText(this, "Sort clicked", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_filter -> {
                Toast.makeText(this, "Filter clicked", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_settings -> {
                Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
