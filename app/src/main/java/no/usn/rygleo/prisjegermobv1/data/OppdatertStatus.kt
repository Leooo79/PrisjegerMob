package no.usn.rygleo.prisjegermobv1.data

/**
 * Modellklasse for å definere objektet som sendes fra tjener når applikasjonen
 * kontrollerer for update
 */
data class OppdatertStatus(
    val handlelisteUtdatert: Boolean,
    val prisUtdatert: Boolean
)