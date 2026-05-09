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
import com.metrolist.music.db.entities.PlaylistPlayCountResult
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import com.metrolist.music.db.entities.PlaylistSongStatResult
import javax.inject.Inject

@HiltViewModel
class PlaylistStatsViewModel
@Inject
constructor(
    @ApplicationContext context: Context,
    val database: MusicDatabase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val playlistId = savedStateHandle.get<String>("playlistId")!!

    val totalListenTime: Flow<Long?> = database.getPlaylistTotalListenTime(playlistId)
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    val playCounts: Flow<PlaylistPlayCountResult?> = database.getPlaylistPlayCounts(playlistId)
        .stateIn(viewModelScope, SharingStarted.Lazily, PlaylistPlayCountResult(0, 0))

    val playlist = database.playlist(playlistId)
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val playlistSongs = database.playlistSongs(playlistId)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val songStats = database.getPlaylistSongStatsByPlaylistId(playlistId)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}
