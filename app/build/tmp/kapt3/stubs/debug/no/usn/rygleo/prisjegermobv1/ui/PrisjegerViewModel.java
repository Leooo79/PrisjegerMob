package no.usn.rygleo.prisjegermobv1.ui;

import java.lang.System;

/**
 * Klassen inneholder logikk for app Prisjeger
 * Kommuniserer med screens (visningskomponenter)
 * Kommuniserer med datakilder (klasser/ TODO: API/ local storage)
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\bJ\u000e\u0010\u0014\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\bJ\u000e\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\b0\u0016H\u0002J\b\u0010\u0017\u001a\u00020\u0018H\u0002J\u0010\u0010\u0019\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\bH\u0002J\u0010\u0010\u001a\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\bH\u0002J\u0016\u0010\u001b\u001a\u00020\u00122\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\b0\u0016H\u0002J\u0010\u0010\u001d\u001a\u00020\u00122\u0006\u0010\u001e\u001a\u00020\u001fH\u0002J\u000e\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020\u0018R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R!\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00078FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b\u000b\u0010\f\u001a\u0004\b\t\u0010\nR\u0017\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00050\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010\u00a8\u0006#"}, d2 = {"Lno/usn/rygleo/prisjegermobv1/ui/PrisjegerViewModel;", "Landroidx/lifecycle/ViewModel;", "()V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lno/usn/rygleo/prisjegermobv1/data/HandlelisteUiState;", "livedata", "Landroidx/lifecycle/MutableLiveData;", "Lno/usn/rygleo/prisjegermobv1/data/HandlelisteItems;", "getLivedata", "()Landroidx/lifecycle/MutableLiveData;", "livedata$delegate", "Lkotlin/Lazy;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "dekrementer", "", "vare", "inkrementer", "manuellHandleliste", "", "manuellHandlelisteData", "Lno/usn/rygleo/prisjegermobv1/data/HandlelisteData;", "oppdaterAntall", "oppdaterSum", "setHandleliste", "oppdatertHandleliste", "setNavn", "nyttNavn", "", "totalSum", "", "handlelisteData", "app_debug"})
public final class PrisjegerViewModel extends androidx.lifecycle.ViewModel {
    private final kotlinx.coroutines.flow.MutableStateFlow<no.usn.rygleo.prisjegermobv1.data.HandlelisteUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<no.usn.rygleo.prisjegermobv1.data.HandlelisteUiState> uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy livedata$delegate = null;
    
    public PrisjegerViewModel() {
        super();
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