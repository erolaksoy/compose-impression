package com.erolaksoy.impression

import com.erolaksoy.impression.wrapper.LazyLayoutWrapper

/**
 * This interface provides a method for determining whether an impression is valid.
 */
interface ImpressionValidator {

    /**
     * Determines whether the given impression is valid.
     *
     * @param state The state of the lazy list.
     * @param key The key of the item to validate.
     * @return Whether the impression is valid.
     */
    suspend fun isValid(state: LazyLayoutWrapper, key: Any): Boolean
}