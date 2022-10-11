package no.usn.rygleo.prisjegermobv1.data

import androidx.lifecycle.LiveData
import no.usn.rygleo.prisjegermobv1.roomDB.BrukerDAO
import no.usn.rygleo.prisjegermobv1.roomDB.Bruker
import no.usn.rygleo.prisjegermobv1.roomDB.Varer
import no.usn.rygleo.prisjegermobv1.roomDB.VarerDAO

class VarerRepo(private val varerDAO: VarerDAO) {
    // Room executes all queries on a separate thread.
// Observed LiveData will notify the observer when the data has changed.
    val alleVarer: LiveData<List<Varer>> = varerDAO.getAlleVarer()
    suspend fun insert(varer: Varer) {
        varerDAO.insertAll(varer)
    }

    suspend fun update(nyAntall: Int, varenavn: String) {
        varerDAO.update(nyAntall, varenavn)
    }

    suspend fun getAlleVarer() {
        varerDAO.getAlleVarer()
    }

    suspend fun getVare(varenavn: String) {
        varerDAO.getVare(varenavn)
    }
}