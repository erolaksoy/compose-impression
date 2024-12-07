package com.erolaksoy.impression

import androidx.annotation.FloatRange
import com.erolaksoy.impression.wrapper.LazyLayoutWrapper

class VisibilityPercentImpressionValidator(
    @FloatRange(0.0, 1.0) private val visibilityPercentThreshold: Float = VISIBILITY_THRESHOLD,
) : ImpressionValidator {

    override suspend fun isValid(state: LazyLayoutWrapper, key: Any): Boolean {
        val item = state.visibleItems.firstOrNull { it.key == key } ?: return false
        val itemVisibilityPercent = state.visibilityPercent(item.key)
        return itemVisibilityPercent >= visibilityPercentThreshold
    }

    companion object {
        private const val VISIBILITY_THRESHOLD = 0.5F
    }
}