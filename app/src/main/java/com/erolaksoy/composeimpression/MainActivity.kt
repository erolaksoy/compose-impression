package com.erolaksoy.composeimpression

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.erolaksoy.composeimpression.ui.theme.ComposeImpressionTheme
import com.erolaksoy.impression.ImpressionState
import com.erolaksoy.impression.impression
import com.erolaksoy.impression.rememberImpressionState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeImpressionTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    PageContent(modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}

@Composable
fun PageContent(
    modifier: Modifier = Modifier,
) {
    var selectedTab by remember { mutableStateOf(0) }
    var lastSeenItem by remember { mutableStateOf(-1) }
    val list = remember { (0..100).toList() }
    val lazyListState = rememberLazyListState()
    val impressionState = rememberImpressionState(lazyListState)
    val coroutineScope = rememberCoroutineScope()

    DisposableEffect(Unit) {
        coroutineScope.launch {
            impressionState.seenEvent.collectLatest {
                lastSeenItem = it as Int
            }
        }
        onDispose { impressionState.clearAll() }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { selectedTab }.collectLatest {
            impressionState.clearAll()
            lastSeenItem = -1
        }
    }

    val tabs = remember {
        listOf(
            "Lazy List (Vertical)",
            "Lazy List (Horizontal)",
        )
    }

    Column(modifier) {
        TabRow(
            selectedTabIndex = selectedTab,
            backgroundColor = MaterialTheme.colors.primaryVariant
        ) {
            tabs.fastForEachIndexed { index, item ->
                Tab(
                    modifier = Modifier.padding(16.dp),
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                ) {
                    Text(text = item)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colors.secondary)
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center,
                text = buildAnnotatedString {
                    append("Last Seen Item ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(lastSeenItem.toString())
                    }
                }
            )
        }

        when (selectedTab) {
            0 -> VerticalListSample(
                list = list,
                lazyListState = lazyListState,
                impressionState = impressionState
            )

            1 -> HorizontalListSample(
                list = list,
                lazyListState = lazyListState,
                impressionState = impressionState
            )
        }
    }
}

@Composable
private fun VerticalListSample(
    modifier: Modifier = Modifier,
    list: List<Int>,
    lazyListState: LazyListState,
    impressionState: ImpressionState,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = lazyListState,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(list, key = { it }) {
            CardItem(
                key = it,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .impression(it, impressionState),
            )
        }
    }
}

@Composable
private fun HorizontalListSample(
    modifier: Modifier = Modifier,
    list: List<Int>,
    lazyListState: LazyListState,
    impressionState: ImpressionState,
) {
    LazyRow(
        modifier = modifier.fillMaxSize(),
        state = lazyListState,
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(list, key = { it }) {
            CardItem(
                key = it,
                modifier = Modifier
                    .width(250.dp)
                    .height(120.dp)
                    .impression(it, impressionState),
            )
        }
    }
}

@Composable
private fun CardItem(
    modifier: Modifier = Modifier,
    key: Int,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colors.secondaryVariant)
    ) {
        Text(
            text = "Item $key",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}