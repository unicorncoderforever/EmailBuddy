package com.preethi.emailbuddy.ui.text

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EmailGeneratorViewModel(
    private val generativeModel: GenerativeModel
) : ViewModel() {

    private val _uiState: MutableStateFlow<EmailGeneratorUiState> =
        MutableStateFlow(EmailGeneratorUiState.Initial)
    val uiState: StateFlow<EmailGeneratorUiState> =
        _uiState.asStateFlow()

    fun summarize(inputText: String) {
        _uiState.value = EmailGeneratorUiState.Loading

        val prompt = "Write an Email regarding: $inputText"

        viewModelScope.launch {
            // Non-streaming
            try {
                val response = generativeModel.generateContent(prompt)
                response.text?.let { outputContent ->
                    _uiState.value = EmailGeneratorUiState.Success(outputContent)
                }
            } catch (e: Exception) {
                _uiState.value = EmailGeneratorUiState.Error(e.localizedMessage ?: "")
            }
        }
    }

    fun generateEmailTemplate(inputText: String?) {
        inputText?.let {
            _uiState.value = EmailGeneratorUiState.Loading

            val prompt = "Write an Email regarding: $inputText"

            viewModelScope.launch {
                try {
                    var outputContent = ""
                    generativeModel.generateContentStream(prompt)
                        .collect { response ->
                            outputContent += response.text
                            _uiState.value = EmailGeneratorUiState.Success(outputContent)
                        }
                } catch (e: Exception) {
                    _uiState.value = EmailGeneratorUiState.Error(e.localizedMessage ?: "")
                }
            }
        }

    }
}
