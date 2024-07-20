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
//Dependencias para guardar las tareas
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {

    private lateinit var editTextTask: EditText
    private lateinit var buttonAddTask: Button
    private lateinit var listViewTasks: ListView
    private val tasks = mutableListOf<Task>()
    private lateinit var taskAdapter: ControladorTareas
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextTask = findViewById(R.id.editTextTask)
        buttonAddTask = findViewById(R.id.buttonAddTask)
        listViewTasks = findViewById(R.id.listViewTasks)
        sharedPreferences = getSharedPreferences("TaskPrefs", Context.MODE_PRIVATE)

        cargarTareas()

        taskAdapter = ControladorTareas(this, tasks)
        listViewTasks.adapter = taskAdapter

        buttonAddTask.setOnClickListener {
            val taskText = editTextTask.text.toString().trim()
            if (taskText.isNotEmpty()) {
                val nuevaTarea = Task(taskText, false)
                tasks.add(nuevaTarea)
                taskAdapter.notifyDataSetChanged()
                editTextTask.text.clear()
                guardarTareas()
                Toast.makeText(this, "Tarea Agregada: $taskText", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Por favor, ingresa una tarea", Toast.LENGTH_SHORT).show()
            }
        }

    }

    //Almacenamiento de Tareas

    //Guardar las tareas
    private fun guardarTareas() {
        val editor = sharedPreferences.edit()
        //Inicialización de la librería GSON para para guardar las tareas
        val gson = Gson()
        //Creación del JSON a partir del objeto que guardar las tareas
        val json = gson.toJson(tasks)
        //Creación de una referencia al JSON con las tareas guardadas
        editor.putString("lista_tareas", json)
        editor.apply()
    }

    //Carga las Tareas Almacenadas
    private fun cargarTareas() {
        //Inicializada el GSON
        val gson = Gson()
        //Crea un objeto JSON para obtener el JSON con las tareas almacenadas
        val json = sharedPreferences.getString("lista_tareas", null)
        //Convierte el JSON al tipo de lista utilizado para mostrarlas en la app
        val type = object : TypeToken<MutableList<Task>>() {}.type
        //Si el JSON es null o vacío limpia las tareas
        if (json != null) {
            tasks.clear()
            tasks.addAll(gson.fromJson(json, type))
        }
    }
}
