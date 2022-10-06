package no.usn.rygleo.prisjegermobv1.data



data class HandlelisteItems(
    var varenavn : String,
    var enhetspris : Double,
    var antall : Int,
    var sumPrVare : Double = Math.round(enhetspris * antall * 100.00) / 100.0,
)







