package com.preethi.emailbuddy.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.preethi.emailbuddy.R
import com.preethi.emailbuddy.ui.text.DataModel
import com.preethi.emailbuddy.ui.text.GridUi
import com.preethi.emailbuddy.ui.text.Item

data class MenuItem(
    val routeId: String,
    val titleResId: Int,
    val descriptionResId: Int
)

@Composable
fun MenuScreen(
    onItemClicked: (String) -> Unit = { },
    onTemplateClicked: (String) -> Unit = { }
) {
    val menuItems = listOf(
        MenuItem("" +
                "rawEmailGeneration", R.string.email_summarize_title, R.string.email_summarize_description),
    )
    Column(
        Modifier
            .padding(top = 16.dp, bottom = 16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(all = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(menuItems[0].titleResId),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(menuItems[0].descriptionResId),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
                TextButton(
                    onClick = {
                        onItemClicked(menuItems[0].routeId)
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = stringResource(R.string.action_try))
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            GridUi(dataModel = getDefaultEmailTemplates(), onTemplateClicked)
        }
    }

}

fun getDefaultEmailTemplates() : DataModel {
    val data1 = Item("Day Off Request", "Day off Letter", "Requesting a day off")
    val data2 = Item("Sick Leave Request", "Sick Leave Template", "Requesting a sick leave")
    val data3 = Item("Resignation Letter", "Resignation request letter", "Resignation from the current organization")
    val data4 = Item("Birthday Celebration", "Birthday Celebration", "celebrating a birthday of team mate")
    val dummy_data_models = DataModel(listOf(data3,data1, data2, data4))
    return dummy_data_models
}

@Preview(showSystemUi = true)
@Composable
fun MenuScreenPreview() {
    MenuScreen()
}
