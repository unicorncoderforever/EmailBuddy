package com.preethi.emailbuddy.ui.text

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.preethi.emailbuddy.R
import com.preethi.emailbuddy.ui.GenerativeViewModelFactory
import com.preethi.emailbuddy.ui.theme.EmailBuddyTheme


@Composable
internal fun rawEmailRoute(
    summarizeViewModel: SummarizeViewModel = viewModel(factory = GenerativeViewModelFactory)
) {
    val summarizeUiState by summarizeViewModel.uiState.collectAsState()

    SummarizeScreen(summarizeUiState, onSummarizeClicked = { inputText ->
        summarizeViewModel.summarizeStreaming(inputText)
    } ,showSearchBar =  true,
        defaultText = null)
}

@Composable
internal fun EmailTemplateRoute(
    summarizeViewModel: SummarizeViewModel = viewModel(factory = GenerativeViewModelFactory),
    emailTemplate: String
) {
    val summarizeUiState by summarizeViewModel.uiState.collectAsState()

    SummarizeScreen(summarizeUiState, onSummarizeClicked = { inputText ->
        summarizeViewModel.summarizeStreaming(inputText)
    },
    defaultText = emailTemplate)

}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SummarizeScreen(
    uiState: SummarizeUiState = SummarizeUiState.Loading,
    onSummarizeClicked: (String?) -> Unit = {},
    showSearchBar: Boolean = false,
    defaultText: String? = null,
) {

    val context = LocalContext.current
    val text = defaultText ?: ""
    var textToSummarize by rememberSaveable { mutableStateOf(text) }
    Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        when (uiState) {
                            is SummarizeUiState.Success -> {
                                val intent = Intent(Intent.ACTION_SENDTO)
                                intent.type = "text/plain"
                                intent.data = Uri.parse("mailto:")
                                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("recipient@example.com"))
                                intent.putExtra(Intent.EXTRA_SUBJECT, "test")
                                intent.putExtra(Intent.EXTRA_TEXT, uiState.outputText)
                                val packageManager = context.packageManager
                                val activity = intent.resolveActivity(context.packageManager)
                                startActivity(context, intent, null)
                            }
                            else -> {
                                // Do nothing
                            }
                        }

                    },
                    content = {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = "Add"
                        )
                    }
                )

        }
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            if(showSearchBar) {
                ElevatedCard(
                    modifier = Modifier
                        .padding(all = 16.dp)
                        .fillMaxWidth(),
                    shape = MaterialTheme.shapes.large
                ) {
                    OutlinedTextField(
                        value = textToSummarize,
                        label = { Text(stringResource(R.string.summarize_label)) },
                        placeholder = { Text(stringResource(R.string.summarize_hint)) },
                        onValueChange = { textToSummarize = it },
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    )
                    TextButton(
                        onClick = {
                            if (textToSummarize.isNotBlank()) {
                                onSummarizeClicked(textToSummarize)
                            }
                        },
                        modifier = Modifier
                            .padding(end = 16.dp, bottom = 16.dp)
                            .align(Alignment.End)
                    ) {
                        Text(stringResource(R.string.action_go))
                    }
                }
            }
            when (uiState) {
                SummarizeUiState.Initial -> {
                    onSummarizeClicked(defaultText)
                }

                SummarizeUiState.Loading -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding(all = 8.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is SummarizeUiState.Success -> {
                    Card(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                            .fillMaxWidth(),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(all = 16.dp)
                                .fillMaxWidth()
                        ) {
                            Icon(
                                Icons.Outlined.Person,
                                contentDescription = "Person Icon",
                                tint = MaterialTheme.colorScheme.onSecondary,
                                modifier = Modifier
                                    .requiredSize(36.dp)
                                    .drawBehind {
                                        drawCircle(color = Color.White)
                                    }
                            )
                            Text(
                                text = uiState.outputText, // TODO(thatfiredev): Figure out Markdown support
                                color = MaterialTheme.colorScheme.onSecondary,
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .fillMaxWidth()
                            )
                        }
                    }
                }

                is SummarizeUiState.Error -> {
                    Card(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = uiState.errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(all = 16.dp)
                        )
                    }
                }
            }
        }

    }


}

@Composable
@Preview(showSystemUi = true)
fun SummarizeScreenPreview() {
    EmailBuddyTheme(darkTheme = true) {
        SummarizeScreen()
    }
}
