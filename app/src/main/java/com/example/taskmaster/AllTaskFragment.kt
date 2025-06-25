package com.example.taskmaster

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

class AllTaskFragment : Fragment(R.layout.fragment_task_list) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val tasks = SharedPrefUtils.getTasks(requireContext())
        setupRecyclerView(view, tasks)
    }
}
