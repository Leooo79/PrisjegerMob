package no.usn.rygleo.prisjegermobv1.data

/**
 * Modell for å definere priserPrVarePrButikk fra backend API
 * Mulig variabel varer bør være av egen klasse (PrisPrVare)
 */
data class PriserPrButikk(
    val butikker: Array<String>,
    val dato: String,
    val varer: Map<String, Array<String>>,
) {
    fun getPriserPrButikk(butikknavn: String) : Map<String, String> {
        var indeks = 0
        var prisliste = emptyMap<String, String>()
   //     val prisliste2 = mapOf("epost" to epost, "passord" to passord)
        for (bNavn in butikker) {
            if (bNavn == butikknavn) {
                indeks = butikker.indexOf(bNavn)
            } else {
                throw Exception("Ugyldig butikknavn")
            }
        }
        for (varene in varer) {
            prisliste = mapOf("varenavn" to varene.key, "pris" to varene.value[indeks])
        }
        return prisliste
    }
}