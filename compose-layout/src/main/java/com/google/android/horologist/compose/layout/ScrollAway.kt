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

package com.google.android.horologist.compose.layout

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyListState
import androidx.wear.compose.material.scrollAway
import com.google.android.horologist.compose.navscaffold.ScalingLazyColumnScrollableState
import androidx.wear.compose.material.scrollAway as scrollAwayCompose

/**
 * Scroll an item vertically in/out of view based on a [ScrollState].
 * Typically used to scroll a [TimeText] item out of view as the user starts to scroll a
 * vertically scrollable [Column] of items upwards and bring additional items into view.
 *
 * @param scrollState The [ScrollState] to used as the basis for the scroll-away.
 * @param offset Adjustment to the starting point for scrolling away. Positive values result in
 * the scroll away starting later.
 */
@Deprecated(
    "Replaced by Wear Compose scrollAway",
    replaceWith = ReplaceWith(
        "this.scrollAway(scrollState, offset)",
        "androidx.wear.compose.material.scrollAway",
    ),
)
public fun Modifier.scrollAway(
    scrollState: ScrollState,
    offset: Dp = 0.dp,
): Modifier = scrollAwayCompose(scrollState, offset)

/**
 * Scroll an item vertically in/out of view based on a [LazyListState].
 * Typically used to scroll a [TimeText] item out of view as the user starts to scroll
 * a [LazyColumn] of items upwards and bring additional items into view.
 *
 * @param scrollState The [LazyListState] to used as the basis for the scroll-away.
 * @param itemIndex The item for which the scroll offset will trigger scrolling away.
 * @param offset Adjustment to the starting point for scrolling away. Positive values result in
 * the scroll away starting later.
 */
@Deprecated(
    "Replaced by Wear Compose scrollAway",
    replaceWith = ReplaceWith(
        "this.scrollAway(scrollState, itemIndex, offset)",
        "androidx.wear.compose.material.scrollAway",
    ),
)
public fun Modifier.scrollAway(
    scrollState: LazyListState,
    itemIndex: Int = 0,
    offset: Dp = 0.dp,
): Modifier = scrollAwayCompose(scrollState, itemIndex, offset)

/**
 * Scroll an item vertically in/out of view based on a [ScalingLazyListState].
 * Typically used to scroll a [TimeText] item out of view as the user starts to scroll
 * a [ScalingLazyColumn] of items upwards and bring additional items into view.
 *
 * @param scrollState The [ScalingLazyListState] to used as the basis for the scroll-away.
 * @param itemIndex The item for which the scroll offset will trigger scrolling away.
 * @param offset Adjustment to the starting point for scrolling away. Positive values result in
 * the scroll away starting later, negative values start scrolling away earlier.
 */
@Deprecated(
    "Replaced by Wear Compose scrollAway",
    replaceWith = ReplaceWith(
        "this.scrollAway(scrollState, itemIndex, offset)",
        "androidx.wear.compose.material.scrollAway",
    ),
)
public fun Modifier.scrollAway(
    scrollState: ScalingLazyListState,
    itemIndex: Int = 1,
    offset: Dp = 0.dp,
): Modifier = scrollAwayCompose(scrollState, itemIndex, offset)

/**
 * Scroll an item vertically in/out of view based on a [ScalingLazyListState].
 * Typically used to scroll a [TimeText] item out of view as the user starts to scroll
 * a [ScalingLazyColumn] of items upwards and bring additional items into view.
 *
 * @param scalingLazyColumnState The list config.
 */
public fun Modifier.scrollAway(
    scalingLazyColumnState: ScalingLazyColumnState,
): Modifier = composed {
    val offset = with(LocalDensity.current) {
        scalingLazyColumnState.initialScrollPosition.offsetPx.toDp()
    }
    scrollAwayCompose(
        scrollState = scalingLazyColumnState.state,
        itemIndex = scalingLazyColumnState.initialScrollPosition.index,
        offset = offset,
    )
}

internal fun Modifier.scrollAway(
    scrollState: State<ScrollableState?>,
): Modifier = composed {
    when (val state = scrollState.value) {
        is ScalingLazyColumnScrollableState -> {
            val offsetDp = with(LocalDensity.current) {
                state.initialOffsetPx.toDp()
            }
            this.scrollAway(state.scalingLazyListState, state.initialIndex, offsetDp)
        }
        is ScalingLazyListState -> this.scrollAway(state)
        is LazyListState -> this.scrollAway(state)
        is ScrollState -> this.scrollAway(state)
        // Disabled
        null -> this.hidden()
        // Enabled but no scroll state
        else -> this
    }
}

internal fun Modifier.hidden(): Modifier = layout { _, _ -> layout(0, 0) {} }
