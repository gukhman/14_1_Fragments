package com.example.a14_1_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class NotesFragment : Fragment() {

    private lateinit var noteAdapter: NoteAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var noteInput: EditText
    private lateinit var addButton: Button
    private lateinit var databaseHelper: NotesDatabaseHelper
    private val notes = mutableListOf<Note>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notes, container, false)

        // Инициализация элементов интерфейса
        noteInput = view.findViewById(R.id.noteInput)
        addButton = view.findViewById(R.id.addButton)
        recyclerView = view.findViewById(R.id.recyclerView)

        // Настройка базы данных
        databaseHelper = NotesDatabaseHelper(requireContext())
        notes.addAll(databaseHelper.getAllNotes()) // Загрузка заметок из базы данных

        // Настройка адаптера и RecyclerView
        noteAdapter = NoteAdapter(notes) { note ->
            // Переход на EditNoteFragment
            val editNoteFragment = EditNoteFragment()
            val bundle = Bundle()
            bundle.putSerializable("note", note)
            editNoteFragment.arguments = bundle

            // Заменяем текущий фрагмент на EditNoteFragment
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, editNoteFragment)
                .addToBackStack(null) // Добавляем в стек, чтобы можно было вернуться
                .commit()
        }
        recyclerView.adapter = noteAdapter
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext()) // Добавляем LayoutManager для списка

        // Обработчик кнопки добавления
        addButton.setOnClickListener {
            val text = noteInput.text.toString()
            if (text.isNotEmpty()) {
                val note = Note(
                    id = notes.size + 1,
                    text = text,
                    timestamp = System.currentTimeMillis()
                )
                databaseHelper.insertNote(note) // Сохраняем заметку в базе данных

                // Добавляем заметку в начало списка
                noteAdapter.addNote(note) // Используем метод для добавления заметки
                recyclerView.scrollToPosition(0) // Прокручиваем к первой заметке
                noteInput.text.clear() // Очищаем поле ввода
            }
        }

        return view
    }
}