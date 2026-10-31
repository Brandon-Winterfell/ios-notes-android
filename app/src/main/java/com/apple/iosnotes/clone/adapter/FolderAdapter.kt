package com.apple.iosnotes.clone.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apple.iosnotes.clone.data.NoteRepository
import com.apple.iosnotes.clone.databinding.ItemFolderBinding
import com.apple.iosnotes.clone.model.Folder

class FolderAdapter(
    private val folders: List<Folder>,
    private val repository: NoteRepository,
    private val onClick: (Folder) -> Unit
) : RecyclerView.Adapter<FolderAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemFolderBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val folder = folders[position]
        holder.binding.tvFolderName.text = folder.name
        val count = repository.getNotesByFolder(folder.id).size
        holder.binding.tvFolderCount.text = count.toString()

        holder.itemView.setOnClickListener { onClick(folder) }
    }

    override fun getItemCount(): Int = folders.size
}
