package com.apple.iosnotes.clone.data

import android.content.Context
import com.apple.iosnotes.clone.model.ChecklistItem
import com.apple.iosnotes.clone.model.Folder
import com.apple.iosnotes.clone.model.Note
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.UUID

class NoteRepository private constructor(context: Context) {

    private val prefs = context.getSharedPreferences("ios_notes_storage", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val notesList = mutableListOf<Note>()
    private val foldersList = mutableListOf<Folder>()

    init {
        loadData()
        if (notesList.isEmpty()) {
            initDefaultNotes()
        }
    }

    companion object {
        @Volatile
        private var instance: NoteRepository? = null

        fun getInstance(context: Context): NoteRepository {
            return instance ?: synchronized(this) {
                instance ?: NoteRepository(context.applicationContext).also { instance = it }
            }
        }
    }

    private fun loadData() {
        val notesJson = prefs.getString("saved_notes", null)
        if (!notesJson.isNullOrEmpty()) {
            val type = object : TypeToken<List<Note>>() {}.type
            val saved: List<Note> = gson.fromJson(notesJson, type)
            notesList.clear()
            notesList.addAll(saved)
        }

        val foldersJson = prefs.getString("saved_folders", null)
        if (!foldersJson.isNullOrEmpty()) {
            val type = object : TypeToken<List<Folder>>() {}.type
            val saved: List<Folder> = gson.fromJson(foldersJson, type)
            foldersList.clear()
            foldersList.addAll(saved)
        } else {
            foldersList.add(Folder("all", "全部备忘录", isSystem = true))
            foldersList.add(Folder("notes", "备忘录", isSystem = true))
            foldersList.add(Folder("work", "工作与项目"))
            foldersList.add(Folder("life", "生活与灵感"))
            foldersList.add(Folder("trash", "最近删除", isTrash = true))
            saveFolders()
        }
    }

    private fun initDefaultNotes() {
        notesList.add(
            Note(
                id = UUID.randomUUID().toString(),
                title = "欢迎使用 iOS 备忘录",
                content = "本应用深度复刻了苹果 iOS 原生备忘录的人机交互体验与字体间距。\n\n特性包括：\n• 苹果 San Francisco 字体规格排版\n• Inset Grouped 圆角卡片\n• 沉浸式琥珀黄待办清单\n• 自由手绘涂鸦速写\n• GitHub Actions 一键打包 APK",
                folderId = "notes",
                isPinned = true
            )
        )
        notesList.add(
            Note(
                id = UUID.randomUUID().toString(),
                title = "周末购物清单",
                content = "记得在超市购买以下生鲜和生活用品：",
                folderId = "life",
                isChecklist = true,
                checklistItems = mutableListOf(
                    ChecklistItem("1", "新鲜牛油果与法棍面包", true),
                    ChecklistItem("2", "意式深烘咖啡豆 500g", true),
                    ChecklistItem("3", "燕麦奶与有机鸡蛋", false),
                    ChecklistItem("4", "海盐黑巧克力", false)
                )
            )
        )
        saveNotes()
    }

    fun getFolders(): List<Folder> = foldersList

    fun getNotesByFolder(folderId: String): List<Note> {
        return if (folderId == "all") {
            notesList.filter { !it.isDeleted }
        } else if (folderId == "trash") {
            notesList.filter { it.isDeleted }
        } else {
            notesList.filter { it.folderId == folderId && !it.isDeleted }
        }
    }

    fun getNoteById(id: String): Note? = notesList.find { it.id == id }

    fun saveNote(note: Note) {
        val index = notesList.indexOfFirst { it.id == note.id }
        if (index >= 0) {
            notesList[index] = note
        } else {
            notesList.add(0, note)
        }
        saveNotes()
    }

    fun deleteNote(id: String) {
        val note = getNoteById(id) ?: return
        note.isDeleted = true
        saveNotes()
    }

    fun addFolder(name: String): Folder {
        val folder = Folder(UUID.randomUUID().toString(), name)
        foldersList.add(folder)
        saveFolders()
        return folder
    }

    private fun saveNotes() {
        prefs.edit().putString("saved_notes", gson.toJson(notesList)).apply()
    }

    private fun saveFolders() {
        prefs.edit().putString("saved_folders", gson.toJson(foldersList)).apply()
    }
}
