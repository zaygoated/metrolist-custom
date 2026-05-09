/**
 * Metrolist Project (C) 2026
 * Licensed under GPL-3.0 | See git history for contributors
 */

package com.metrolist.music.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.metrolist.music.R
import com.metrolist.music.ui.component.IconButton
import com.metrolist.music.ui.component.SongListItem
import com.metrolist.music.ui.components.stats.StatCard
import com.metrolist.music.ui.utils.backToMain
import com.metrolist.music.utils.makeTimeString
import com.metrolist.music.viewmodels.PlaylistStatsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistStatsScreen(
    playlistId: String,
    navController: NavController,
    viewModel: PlaylistStatsViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    val totalListenTime by viewModel.totalListenTime.collectAsStateWithLifecycle(initialValue = 0)
    val playCounts by viewModel.playCounts.collectAsStateWithLifecycle(initialValue = null)
    val playlist by viewModel.playlist.collectAsStateWithLifecycle(initialValue = null)
    val playlistSongs by viewModel.playlistSongs.collectAsStateWithLifecycle(initialValue = emptyList())
    val songStats by viewModel.songStats.collectAsStateWithLifecycle(initialValue = emptyList())

    val totalDuration = remember(playlistSongs) {
        playlistSongs.sumOf { it.song.song.duration * 1000L }
    }

    val uniqueSongs = playCounts?.uniqueSongs ?: 0
    val totalPlays = playCounts?.totalPlays ?: 0
    val avgPlaysPerSong = if (uniqueSongs > 0) totalPlays / uniqueSongs else 0

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding =
                PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp,
                ),
        ) {
            item {
                Spacer(
                    modifier = Modifier.windowInsetsPadding(
                        WindowInsets.systemBars.only(WindowInsetsSides.Top)
                    )
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                Text(
                    text = stringResource(R.string.playlist_stats),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp),
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    ),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                    ) {
                        Text(
                            text = playlist?.playlist?.name ?: "",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = pluralStringResource(R.plurals.n_song, playlistSongs.size, playlistSongs.size),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            // Stats cards row 1
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    StatCard(
                        icon = R.drawable.library_music,
                        value = totalListenTime?.let { makeTimeString(it) } ?: "0s",
                        label = stringResource(R.string.total_listen_time),
                        modifier = Modifier.weight(1f),
                    )
                    StatCard(
                        icon = R.drawable.play,
                        value = totalPlays.toString(),
                        label = stringResource(R.string.total_plays),
                        modifier = Modifier.weight(1f),
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(12.dp)) }

            // Stats cards row 2
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    StatCard(
                        icon = R.drawable.stats,
                        value = uniqueSongs.toString(),
                        label = stringResource(R.string.unique_songs),
                        modifier = Modifier.weight(1f),
                    )
                    StatCard(
                        icon = R.drawable.replay,
                        value = avgPlaysPerSong.toString(),
                        label = stringResource(R.string.avg_plays_per_song),
                        modifier = Modifier.weight(1f),
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            // Stats section
            item {
                Text(
                    text = stringResource(R.string.playlist_details),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 4.dp, bottom = 8.dp),
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    ),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = stringResource(R.string.total_duration),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            Text(
                                text = if (totalDuration > 0) makeTimeString(totalDuration) else "0s",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = stringResource(R.string.songs_skipped),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            Text(
                                text = (playlistSongs.size - uniqueSongs).toString(),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = stringResource(R.string.completion_rate),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            Text(
                                text = if (totalDuration > 0 && totalListenTime != null) {
                                    "${((totalListenTime!! * 100) / totalDuration).coerceAtMost(100)}%"
                                } else "0%",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                            )
                        }
                    }
                }
            }

            if (playlistSongs.isNotEmpty()) {
                item { Spacer(modifier = Modifier.height(24.dp)) }

                item {
                    Text(
                        text = stringResource(R.string.songs),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp),
                    )
                }

                items(playlistSongs, key = { it.id }) { playlistSong ->
                    val stat = songStats.find { it.songId == playlistSong.songId }
                    val playCount = stat?.playCount ?: 0
                    val totalTime = stat?.totalTime ?: 0L
                    SongListItem(
                        song = playlistSong.song,
                        subtitleOverride = stringResource(R.string.song_stat_subtitle, playCount, makeTimeString(totalTime)),
                        showDownloadIcon = false,
                        showLikedIcon = false,
                        showInLibraryIcon = false,
                        isSwipeable = false,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }

        TopAppBar(
            title = { Text(stringResource(R.string.playlist_stats)) },
            navigationIcon = {
                IconButton(
                    onClick = { navController.navigateUp() },
                    onLongClick = { navController.backToMain() },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.arrow_back),
                        contentDescription = null,
                    )
                }
            },
        )
    }
}
