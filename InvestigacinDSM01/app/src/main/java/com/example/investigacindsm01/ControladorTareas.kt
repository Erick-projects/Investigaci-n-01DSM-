package com.example.investigacindsm01

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast

data class Task(val description: String, var isCompleted: Boolean)

class ControladorTareas(context: Context, private val tasks: List<Task>) :
    ArrayAdapter<Task>(context, 0, tasks) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val task = getItem(position)
        //Layout de la lista de tareas
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.lista_tareas, parent, false)

        //Checkbox
        val checkBoxTask = view.findViewById<CheckBox>(R.id.checkBoxTask)
        val textViewTask = view.findViewById<TextView>(R.id.textViewTask)

        task?.let {
            textViewTask.text = it.description
            checkBoxTask.isChecked = it.isCompleted

            checkBoxTask.setOnCheckedChangeListener { _, isChecked ->
                it.isCompleted = isChecked
                if (isChecked) {
                    textViewTask.paintFlags = textViewTask.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
                    Toast.makeText(context, "Tarea Completada: ${it.description}", Toast.LENGTH_SHORT).show()
                } else {
                    textViewTask.paintFlags = textViewTask.paintFlags and android.graphics.Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }
            }
        }

        return view
    }
}
