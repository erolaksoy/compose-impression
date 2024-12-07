package com.erolaksoy.impression.wrapper

import androidx.compose.foundation.lazy.LazyListState

internal class LazyListStateWrapper(private val lazyListState: LazyListState) : LazyLayoutWrapper {

    override val visibleItems: List<ImpressionVisibleItemInfo>
        get() = lazyListState.layoutInfo.visibleItemsInfo.map {
            ImpressionVisibleItemInfo(
                key = it.key,
                offset = it.offset,
                size = it.size,
            )
        }

    override suspend fun visibilityPercent(itemKey: Any): Float {
        val item = visibleItems.firstOrNull { it.key == itemKey } ?: return 0f
        return calculateVisibilityPercent(
            viewportStartOffset = lazyListState.layoutInfo.viewportStartOffset,
            viewportEndOffset = lazyListState.layoutInfo.viewportEndOffset,
            itemOffset = item.offset,
            itemSize = item.size
        )
    }
}