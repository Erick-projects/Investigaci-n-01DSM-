package com.example.investigacindsm01

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var editTextTask: EditText
    private lateinit var buttonAddTask: Button
    private lateinit var listViewTasks: ListView
    private val tasks = mutableListOf<Task>()
    private lateinit var taskAdapter: ControladorTareas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextTask = findViewById(R.id.editTextTask)
        buttonAddTask = findViewById(R.id.buttonAddTask)
        listViewTasks = findViewById(R.id.listViewTasks)

        taskAdapter = ControladorTareas(this, tasks)
        listViewTasks.adapter = taskAdapter

        buttonAddTask.setOnClickListener {
            val taskText = editTextTask.text.toString().trim()
            if (taskText.isNotEmpty()) {
                val nuevaTarea = Task(taskText, false)
                tasks.add(nuevaTarea)
                taskAdapter.notifyDataSetChanged()
                editTextTask.text.clear()
                Toast.makeText(this, "Tarea Agregada: $taskText", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Por favor, ingresa una tarea", Toast.LENGTH_SHORT).show()
            }
        }


    }
}
