/**
 * Metrolist Project (C) 2026
 * Licensed under GPL-3.0 | See git history for contributors
 */

package com.metrolist.music.viewmodels

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.metrolist.music.db.MusicDatabase
import com.metrolist.music.db.entities.SongPlayCountResult
import com.metrolist.music.db.entities.SongListenDatesResult
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SongStatsViewModel
@Inject
constructor(
    @ApplicationContext context: Context,
    val database: MusicDatabase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val songId = savedStateHandle.get<String>("songId")!!

    val playCount: Flow<SongPlayCountResult?> = database.getSongPlayCount(songId)
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val listenDates: Flow<SongListenDatesResult?> = database.getSongListenDates(songId)
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val playTimeRank: Flow<Int> = database.getSongPlayTimeRank(songId)
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    // Hourly distribution - which hours of day the song is most played
    val hourlyDistribution: Flow<Map<Int, Int>> = database.events()
        .map { events ->
            events.filter { it.event.songId == songId }
                .groupingBy { it.event.timestamp.hour }
                .eachCount()
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyMap())

    // Weekly distribution - which days of week
    val weeklyDistribution: Flow<Map<Int, Int>> = database.events()
        .map { events ->
            events.filter { it.event.songId == songId }
                .groupingBy { it.event.timestamp.dayOfWeek.value }
                .eachCount()
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyMap())
}
