package com.example.a14_1_fragments

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class NotesDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "notes.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_NAME = "notes"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TEXT = "text"
        private const val COLUMN_TIMESTAMP = "timestamp"
        private const val COLUMN_IS_COMPLETED = "isCompleted"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TEXT TEXT,
                $COLUMN_TIMESTAMP INTEGER,
                $COLUMN_IS_COMPLETED INTEGER DEFAULT 0
            )
        """
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertNote(note: Note) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TEXT, note.text)
            put(COLUMN_TIMESTAMP, note.timestamp)
            put(COLUMN_IS_COMPLETED, if (note.isCompleted) 1 else 0)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllNotes(): List<Note> {
        val notes = mutableListOf<Note>()
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, "$COLUMN_TIMESTAMP DESC")

        cursor.use {
            while (it.moveToNext()) {
                val note = Note(
                    id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID)),
                    text = it.getString(it.getColumnIndexOrThrow(COLUMN_TEXT)),
                    timestamp = it.getLong(it.getColumnIndexOrThrow(COLUMN_TIMESTAMP)),
                    isCompleted = it.getInt(it.getColumnIndexOrThrow(COLUMN_IS_COMPLETED)) == 1
                )
                notes.add(note)
            }
        }
        db.close()
        return notes
    }

    fun deleteNote(noteId: Int) {
        val db = writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(noteId.toString()))
        db.close()
    }

    fun updateNote(note: Note) {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put("text", note.text)
            put("timestamp", note.timestamp)
        }
        db.update("notes", contentValues, "id = ?", arrayOf(note.id.toString()))
        db.close()
    }

}
