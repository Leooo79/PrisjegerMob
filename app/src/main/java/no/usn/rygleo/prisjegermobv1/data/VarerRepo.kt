package no.usn.rygleo.prisjegermobv1.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import no.usn.rygleo.prisjegermobv1.RestApi
import no.usn.rygleo.prisjegermobv1.roomDB.Varer
import no.usn.rygleo.prisjegermobv1.roomDB.VarerDAO
import no.usn.rygleo.prisjegermobv1.ui.PrisjegerViewModel
import java.util.concurrent.Callable


class VarerRepo(private val varerDAO: VarerDAO) {

    // sortering på listenavn gjøres i filteret (composable)
    //val alleVarer: Flow<List<Varer>> = varerDAO.getAlleVarer2()
  //  val alleVarerSortert: Flow<List<Varer>> = varerDAO.getAlleVarer()


    suspend fun insert(varer: Varer) {
         varerDAO.insertAll(varer)
    }

 //   suspend fun oppdaterAntall(nyAntall: Int, varenavn: String, listenavn: String) {
 //       varerDAO.oppdaterAntall(nyAntall, varenavn, listenavn)
 //   }


    suspend fun update2(varer: Varer) {
        varerDAO.update(varer)
    }


    suspend fun oppdaterPris(varenavn: String, listenavn: String, enhetspris: Double) {
        varerDAO.oppdaterPris(varenavn, listenavn, enhetspris)
    }


    suspend fun slettVare(varer: Varer) {
        varerDAO.slettVare(varer)
    }

    suspend fun slettHandleliste(listenavn: String) {
        varerDAO.slettHandleliste(listenavn)
    }


    suspend fun getAlleVarer() : Flow<List<Varer>> {
        return varerDAO.getAlleVarer()
    }

    suspend fun getAlleValgteVarer() : Flow<List<Varer>> {
        return varerDAO.getAlleValgteVarer()
    }


}