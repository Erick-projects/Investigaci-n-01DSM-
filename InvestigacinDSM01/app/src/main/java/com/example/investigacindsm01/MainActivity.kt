package com.example.investigacindsm01

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {

    private lateinit var editTextTask: EditText
    private lateinit var buttonAddTask: Button
    private lateinit var listViewTasks: ListView
    private lateinit var textoBuscar: EditText
    private lateinit var botonBuscar: Button
    private lateinit var botonMostrarTodas: Button
    private val tasks = mutableListOf<Task>()
    private val tareasEncontradas = mutableListOf<Task>()
    private lateinit var controladorTareas: ControladorTareas
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextTask = findViewById(R.id.editTextTask)
        buttonAddTask = findViewById(R.id.buttonAddTask)
        listViewTasks = findViewById(R.id.listViewTasks)
        textoBuscar = findViewById(R.id.editTextSearch)
        botonBuscar = findViewById(R.id.buttonSearchTask)
        botonMostrarTodas = findViewById(R.id.buttonShowAllTasks)
        sharedPreferences = getSharedPreferences("TaskPrefs", Context.MODE_PRIVATE)

        cargarTareas()

        controladorTareas = ControladorTareas(this, tasks)
        listViewTasks.adapter = controladorTareas

        buttonAddTask.setOnClickListener {
            val taskText = editTextTask.text.toString().trim()
            if (taskText.isNotEmpty()) {
                val nuevaTarea = Task(taskText, false)
                tasks.add(nuevaTarea)
                controladorTareas.notifyDataSetChanged()
                editTextTask.text.clear()
                guardarTareas()
                Toast.makeText(this, "Tarea Agregada: $taskText", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Por favor, ingresa una tarea", Toast.LENGTH_SHORT).show()
            }
        }

        botonBuscar.setOnClickListener {
            val tarea = textoBuscar.text.toString().trim()
            if (tarea.isNotEmpty()) {
                buscarTareas(tarea)
            } else {
                Toast.makeText(this, "Por favor, ingresa un texto de búsqueda", Toast.LENGTH_SHORT).show()
            }
        }
        botonMostrarTodas.setOnClickListener {
            mostrarTodasLasTareas()
        }
    }

    // Almacenamiento de Tareas
    // Guardar las tareas
    fun guardarTareas() {
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(tasks)
        editor.putString("lista_tareas", json)
        editor.apply()
    }

    // Cargar las Tareas Almacenadas
    fun cargarTareas() {
        val gson = Gson()
        val json = sharedPreferences.getString("lista_tareas", null)
        val type = object : TypeToken<MutableList<Task>>() {}.type
        if (json != null) {
            tasks.clear()
            tasks.addAll(gson.fromJson(json, type))
        }
    }

    // Buscar tareas
    fun buscarTareas(query: String) {
        tareasEncontradas.clear()
        for (task in tasks) {
            if (task.description.contains(query, ignoreCase = true)) {
                tareasEncontradas.add(task)
            }
        }
        if (tareasEncontradas.isEmpty()) {
            Toast.makeText(this, "No se encontraron tareas", Toast.LENGTH_SHORT).show()
        } else {
            val listaTareasEncontradas = ControladorTareas(this, tareasEncontradas)
            listViewTasks.adapter = listaTareasEncontradas
        }
    }

    // Mostrar todas las tareas
    fun mostrarTodasLasTareas() {
        listViewTasks.adapter = controladorTareas
    }
}
