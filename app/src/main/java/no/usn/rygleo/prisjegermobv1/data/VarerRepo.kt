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

    suspend fun update(nyAntall: Int, varenavn: String, listenavn: String) {
        varerDAO.update(nyAntall, varenavn, listenavn)
    }


    suspend fun update2(varer: Varer) {
        varerDAO.update2(varer)
    }


    suspend fun delete(varer: Varer) {
        varerDAO.delete(varer)
    }

    suspend fun delete2(varenavn: String, listenavn: String) {
        varerDAO.delete2(varenavn, listenavn)
    }

    suspend fun getAlleVarer() : Flow<List<Varer>> {
        return varerDAO.getAlleVarer()
    }

    suspend fun getAlleVarer2() : Flow<List<Varer>> {
        return varerDAO.getAlleVarer()
    }

    suspend fun getVare(varenavn: String) {
        varerDAO.getVare(varenavn)
    }
}