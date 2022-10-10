package no.usn.rygleo.prisjegermobv1.ui;

import java.lang.System;

/**
 * Klassen inneholder logikk for app Prisjeger
 * Kommuniserer med screens (visningskomponenter)
 * Kommuniserer med datakilder (klasser/ TODO: API/ local storage)
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000t\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020\u0010J\u0006\u0010%\u001a\u00020&J\u000e\u0010\'\u001a\u00020#2\u0006\u0010$\u001a\u00020\u0010J\u000e\u0010(\u001a\u00020&2\u0006\u0010)\u001a\u00020\u000bJ\u000e\u0010*\u001a\b\u0012\u0004\u0012\u00020\u00100\nH\u0002J\b\u0010+\u001a\u00020,H\u0002J\u0010\u0010-\u001a\u00020#2\u0006\u0010$\u001a\u00020\u0010H\u0002J\u0010\u0010.\u001a\u00020#2\u0006\u0010$\u001a\u00020\u0010H\u0002J\u0016\u0010/\u001a\u00020#2\f\u00100\u001a\b\u0012\u0004\u0012\u00020\u00100\nH\u0002J\u0010\u00101\u001a\u00020#2\u0006\u00102\u001a\u000203H\u0002J\u000e\u00104\u001a\u0002052\u0006\u00106\u001a\u00020,R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\n0\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR!\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f8FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0013\u0010\u0014\u001a\u0004\b\u0011\u0010\u0012R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0017\u001a\u00020\u0018\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u0011\u0010\u001b\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001dR\u0017\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00070\u001f\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010!\u00a8\u00067"}, d2 = {"Lno/usn/rygleo/prisjegermobv1/ui/PrisjegerViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lno/usn/rygleo/prisjegermobv1/data/HandlelisteUiState;", "alleVarer", "Landroidx/lifecycle/LiveData;", "", "Lno/usn/rygleo/prisjegermobv1/roomDB/Varer;", "getAlleVarer", "()Landroidx/lifecycle/LiveData;", "livedata", "Landroidx/lifecycle/MutableLiveData;", "Lno/usn/rygleo/prisjegermobv1/data/HandlelisteItems;", "getLivedata", "()Landroidx/lifecycle/MutableLiveData;", "livedata$delegate", "Lkotlin/Lazy;", "repository", "Lno/usn/rygleo/prisjegermobv1/data/VarerRepo;", "testBruker", "Lno/usn/rygleo/prisjegermobv1/roomDB/Bruker;", "getTestBruker", "()Lno/usn/rygleo/prisjegermobv1/roomDB/Bruker;", "testVare", "getTestVare", "()Lno/usn/rygleo/prisjegermobv1/roomDB/Varer;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "dekrementer", "", "vare", "getVare", "Lkotlinx/coroutines/Job;", "inkrementer", "insert", "varer", "manuellHandleliste", "manuellHandlelisteData", "Lno/usn/rygleo/prisjegermobv1/data/HandlelisteData;", "oppdaterAntall", "oppdaterSum", "setHandleliste", "oppdatertHandleliste", "setNavn", "nyttNavn", "", "totalSum", "", "handlelisteData", "app_debug"})
public final class PrisjegerViewModel extends androidx.lifecycle.AndroidViewModel {
    private final kotlinx.coroutines.flow.MutableStateFlow<no.usn.rygleo.prisjegermobv1.data.HandlelisteUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<no.usn.rygleo.prisjegermobv1.data.HandlelisteUiState> uiState = null;
    
    /**
     * Testing på LiveData og Room
     */
    @org.jetbrains.annotations.NotNull()
    private final no.usn.rygleo.prisjegermobv1.roomDB.Bruker testBruker = null;
    @org.jetbrains.annotations.NotNull()
    private final no.usn.rygleo.prisjegermobv1.roomDB.Varer testVare = null;
    private final no.usn.rygleo.prisjegermobv1.data.VarerRepo repository = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.util.List<no.usn.rygleo.prisjegermobv1.roomDB.Varer>> alleVarer = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy livedata$delegate = null;
    
    public PrisjegerViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application application) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<no.usn.rygleo.prisjegermobv1.data.HandlelisteUiState> getUiState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final no.usn.rygleo.prisjegermobv1.roomDB.Bruker getTestBruker() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final no.usn.rygleo.prisjegermobv1.roomDB.Varer getTestVare() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<no.usn.rygleo.prisjegermobv1.roomDB.Varer>> getAlleVarer() {
        return null;
    }
    
    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job insert(@org.jetbrains.annotations.NotNull()
    no.usn.rygleo.prisjegermobv1.roomDB.Varer varer) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job getVare() {
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
    
    private final void setNavn(java.lang.String nyttNavn) {
    }
    
    private final void setHandleliste(java.util.List<no.usn.rygleo.prisjegermobv1.data.HandlelisteItems> oppdatertHandleliste) {
    }
}