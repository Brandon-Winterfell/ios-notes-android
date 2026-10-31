package com.apple.iosnotes.clone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apple.iosnotes.clone.databinding.ItemNoteBinding
import com.apple.iosnotes.clone.model.Note
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NoteAdapter(
    private var notes: List<Note>,
    private val onClick: (Note) -> Unit
) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    fun updateData(newNotes: List<Note>) {
        this.notes = newNotes
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notes[position]
        holder.binding.tvNoteTitle.text = note.title.ifEmpty { "新备忘录" }
        holder.binding.tvNoteSnippet.text = note.content.ifEmpty { "无其他文本" }

        val formatter = SimpleDateFormat("MM/dd", Locale.getDefault())
        holder.binding.tvNoteDate.text = formatter.format(Date(note.updatedAt))

        holder.binding.ivPinBadge.visibility = if (note.isPinned) View.VISIBLE else View.GONE
        holder.binding.divider.visibility = if (position == notes.size - 1) View.GONE else View.VISIBLE

        holder.itemView.setOnClickListener { onClick(note) }
    }

    override fun getItemCount(): Int = notes.size
}
