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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.metrolist.music.R
import com.metrolist.music.ui.component.IconButton
import com.metrolist.music.ui.components.stats.StatCard
import com.metrolist.music.ui.utils.backToMain
import com.metrolist.music.utils.makeTimeString
import com.metrolist.music.viewmodels.SongStatsViewModel
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongStatsScreen(
    songId: String,
    navController: NavController,
    viewModel: SongStatsViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    val playCount by viewModel.playCount.collectAsStateWithLifecycle(initialValue = null)
    val listenDates by viewModel.listenDates.collectAsStateWithLifecycle(initialValue = null)
    val rank by viewModel.playTimeRank.collectAsStateWithLifecycle(initialValue = 0)
    val hourlyDistribution by viewModel.hourlyDistribution.collectAsStateWithLifecycle(initialValue = emptyMap())
    val weeklyDistribution by viewModel.weeklyDistribution.collectAsStateWithLifecycle(initialValue = emptyMap())

    val formatter = remember { DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM) }

    val avgSessionLength = remember(playCount) {
        val pc = playCount
        if (pc != null && pc.playCount > 0 && pc.totalTime != null) {
            pc.totalTime / pc.playCount
        } else 0L
    }

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
                    text = stringResource(R.string.song_stats),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp),
                )
            }

            // Key stats cards row 1
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
    StatCard(
        icon = R.drawable.play,
        value = playCount?.playCount?.toString() ?: "0",
        label = stringResource(R.string.plays),
        modifier = Modifier.weight(1f),
    )
    StatCard(
        icon = R.drawable.library_music,
        value = playCount?.totalTime?.let { makeTimeString(it) } ?: "0s",
        label = stringResource(R.string.total_time),
        modifier = Modifier.weight(1f),
    )
    }
}

item { Spacer(modifier = Modifier.height(12.dp)) }

// Key stats cards row 2
item {
Row(
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    modifier = Modifier.fillMaxWidth(),
) {
    StatCard(
        icon = R.drawable.stats,
        value = if (rank > 0) "#${rank}" else "-",
        label = stringResource(R.string.rank),
        modifier = Modifier.weight(1f),
    )
    StatCard(
        icon = R.drawable.history,
        value = if (avgSessionLength > 0) makeTimeString(avgSessionLength) else "0s",
        label = stringResource(R.string.avg_session),
        modifier = Modifier.weight(1f),
    )
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            // Listening history section
            item {
                Text(
                    text = stringResource(R.string.listening_history),
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
                                text = stringResource(R.string.first_listened),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            Text(
                                text = listenDates?.firstListenDate?.format(formatter) ?: "-",
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
                                text = stringResource(R.string.last_listened),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            Text(
                                text = listenDates?.lastListenDate?.format(formatter) ?: "-",
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
                                text = stringResource(R.string.avg_session_length),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            Text(
                                text = if (avgSessionLength > 0) makeTimeString(avgSessionLength) else "0s",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                            )
                        }
                    }
                }
            }

            // Hourly distribution
            if (hourlyDistribution.isNotEmpty()) {
                item { Spacer(modifier = Modifier.height(24.dp)) }
                item {
                    Text(
                        text = stringResource(R.string.listening_patterns),
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
                            val maxHour = hourlyDistribution.maxByOrNull { it.value }?.key ?: -1
                            if (maxHour >= 0) {
                                val hourStr = when {
                                    maxHour == 0 -> "12 AM"
                                    maxHour < 12 -> "${maxHour} AM"
                                    maxHour == 12 -> "12 PM"
                                    else -> "${maxHour - 12} PM"
                                }
                                Text(
                                    text = stringResource(R.string.most_active_hour_format, hourStr),
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }
                        }
                    }
                }
            }
        }

        TopAppBar(
            title = { Text(stringResource(R.string.song_stats)) },
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


