package com.apple.iosnotes.clone

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.apple.iosnotes.clone.data.NoteRepository
import com.apple.iosnotes.clone.databinding.ActivityNoteDetailBinding
import com.apple.iosnotes.clone.model.Note
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class NoteDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoteDetailBinding
    private lateinit var repository: NoteRepository
    private var currentNote: Note? = null
    private var folderId: String = "notes"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = NoteRepository.getInstance(this)
        folderId = intent.getStringExtra("folder_id") ?: "notes"
        val noteId = intent.getStringExtra("note_id")

        if (noteId != null) {
            currentNote = repository.getNoteById(noteId)
        }

        if (currentNote == null) {
            currentNote = Note(
                id = UUID.randomUUID().toString(),
                title = "",
                content = "",
                folderId = folderId
            )
        }

        initViews()
        setupListeners()
    }

    private fun initViews() {
        val note = currentNote ?: return
        binding.etTitle.setText(note.title)
        binding.etContent.setText(note.content)

        val formatter = SimpleDateFormat("yyyy年M月d日 HH:mm", Locale.getDefault())
        binding.tvDate.text = formatter.format(Date(note.updatedAt))
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            saveCurrentNote()
            finish()
        }

        binding.btnDone.setOnClickListener {
            saveCurrentNote()
            finish()
        }

        binding.btnToolbarDelete.setOnClickListener {
            currentNote?.let { repository.deleteNote(it.id) }
            Toast.makeText(this, "备忘录已移至最近删除", Toast.LENGTH_SHORT).show()
            finish()
        }

        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                saveCurrentNote()
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        binding.etTitle.addTextChangedListener(watcher)
        binding.etContent.addTextChangedListener(watcher)
    }

    private fun saveCurrentNote() {
        val note = currentNote ?: return
        val titleText = binding.etTitle.text.toString().trim()
        val contentText = binding.etContent.text.toString().trim()

        if (titleText.isEmpty() && contentText.isEmpty()) {
            return
        }

        note.title = titleText.ifEmpty { "无标题备忘录" }
        note.content = contentText
        note.updatedAt = System.currentTimeMillis()

        repository.saveNote(note)
    }

    override fun onBackPressed() {
        saveCurrentNote()
        super.onBackPressed()
    }
}
