/**
 * Metrolist Project (C) 2026
 * Licensed under GPL-3.0 | See git history for contributors
 */

package com.metrolist.music.db.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation
import java.time.LocalDateTime

data class SongPlayCountResult(
    val playCount: Int,
    val totalTime: Long?
)

data class SongListenDatesResult(
    val firstListenDate: LocalDateTime?,
    val lastListenDate: LocalDateTime?
)

data class PlaylistSongStatResult(
    val songId: String,
    val totalTime: Long,
    val playCount: Int
)

data class PlaylistPlayCountResult(
    val uniqueSongs: Int,
    val totalPlays: Int
)
