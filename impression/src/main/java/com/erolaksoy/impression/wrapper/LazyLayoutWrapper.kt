package com.erolaksoy.impression.wrapper

interface LazyLayoutWrapper {

    val visibleItems: List<ImpressionVisibleItemInfo>

    suspend fun visibilityPercent(itemKey: Any): Float
}