package no.usn.rygleo.prisjegermobv1.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import no.usn.rygleo.prisjegermobv1.roomDB.Varer
import no.usn.rygleo.prisjegermobv1.roomDB.VarerDAO
import no.usn.rygleo.prisjegermobv1.ui.PrisjegerViewModel


class VarerRepo(private val varerDAO: VarerDAO) {

    suspend fun insert(varer: Varer) {
        varerDAO.insertAll(varer)
    }

    suspend fun update(nyAntall: Int, varenavn: String, listenavn: String) {
        varerDAO.update(nyAntall, varenavn, listenavn)
    }

    suspend fun getAlleVarer(listenavn: String) {
        varerDAO.getAlleVarer(listenavn)
    }

    suspend fun getVare(varenavn: String) {
        varerDAO.getVare(varenavn)
    }
}