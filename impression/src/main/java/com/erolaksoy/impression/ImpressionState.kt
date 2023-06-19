package com.erolaksoy.impression

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.SharedFlow

@Stable
interface ImpressionState {

    /**
     * A shared flow that emits events when an impression is happened.
     */
    val seenEvent: SharedFlow<Any>

    /**
     * A set of all items that have been recorded as impressions.
     */
    val recordedItems: Set<Any>

    /**
     * Tracks an impression for the given key.
     * @param key The key of the item to track.
     */
    suspend fun onItemPlaced(key: Any)

    /**
     * Adds a validator to the impression state.
     * @param validator The validator to add.
     */
    fun addValidator(validator: ImpressionValidator)

    /**
     * Clears all state from the impression state.
     */
    fun clearAll()
}