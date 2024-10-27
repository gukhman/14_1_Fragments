package com.example.a14_1_fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NoteAdapter(
    private val notes: MutableList<Note>,
    private val onNoteClick: (Note) -> Unit // Параметр для обработки клика
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val noteId = itemView.findViewById<TextView>(R.id.noteId)
        private val noteText = itemView.findViewById<TextView>(R.id.noteText)
        private val checkBox = itemView.findViewById<CheckBox>(R.id.checkBox)
        private val noteTimestamp = itemView.findViewById<TextView>(R.id.noteTimestamp)

        init {
            itemView.setOnClickListener {
                // Обрабатываем клик по элементу списка
                onNoteClick(notes[adapterPosition])
            }
        }

        fun bind(note: Note) {
            noteId.text = String.format(Locale.getDefault(), "%d", note.id) // Используем String.format
            noteText.text = note.text
            checkBox.isChecked = note.isCompleted
            noteTimestamp.text = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(Date(note.timestamp))

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                note.isCompleted = isChecked
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(notes[position])
    }

    override fun getItemCount() = notes.size

    fun addNote(note: Note) {
        notes.add(0, note) // Добавляем в начало
        notifyItemInserted(0)
    }

    fun removeNote(note: Note) {
        val position = notes.indexOf(note)
        if (position >= 0) {
            notes.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}
