package no.usn.rygleo.prisjegermobv1.data

import com.squareup.moshi.Json

data class VarenavnAPI(
    @field:Json(name = "varenavn") val varenavn: String?,
)