package com.apple.iosnotes.clone

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.apple.iosnotes.clone.adapter.NoteAdapter
import com.apple.iosnotes.clone.data.NoteRepository
import com.apple.iosnotes.clone.databinding.ActivityNotesListBinding
import com.apple.iosnotes.clone.model.Note

class NotesListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotesListBinding
    private lateinit var repository: NoteRepository
    private var folderId: String = "notes"
    private var folderName: String = "备忘录"

    private lateinit var pinnedAdapter: NoteAdapter
    private lateinit var normalAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotesListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = NoteRepository.getInstance(this)
        folderId = intent.getStringExtra("folder_id") ?: "notes"
        folderName = intent.getStringExtra("folder_name") ?: "备忘录"

        binding.tvFolderTitle.text = folderName

        setupRecyclerViews()
        setupListeners()
    }

    override fun onResume() {
        super.onResume()
        refreshNotes()
    }

    private fun setupRecyclerViews() {
        pinnedAdapter = NoteAdapter(emptyList()) { note -> openNote(note) }
        normalAdapter = NoteAdapter(emptyList()) { note -> openNote(note) }

        binding.rvPinnedNotes.layoutManager = LinearLayoutManager(this)
        binding.rvPinnedNotes.adapter = pinnedAdapter

        binding.rvNotes.layoutManager = LinearLayoutManager(this)
        binding.rvNotes.adapter = normalAdapter
    }

    private fun refreshNotes(query: String = "") {
        val allNotes = repository.getNotesByFolder(folderId)
        val filtered = if (query.isEmpty()) {
            allNotes
        } else {
            allNotes.filter { it.title.contains(query, true) || it.content.contains(query, true) }
        }

        val pinned = filtered.filter { it.isPinned }
        val normal = filtered.filter { !it.isPinned }

        binding.cardPinned.visibility = if (pinned.isNotEmpty()) View.VISIBLE else View.GONE
        binding.tvPinnedHeader.visibility = if (pinned.isNotEmpty()) View.VISIBLE else View.GONE
        pinnedAdapter.updateData(pinned)

        binding.cardNotes.visibility = if (normal.isNotEmpty()) View.VISIBLE else View.GONE
        binding.tvNotesHeader.visibility = if (normal.isNotEmpty() && pinned.isNotEmpty()) View.VISIBLE else View.GONE
        normalAdapter.updateData(normal)

        binding.tvNotesCount.text = getString(R.string.notes_count_format, filtered.size)
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener { finish() }

        binding.btnCreateNote.setOnClickListener {
            val intent = Intent(this, NoteDetailActivity::class.java).apply {
                putExtra("folder_id", folderId)
            }
            startActivity(intent)
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                refreshNotes(s?.toString().orEmpty())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun openNote(note: Note) {
        val intent = Intent(this, NoteDetailActivity::class.java).apply {
            putExtra("note_id", note.id)
            putExtra("folder_id", note.folderId)
        }
        startActivity(intent)
    }
}
