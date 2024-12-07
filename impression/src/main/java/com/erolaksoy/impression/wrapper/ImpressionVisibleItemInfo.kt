package com.erolaksoy.impression.wrapper

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.runtime.Stable

@Stable
data class ImpressionVisibleItemInfo(
    val key: Any,
    val offset: Int,
    val size: Int,
)