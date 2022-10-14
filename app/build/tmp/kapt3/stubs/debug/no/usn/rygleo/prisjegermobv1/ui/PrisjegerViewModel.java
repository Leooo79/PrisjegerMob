package no.usn.rygleo.prisjegermobv1.ui;

import java.lang.System;

/**
 * Klassen inneholder logikk for app Prisjeger
 * Kommuniserer med screens (visningskomponenter)
 * Kommuniserer med datakilder (klasser/ TODO: API/ local storage)
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u009a\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0010\n\u0002\u0010\u0006\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u00108\u001a\u0002092\u0006\u0010:\u001a\u00020\u001cJ\u000e\u0010;\u001a\u00020<2\u0006\u0010=\u001a\u00020\'J\u000e\u0010>\u001a\u00020<2\u0006\u0010=\u001a\u00020\'J\u0016\u0010?\u001a\u00020<2\u0006\u0010@\u001a\u00020\u00152\u0006\u0010A\u001a\u00020\u0015J\u000e\u0010B\u001a\u0002092\u0006\u0010:\u001a\u00020\u001cJ\u000e\u0010C\u001a\u00020<2\u0006\u0010D\u001a\u00020\rJ\u000e\u0010E\u001a\u00020<2\u0006\u0010:\u001a\u00020\u0011J\u0006\u0010F\u001a\u000209J\u000e\u0010G\u001a\b\u0012\u0004\u0012\u00020\u001c0\fH\u0002J\b\u0010H\u001a\u00020IH\u0002J\u000e\u0010J\u001a\b\u0012\u0004\u0012\u00020\u00110\fH\u0002J\u0010\u0010K\u001a\u0002092\u0006\u0010:\u001a\u00020\u001cH\u0002J\u0010\u0010L\u001a\u0002092\u0006\u0010@\u001a\u00020\u0015H\u0002J\u0010\u0010M\u001a\u0002092\u0006\u0010:\u001a\u00020\u001cH\u0002J\u000e\u0010N\u001a\u0002092\u0006\u0010:\u001a\u00020\u0011J\u001e\u0010O\u001a\u00020<2\u0006\u0010P\u001a\u00020\'2\u0006\u0010A\u001a\u00020\u00152\u0006\u0010@\u001a\u00020\u0015J\u0006\u0010Q\u001a\u000209J\u0016\u0010R\u001a\u0002092\f\u0010S\u001a\b\u0012\u0004\u0012\u00020\u001c0\fH\u0002J\u000e\u0010T\u001a\u0002092\u0006\u0010U\u001a\u00020\u0015J\u0016\u0010V\u001a\u0002092\u0006\u0010W\u001a\u00020I2\u0006\u0010X\u001a\u00020\u0015J\u000e\u0010Y\u001a\u00020Z2\u0006\u0010W\u001a\u00020IJ\u0006\u0010[\u001a\u00020ZR\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\t0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\f0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR6\u0010\u0012\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00110\f0\u000b2\u0012\u0010\u0010\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00110\f0\u000b@BX\u0086\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u000fR\u001a\u0010\u0014\u001a\u00020\u0015X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019R!\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001c0\u001b8FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b\u001f\u0010 \u001a\u0004\b\u001d\u0010\u001eR\u000e\u0010!\u001a\u00020\"X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020$X\u0082\u0004\u00a2\u0006\u0002\n\u0000R%\u0010%\u001a\u0016\u0012\u0004\u0012\u00020\'\u0018\u00010&j\n\u0012\u0004\u0012\u00020\'\u0018\u0001`(\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010*R\u0011\u0010+\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010-R\u0017\u0010.\u001a\b\u0012\u0004\u0012\u00020\u00070/\u00a2\u0006\b\n\u0000\u001a\u0004\b0\u00101R\u0017\u00102\u001a\b\u0012\u0004\u0012\u00020\t0/\u00a2\u0006\b\n\u0000\u001a\u0004\b3\u00101R\u0011\u00104\u001a\u000205\u00a2\u0006\b\n\u0000\u001a\u0004\b6\u00107\u00a8\u0006\\"}, d2 = {"Lno/usn/rygleo/prisjegermobv1/ui/PrisjegerViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lno/usn/rygleo/prisjegermobv1/data/HandlelisteUiState;", "_uiStateNy", "Lno/usn/rygleo/prisjegermobv1/data/VarerUiState;", "allUsers", "Landroidx/lifecycle/LiveData;", "", "Lno/usn/rygleo/prisjegermobv1/roomDB/Bruker;", "getAllUsers", "()Landroidx/lifecycle/LiveData;", "<set-?>", "Lno/usn/rygleo/prisjegermobv1/roomDB/Varer;", "alleVarer", "getAlleVarer", "currentListenavn", "", "getCurrentListenavn", "()Ljava/lang/String;", "setCurrentListenavn", "(Ljava/lang/String;)V", "livedata", "Landroidx/lifecycle/MutableLiveData;", "Lno/usn/rygleo/prisjegermobv1/data/HandlelisteItems;", "getLivedata", "()Landroidx/lifecycle/MutableLiveData;", "livedata$delegate", "Lkotlin/Lazy;", "repoVarer", "Lno/usn/rygleo/prisjegermobv1/data/VarerRepo;", "repository", "Lno/usn/rygleo/prisjegermobv1/data/BrukerRepo;", "sorterteBrukere", "Ljava/util/ArrayList;", "", "Lkotlin/collections/ArrayList;", "getSorterteBrukere", "()Ljava/util/ArrayList;", "testBruker", "getTestBruker", "()Lno/usn/rygleo/prisjegermobv1/roomDB/Bruker;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "uiStateNy", "getUiStateNy", "varerDAO", "Lno/usn/rygleo/prisjegermobv1/roomDB/VarerDAO;", "getVarerDAO", "()Lno/usn/rygleo/prisjegermobv1/roomDB/VarerDAO;", "dekrementer", "", "vare", "getBruker", "Lkotlinx/coroutines/Job;", "brukerId", "getBrukerNavn", "getVare", "listenavn", "varenavn", "inkrementer", "insert", "bruker", "insertVare", "lagTestliste", "manuellHandleliste", "manuellHandlelisteData", "Lno/usn/rygleo/prisjegermobv1/data/HandlelisteData;", "manuellVareliste", "oppdaterAntall", "oppdaterListenavn", "oppdaterSum", "oppdaterSumFraLD", "oppdaterVare", "nyAntall", "selectAllIDs", "setHandleliste", "oppdatertHandleliste", "setListeNavn", "nyttListeNavn", "setNavn", "handlelisteData", "nyttNavn", "totalSum", "", "totalSumLiveData", "app_debug"})
public final class PrisjegerViewModel extends androidx.lifecycle.AndroidViewModel {
    
    /**
     * Testing på LiveData og Room FOR HANDLELISTE
     */
    private final no.usn.rygleo.prisjegermobv1.data.VarerRepo repoVarer = null;
    @org.jetbrains.annotations.NotNull()
    private androidx.lifecycle.LiveData<java.util.List<no.usn.rygleo.prisjegermobv1.roomDB.Varer>> alleVarer;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String currentListenavn = "RoomListe1";
    @org.jetbrains.annotations.NotNull()
    private final no.usn.rygleo.prisjegermobv1.roomDB.VarerDAO varerDAO = null;
    private final kotlinx.coroutines.flow.MutableStateFlow<no.usn.rygleo.prisjegermobv1.data.VarerUiState> _uiStateNy = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<no.usn.rygleo.prisjegermobv1.data.VarerUiState> uiStateNy = null;
    
    /**
     * Testing på LiveData og Room FOR BRUKERE
     */
    @org.jetbrains.annotations.NotNull()
    private final no.usn.rygleo.prisjegermobv1.roomDB.Bruker testBruker = null;
    private final no.usn.rygleo.prisjegermobv1.data.BrukerRepo repository = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.util.List<no.usn.rygleo.prisjegermobv1.roomDB.Bruker>> allUsers = null;
    @org.jetbrains.annotations.Nullable()
    private final java.util.ArrayList<java.lang.Integer> sorterteBrukere = null;
    private final kotlinx.coroutines.flow.MutableStateFlow<no.usn.rygleo.prisjegermobv1.data.HandlelisteUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<no.usn.rygleo.prisjegermobv1.data.HandlelisteUiState> uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy livedata$delegate = null;
    
    public PrisjegerViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application application) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<no.usn.rygleo.prisjegermobv1.roomDB.Varer>> getAlleVarer() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCurrentListenavn() {
        return null;
    }
    
    public final void setCurrentListenavn(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final no.usn.rygleo.prisjegermobv1.roomDB.VarerDAO getVarerDAO() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<no.usn.rygleo.prisjegermobv1.data.VarerUiState> getUiStateNy() {
        return null;
    }
    
    /**
     * Funksjonen kalles fra composables for å oppdatere hvilken handleliste
     * som vises
     */
    public final void setListeNavn(@org.jetbrains.annotations.NotNull()
    java.lang.String nyttListeNavn) {
    }
    
    /**
     * Hjelpemetode for å oppdatere state på listenavn -> rekomposisjon
     */
    private final void oppdaterListenavn(java.lang.String listenavn) {
    }
    
    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job insertVare(@org.jetbrains.annotations.NotNull()
    no.usn.rygleo.prisjegermobv1.roomDB.Varer vare) {
        return null;
    }
    
    /**
     * Lager en testliste og kjører insert mot lokal DB (Room)
     */
    public final void lagTestliste() {
    }
    
    /**
     * Funksjon for å oppdatere en vare (antall)
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job oppdaterVare(int nyAntall, @org.jetbrains.annotations.NotNull()
    java.lang.String varenavn, @org.jetbrains.annotations.NotNull()
    java.lang.String listenavn) {
        return null;
    }
    
    /**
     * returnerer vare pr handliliste pr varenavn
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job getVare(@org.jetbrains.annotations.NotNull()
    java.lang.String listenavn, @org.jetbrains.annotations.NotNull()
    java.lang.String varenavn) {
        return null;
    }
    
    /**
     * Funksjon for å opprette en liste av handlelisteItems
     * Skal erstattes av reelle data fra API
     */
    private final java.util.List<no.usn.rygleo.prisjegermobv1.roomDB.Varer> manuellVareliste() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final no.usn.rygleo.prisjegermobv1.roomDB.Bruker getTestBruker() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<no.usn.rygleo.prisjegermobv1.roomDB.Bruker>> getAllUsers() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.ArrayList<java.lang.Integer> getSorterteBrukere() {
        return null;
    }
    
    public final void selectAllIDs() {
    }
    
    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job insert(@org.jetbrains.annotations.NotNull()
    no.usn.rygleo.prisjegermobv1.roomDB.Bruker bruker) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job getBruker(int brukerId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job getBrukerNavn(int brukerId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<no.usn.rygleo.prisjegermobv1.data.HandlelisteUiState> getUiState() {
        return null;
    }
    
    /**
     * Hjelpemetoder for å opprette testdata
     */
    private final no.usn.rygleo.prisjegermobv1.data.HandlelisteData manuellHandlelisteData() {
        return null;
    }
    
    /**
     * Funksjon for å opprette en liste av handlelisteItems
     * Skal erstattes av reelle data fra API
     */
    private final java.util.List<no.usn.rygleo.prisjegermobv1.data.HandlelisteItems> manuellHandleliste() {
        return null;
    }
    
    /**
     * Hjelpemetode for å regne ut total sum for hele lista
     */
    public final double totalSum(@org.jetbrains.annotations.NotNull()
    no.usn.rygleo.prisjegermobv1.data.HandlelisteData handlelisteData) {
        return 0.0;
    }
    
    /**
     * Hjelpemetode for å regne ut total sum for hele lista
     */
    public final double totalSumLiveData() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<no.usn.rygleo.prisjegermobv1.data.HandlelisteItems> getLivedata() {
        return null;
    }
    
    /**
     * Hjelpemetode for å øke antall
     * TODO: API kall avventer oppkobling mot database/ local storage
     */
    public final void inkrementer(@org.jetbrains.annotations.NotNull()
    no.usn.rygleo.prisjegermobv1.data.HandlelisteItems vare) {
    }
    
    /**
     * Hjelpemetode for å redusere antall
     * TODO: API kall avventer oppkobling mot database/ local storage
     */
    public final void dekrementer(@org.jetbrains.annotations.NotNull()
    no.usn.rygleo.prisjegermobv1.data.HandlelisteItems vare) {
    }
    
    /**
     * Hjelpemetode for å oppdatere totalsum for handleliste
     */
    private final void oppdaterSum(no.usn.rygleo.prisjegermobv1.data.HandlelisteItems vare) {
    }
    
    /**
     * Hjelpemetode for å oppdatere antall pr vare
     */
    private final void oppdaterAntall(no.usn.rygleo.prisjegermobv1.data.HandlelisteItems vare) {
    }
    
    /**
     * Hjelpemetode for å oppdatere totalsum for handleliste
     */
    public final void oppdaterSumFraLD(@org.jetbrains.annotations.NotNull()
    no.usn.rygleo.prisjegermobv1.roomDB.Varer vare) {
    }
    
    public final void setNavn(@org.jetbrains.annotations.NotNull()
    no.usn.rygleo.prisjegermobv1.data.HandlelisteData handlelisteData, @org.jetbrains.annotations.NotNull()
    java.lang.String nyttNavn) {
    }
    
    private final void setHandleliste(java.util.List<no.usn.rygleo.prisjegermobv1.data.HandlelisteItems> oppdatertHandleliste) {
    }
}