package com.erolaksoy.composeimpression

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.erolaksoy.composeimpression.ui.theme.ComposeImpressionTheme
import com.erolaksoy.impression.VisibilityPercentImpressionValidator
import com.erolaksoy.impression.impression
import com.erolaksoy.impression.rememberImpressionState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeImpressionTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = rememberLazyListState()
                    ) {
                        item {
                            HorizontalList(
                                onImpressionHappened = {
                                    Log.d("impression1", "$it item impression happened")
                                }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        item {
                            HorizontalList(
                                onImpressionHappened = {
                                    Log.d("impression2", "$it item impression happened")
                                }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun HorizontalList(
    modifier: Modifier = Modifier,
    onImpressionHappened: (Int) -> Unit,
) {
    val list = remember { (0..100).toList() }
    val lazyListState = rememberLazyListState()

    val impressionState = rememberImpressionState(
        lazyListState = lazyListState
    ) {
        addValidator(VisibilityPercentImpressionValidator(0.5f))
    }

    LazyRow(
        state = lazyListState,
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(list, key = { it }) {
            Card(
                backgroundColor = MaterialTheme.colors.secondaryVariant,
                modifier = Modifier
                    .width(200.dp)
                    .height(100.dp)
                    .impression(
                        key = it,
                        impressionState = impressionState,
                        onImpressionHappened = onImpressionHappened,
                    )
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    text = "Item $it",
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}

@Composable
fun VerticalList(
    modifier: Modifier = Modifier,
    onImpressionHappened: (Int) -> Unit,
) {
    val list = remember { (0..100).toList() }
    val lazyListState = rememberLazyListState()
    val impressionState = rememberImpressionState(
        lazyListState = lazyListState
    ) {
        addValidator(VisibilityPercentImpressionValidator(0.5f))
    }
    LazyColumn(
        state = lazyListState,
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(list, key = { it }) {
            Card(
                backgroundColor = MaterialTheme.colors.secondaryVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .impression(
                        key = it,
                        impressionState = impressionState,
                        onImpressionHappened = onImpressionHappened,
                    )
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    text = "Item $it",
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}