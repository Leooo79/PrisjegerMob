package no.usn.rygleo.prisjegermobv1.data

data class TestAPI(
    val accessibility: Double,
    val activity: String,
    val key: String,
    val link: String,
    val participants: Int,
    val price: Double,  // var Int, må rettes til Double
    val type: String
)