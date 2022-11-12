package no.usn.rygleo.prisjegermobv1.data

import androidx.lifecycle.LiveData
import no.usn.rygleo.prisjegermobv1.roomDB.BrukerDAO
import no.usn.rygleo.prisjegermobv1.roomDB.Bruker

class BrukerRepo(private val brukerDAO: BrukerDAO) {
    // Room executes all queries on a separate thread.
// Observed LiveData will notify the observer when the data has changed.
    val allUsers: LiveData<List<Bruker>> = brukerDAO.getAlleBrukere()
    suspend fun insert(bruker: Bruker) {
        brukerDAO.insert(bruker)
    }

    /*
    suspend fun getBruker(brukerId: Int)  {
         brukerDAO.getBruker(brukerId)
    }

    suspend fun getBrukerNavn(brukerId: Int)  {
        brukerDAO.getBruker(brukerId)
    }

     */
}