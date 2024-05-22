package com.preethi.emailbuddy.ui.text

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GridUi(dataModel: DataModel, onTemplateClicked: (String) -> Unit = { }) {
    // Create a grid layout with 2 columns
    LazyVerticalGrid(columns =  GridCells.Fixed(2) ) {
        // Iterate over the data model and create a block for each item
        items(dataModel.items) { block ->
            // Create a block with the item's data
            Block(block, onTemplateClicked = onTemplateClicked)
        }
    }
}

@Composable
fun Block(item: Item, onTemplateClicked: (String) -> Unit) {
    // Create a container for the block
    Surface(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        onClick = {
            onTemplateClicked(item.description)
        }
    ) {
        // Display the item's data
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = item.title, style = MaterialTheme.typography.titleMedium)
            Text(text = item.description, style = MaterialTheme.typography.bodySmall)
        }
    }
}

// Define the data model
data class DataModel(val items: List<Item>)
data class Item(val title: String, val description: String, val emailContext: String)