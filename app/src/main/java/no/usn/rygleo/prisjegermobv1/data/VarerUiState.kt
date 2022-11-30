package no.usn.rygleo.prisjegermobv1.data

/**
 * Klasse for å håndtere endringer i statevariabler (rekomp)
 */
data class VarerUiState(
    val listenavn: String,
    val sortert: Boolean,
    val butikknavn: String
)
