package com.erolaksoy.impression

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.onGloballyPositioned
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Stable
fun <T : Any> Modifier.impression(
    key: T,
    impressionState: ImpressionState,
    onImpressionHappened: (T) -> Unit,
) = composed {

    val coroutineScope = rememberCoroutineScope()

    DisposableEffect(key) {
        val job = coroutineScope.launch {
            impressionState.seenEvent.collect {
                if (key == it) {
                    onImpressionHappened(key)
                }
            }
        }
        onDispose { job.cancel() }
    }

    onGloballyPositioned {
        coroutineScope.launch {
            if (it.isAttached.not()) return@launch
            impressionState.onItemPlaced(key = key)
        }
    }
}

@Stable
fun <T : Any> Modifier.impression(
    key: T,
    impressionState: ImpressionState,
) = composed {

    val coroutineScope = rememberCoroutineScope()

    onGloballyPositioned {
        coroutineScope.launch {
            if (it.isAttached.not()) return@launch
            impressionState.onItemPlaced(key = key)
        }
    }
}

@Composable
fun rememberImpressionState(
    lazyListState: LazyListState,
    block: ImpressionState.() -> Unit = defaultImpressionStateBlock,
): ImpressionState {
    return remember {
        ImpressionStateImpl(lazyListState).apply(block)
    }
}

internal val defaultImpressionStateBlock: ImpressionState.() -> Unit = {
    addValidator(VisibilityPercentImpressionValidator())
}

class ImpressionStateImpl(private val state: LazyListState) : ImpressionState {
    private val _seenEvent = MutableSharedFlow<Any>(extraBufferCapacity = 1)
    override val seenEvent: SharedFlow<Any> = _seenEvent.asSharedFlow()

    private val _recordedItems = mutableSetOf<Any>()
    override val recordedItems: Set<Any> = _recordedItems.toSet()

    private val _validators = mutableSetOf<ImpressionValidator>()

    override suspend fun onItemPlaced(key: Any) = withContext(Dispatchers.Default) {
        if (_recordedItems.contains(key)) return@withContext

        val visibleItemsInfo = state.layoutInfo.visibleItemsInfo

        val filteredList = if (_validators.isEmpty()) {
            visibleItemsInfo
        } else {
            visibleItemsInfo.filter { _validators.any { it.isValid(state, key) } }
        }

        val isItemInList = filteredList.any { it.key == key }

        if (isItemInList) {
            _recordedItems.add(key)
            _seenEvent.emit(key)
        }
    }

    override fun addValidator(validator: ImpressionValidator) {
        _validators.add(validator)
    }

    override fun clearValidators() {
        _validators.clear()
    }

    override fun clearAll() {
        _recordedItems.clear()
    }
}
