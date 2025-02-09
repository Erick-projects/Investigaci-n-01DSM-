package com.example.investigacindsm01

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

// Clase para las tareas y su estado
data class Task(var description: String, var isCompleted: Boolean)

class ControladorTareas(context: Context, private val tasks: MutableList<Task>) :
    ArrayAdapter<Task>(context, 0, tasks) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val task = getItem(position)
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.lista_tareas, parent, false)

        val checkBoxTask = view.findViewById<CheckBox>(R.id.checkBoxTask)
        val textViewTask = view.findViewById<TextView>(R.id.textViewTask)
        val botonEliminarTareas = view.findViewById<Button>(R.id.buttonDeleteTask)
        val botonEditarTareas = view.findViewById<Button>(R.id.buttonEditTask)

        task?.let {
            textViewTask.text = it.description
            checkBoxTask.isChecked = it.isCompleted

            // Actualiza el texto con o sin línea de tachado
            textViewTask.paintFlags = if (it.isCompleted) {
                textViewTask.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                textViewTask.paintFlags and android.graphics.Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }

            checkBoxTask.setOnCheckedChangeListener { _, isChecked ->
                it.isCompleted = isChecked
                // Actualiza el texto con o sin línea de tachado
                textViewTask.paintFlags = if (isChecked) {
                    textViewTask.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    textViewTask.paintFlags and android.graphics.Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }
                // Llama a la función para guardar las tareas
                (context as MainActivity).guardarTareas()
            }


            botonEliminarTareas.setOnClickListener {
                tasks.removeAt(position)
                notifyDataSetChanged()
                (context as MainActivity).guardarTareas()
                Toast.makeText(context.applicationContext, "Tarea Eliminada", Toast.LENGTH_SHORT).show()
            }
            botonEditarTareas.setOnClickListener {
                mostrarDialogoEdicion(task, position)
            }
        }

        return view
    }

    private fun mostrarDialogoEdicion(task: Task, position: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Editar Tarea")

        val input = EditText(context)
        input.setText(task.description)
        builder.setView(input)

        builder.setPositiveButton("Guardar") { dialog, _ ->
            task.description = input.text.toString()
            notifyDataSetChanged()
            (context as MainActivity).guardarTareas()
            Toast.makeText(context.applicationContext, "Tarea Editada", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }
}
