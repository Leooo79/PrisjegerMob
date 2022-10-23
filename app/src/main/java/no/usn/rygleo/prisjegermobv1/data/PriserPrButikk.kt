package no.usn.rygleo.prisjegermobv1.data

/**
 * Modell for å definere priserPrVarePrButikk fra backend API
 * Mulig variabel varer bør være av egen klasse (PrisPrVare)
 */
data class PriserPrButikk(
    val butikker: Array<String>,
    val dato: String,
    val varer: Map<String, Array<String>>
)