package com.erolaksoy.impression.wrapper

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.lazy.grid.LazyGridState

internal class LazyGridStateWrapper(private val state: LazyGridState) : LazyLayoutWrapper {

    override val visibleItems: List<ImpressionVisibleItemInfo>
        get() = state.layoutInfo.visibleItemsInfo.map {
            val isVertical = state.layoutInfo.orientation == Orientation.Vertical
            ImpressionVisibleItemInfo(
                key = it.key,
                offset = if (isVertical) it.offset.y else it.offset.x,
                size = if (isVertical) it.size.height else it.size.width
            )
        }

    override suspend fun visibilityPercent(itemKey: Any): Float {
        val item = visibleItems.firstOrNull { it.key == itemKey } ?: return 0f
        return calculateVisibilityPercent(
            viewportStartOffset = state.layoutInfo.viewportStartOffset,
            viewportEndOffset = state.layoutInfo.viewportEndOffset,
            itemOffset = item.offset,
            itemSize = item.size
        )
    }
}