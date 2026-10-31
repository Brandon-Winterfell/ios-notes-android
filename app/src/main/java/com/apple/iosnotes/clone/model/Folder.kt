package com.apple.iosnotes.clone.model

import java.io.Serializable

data class Folder(
    val id: String,
    var name: String,
    val isSystem: Boolean = false,
    val isTrash: Boolean = false
) : Serializable
