package com.example.a14_1_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment

class EditNoteFragment : Fragment() {

    private lateinit var note: Note
    private lateinit var noteInput: EditText
    private lateinit var saveButton: Button
    private lateinit var databaseHelper: NotesDatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_note, container, false)

        // Инициализация
        noteInput = view.findViewById(R.id.noteInput)
        saveButton = view.findViewById(R.id.saveButton)

        // Получаем данные заметки из аргументов
        note = arguments?.getSerializable("note") as Note

        noteInput.setText(note.text)

        // Инициализация БД
        databaseHelper = NotesDatabaseHelper(requireContext())

        // Обработчик кнопки сохранения
        saveButton.setOnClickListener {
            val updatedText = noteInput.text.toString()
            if (updatedText.isNotEmpty()) {

                // Обновляем заметку
                note.text = updatedText
                databaseHelper.updateNote(note)

                // Возвращаемся к предыдущему фрагменту
                requireActivity().supportFragmentManager.popBackStack()
            }
        }

        return view
    }
}