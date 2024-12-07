package com.erolaksoy.impression.wrapper

internal fun calculateVisibilityPercent(
    viewportStartOffset: Int,
    viewportEndOffset: Int,
    itemOffset: Int,
    itemSize: Int,
): Float {
    val start = (viewportStartOffset - itemOffset).coerceAtLeast(0)
    val end = (itemOffset + itemSize - viewportEndOffset).coerceAtLeast(0)
    val visibleArea = maxOf(0f, (start + end).toFloat())
    return 1f - (visibleArea / itemSize.toFloat())
}