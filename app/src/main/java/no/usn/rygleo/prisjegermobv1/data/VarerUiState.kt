package no.usn.rygleo.prisjegermobv1.data

import androidx.lifecycle.LiveData
import no.usn.rygleo.prisjegermobv1.roomDB.Bruker
import no.usn.rygleo.prisjegermobv1.roomDB.Varer

data class VarerUiState(
    val listenavn: String = "",
)
