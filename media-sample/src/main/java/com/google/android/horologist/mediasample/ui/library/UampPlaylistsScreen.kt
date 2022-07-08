/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.horologist.mediasample.ui.library

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.ScalingLazyListState
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.items
import com.google.android.horologist.compose.layout.StateUtils.rememberStateWithLifecycle
import com.google.android.horologist.compose.navscaffold.scrollableColumn
import com.google.android.horologist.media.ui.components.MediaChip
import com.google.android.horologist.mediasample.R
import com.google.android.horologist.mediasample.domain.Settings

@Composable
fun UampPlaylistsScreen(
    focusRequester: FocusRequester,
    uampPlaylistsScreenViewModel: UampPlaylistsScreenViewModel,
    state: ScalingLazyListState,
    onPlayClick: () -> Unit,
    modifier: Modifier = Modifier,
    settingsState: Settings?,
) {
    val uiState by rememberStateWithLifecycle(uampPlaylistsScreenViewModel.uiState)

    ScalingLazyColumn(
        modifier = modifier
            .fillMaxSize()
            .scrollableColumn(focusRequester, state),
        state = state
    ) {
        val items = uiState.items
        item {
            Text(
                stringResource(id = R.string.horologist_library),
                style = MaterialTheme.typography.body1
            )
        }
        if (items != null) {
            items(items) {
                val mediaItem =
                    if (settingsState?.showArtworkOnChip == true) it else it.copy(artworkUri = null)
                MediaChip(
                    mediaItem = mediaItem,
                    onClick = {
                        uampPlaylistsScreenViewModel.play(it)
                        onPlayClick()
                    },
                    defaultTitle = stringResource(id = R.string.horologist_no_title),
                )
            }
        } else {
            item {
                Text("Loading...", style = MaterialTheme.typography.caption3)
            }
        }
    }
}