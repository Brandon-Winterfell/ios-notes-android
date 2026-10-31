package com.apple.iosnotes.clone

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.apple.iosnotes.clone.adapter.FolderAdapter
import com.apple.iosnotes.clone.data.NoteRepository
import com.apple.iosnotes.clone.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var repository: NoteRepository
    private lateinit var adapter: FolderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = NoteRepository.getInstance(this)

        setupRecyclerView()
        setupListeners()
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    private fun setupRecyclerView() {
        adapter = FolderAdapter(repository.getFolders(), repository) { folder ->
            val intent = Intent(this, NotesListActivity::class.java).apply {
                putExtra("folder_id", folder.id)
                putExtra("folder_name", folder.name)
            }
            startActivity(intent)
        }

        binding.rvFolders.layoutManager = LinearLayoutManager(this)
        binding.rvFolders.adapter = adapter
    }

    private fun setupListeners() {
        binding.btnNewFolder.setOnClickListener {
            showNewFolderDialog()
        }

        binding.btnQuickNewNote.setOnClickListener {
            val intent = Intent(this, NoteDetailActivity::class.java).apply {
                putExtra("folder_id", "notes")
            }
            startActivity(intent)
        }
    }

    private fun showNewFolderDialog() {
        val input = EditText(this)
        input.hint = getString(R.string.folder_name_hint)

        AlertDialog.Builder(this)
            .setTitle(R.string.new_folder_title)
            .setView(input)
            .setPositiveButton(R.string.save) { _, _ ->
                val name = input.text.toString().trim()
                if (name.isNotEmpty()) {
                    repository.addFolder(name)
                    adapter.notifyDataSetChanged()
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }
}
