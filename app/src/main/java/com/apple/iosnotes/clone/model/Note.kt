package com.apple.iosnotes.clone.model

import java.io.Serializable

data class ChecklistItem(
    val id: String,
    var text: String,
    var done: Boolean = false
) : Serializable

data class Note(
    val id: String,
    var title: String,
    var content: String,
    var folderId: String = "notes",
    var isPinned: Boolean = false,
    var isChecklist: Boolean = false,
    var checklistItems: MutableList<ChecklistItem> = mutableListOf(),
    var sketchPath: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    var updatedAt: Long = System.currentTimeMillis(),
    var isDeleted: Boolean = false
) : Serializable
