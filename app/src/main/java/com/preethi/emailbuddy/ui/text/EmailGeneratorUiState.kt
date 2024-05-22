package com.preethi.emailbuddy.ui.text

/**
 * A sealed hierarchy describing the state of the text generation.
 */
sealed interface EmailGeneratorUiState {

    /**
     * Empty state when the screen is first shown
     */
    data object Initial: EmailGeneratorUiState

    /**x
     * Still loading
     */
    data object Loading: EmailGeneratorUiState

    /**
     * Text has been generated
     */
    data class Success(
        val outputText: String
    ): EmailGeneratorUiState

    /**
     * There was an error generating text
     */
    data class Error(
        val errorMessage: String
    ): EmailGeneratorUiState
}
