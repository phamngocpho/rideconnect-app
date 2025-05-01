package com.rideconnect.presentation.components.location

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rideconnect.data.remote.dto.response.location.Prediction

@Composable
fun LocationResultsList(
    title: String,
    predictions: List<Prediction>,
    onItemClick: (Prediction) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(8.dp)
            )

            LazyColumn(
                modifier = Modifier.heightIn(max = 300.dp)
            ) {
                items(predictions) { prediction ->
                    ListItem(
                        headlineContent = {
                            Text(
                                prediction.structured_formatting.main_text,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        supportingContent = {
                            Text(
                                prediction.structured_formatting.secondary_text,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        leadingContent = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                        modifier = Modifier.clickable { onItemClick(prediction) }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}
