package no.usn.rygleo.prisjegermobv1.data

/**
 * Modellklasse for å definere JSON-objektet priserPrVarePrButikk fra tjener
 * Map benyttes for tilgang til JSON nøkler som er varenavn (string med whitespace)
 * Uten hull i nøkler kunne modellen vært generert automatisk ,hvorpå nøkler hadde blitt
 * instansvariabler i modellklassen.
 */
data class PriserPrButikk(
    val butikker: Array<String>,
    val dato: String,
    val varer: Map<String, Array<String>>,
)