package com.erolaksoy.impression

import androidx.annotation.FloatRange
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState

class VisibilityPercentImpressionValidator(
    @FloatRange(0.0, 1.0) private val visibilityPercentThreshold: Float = VISIBILITY_THRESHOLD,
) : ImpressionValidator {

    override suspend fun isValid(state: LazyListState, key: Any): Boolean {
        val item = state.layoutInfo.visibleItemsInfo.firstOrNull { it.key == key } ?: return false
        val itemVisibilityPercent = state.visibilityPercent(item)
        return itemVisibilityPercent >= visibilityPercentThreshold
    }

    /**
     * Calculates the visibility percentage for the given item.
     *
     * @param itemInfo The item information for the item to calculate the visibility percentage for.
     * @return The visibility percentage for the item.
     */
    private fun LazyListState.visibilityPercent(itemInfo: LazyListItemInfo): Float {
        val start = (layoutInfo.viewportStartOffset - itemInfo.offset).coerceAtLeast(0)
        val end = (itemInfo.offset + itemInfo.size - layoutInfo.viewportEndOffset).coerceAtLeast(0)
        val visibleArea = maxOf(0f, (start + end).toFloat())
        return 1f - (visibleArea / itemInfo.size.toFloat())
    }

    companion object {
        private const val VISIBILITY_THRESHOLD = 0.5F
    }
}