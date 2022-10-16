package no.usn.rygleo.prisjegermobv1;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 2, d1 = {"\u0000:\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\u001a\u001c\u0010\u0000\u001a\u00020\u00012\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u0007\u001a4\u0010\u0006\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n2\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00010\rH\u0003\u001a,\u0010\u000e\u001a\u00020\u00012\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u00102\u0006\u0010\u0002\u001a\u00020\u0003H\u0007\u001a\u0016\u0010\u0012\u001a\u00020\u00012\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u0010H\u0007\u001a\u0018\u0010\u0013\u001a\u00020\u00012\u0006\u0010\u0014\u001a\u00020\u000b2\u0006\u0010\u0002\u001a\u00020\u0003H\u0007\u001a\b\u0010\u0015\u001a\u00020\u0001H\u0007\u001a\u0018\u0010\u0016\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0007\u00a8\u0006\u0017"}, d2 = {"HandlelisteScreen", "", "prisjegerViewModel", "Lno/usn/rygleo/prisjegermobv1/ui/PrisjegerViewModel;", "modifier", "Landroidx/compose/ui/Modifier;", "HeaderVisning", "uiStateNy", "Lno/usn/rygleo/prisjegermobv1/data/VarerUiState;", "vareListe", "", "Lno/usn/rygleo/prisjegermobv1/roomDB/Varer;", "iHandleModus", "Lkotlin/Function0;", "ListeVisning", "state", "Landroidx/compose/runtime/MutableState;", "Landroidx/compose/ui/text/input/TextFieldValue;", "Sokefelt", "VarelisteItem", "vare", "VelgButikk", "VelgHandleliste", "app_debug"})
public final class HandlelisteScreenKt {
    
    /**
     * Funksjon for å bygge opp og vise handleliste
     * Benytter en rekke hjelpemetoder
     * Hovekomponenter er header, søkefelt (TF) og listevisning (LazyC)
     */
    @androidx.compose.runtime.Composable()
    public static final void HandlelisteScreen(@org.jetbrains.annotations.NotNull()
    no.usn.rygleo.prisjegermobv1.ui.PrisjegerViewModel prisjegerViewModel, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * Funksjon for å bygge opp og vise header med valg og aggregerte data
     */
    @androidx.compose.runtime.Composable()
    private static final void HeaderVisning(no.usn.rygleo.prisjegermobv1.data.VarerUiState uiStateNy, java.util.List<no.usn.rygleo.prisjegermobv1.roomDB.Varer> vareListe, no.usn.rygleo.prisjegermobv1.ui.PrisjegerViewModel prisjegerViewModel, kotlin.jvm.functions.Function0<kotlin.Unit> iHandleModus) {
    }
    
    /**
     * Enkel nedtrekksmeny for å velge butikk
     * Visning med Toast
     * Events:
     * - Aktivere/ deaktivere meny
     * - Velge butikk -> listen viser priser fra valgt butikk
     * TODO: - Opprette og navngi egne handlelister
     * TODO: - Bytte mellom to ulike visninger: handlemodus / lageHandlelisteModus
     * TODO: - Egen knapp for å legge til flere varer i listen (kanskje ikke nødvendig, må testes)
     *
     * TODO: indirekte kall på API / Datakilde(r) via viewModel for å oppdatere priser
     * TODO: etablere listeinnhold som ressurser (egen tabell?)
     * TODO: bedre tilpasning til bakgrunn, dimensjoner box/ knapp
     */
    @androidx.compose.runtime.Composable()
    public static final void VelgButikk() {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void VelgHandleliste(@org.jetbrains.annotations.NotNull()
    no.usn.rygleo.prisjegermobv1.ui.PrisjegerViewModel prisjegerViewModel, @org.jetbrains.annotations.NotNull()
    no.usn.rygleo.prisjegermobv1.data.VarerUiState uiStateNy) {
    }
    
    /**
     * Søkefelt for å søke på varenavn.
     * Bygger tabell som grunnlag for items i Lazycolumn
     * Skjuler tastatur ved resett og onDone
     * TODO: Burde søkefeltet falt ut av fokus også? Bør teste/ prioritere ulike use case
     */
    @androidx.compose.runtime.Composable()
    @kotlin.OptIn(markerClass = {androidx.compose.ui.ExperimentalComposeUiApi.class})
    public static final void Sokefelt(@org.jetbrains.annotations.NotNull()
    androidx.compose.runtime.MutableState<androidx.compose.ui.text.input.TextFieldValue> state) {
    }
    
    /**
     * Funksjonen bygger opp LazyColumn og viser varer fra filteret (Sokefelt)
     * Dersom filter er deaktivert (tomt, uten tekst) vises hele listen
     */
    @androidx.compose.runtime.Composable()
    public static final void ListeVisning(@org.jetbrains.annotations.NotNull()
    java.util.List<no.usn.rygleo.prisjegermobv1.roomDB.Varer> vareListe, @org.jetbrains.annotations.NotNull()
    androidx.compose.runtime.MutableState<androidx.compose.ui.text.input.TextFieldValue> state, @org.jetbrains.annotations.NotNull()
    no.usn.rygleo.prisjegermobv1.ui.PrisjegerViewModel prisjegerViewModel) {
    }
    
    /**
     * Funksjonen bygger opp og viser handlelister/ varelister
     * Hver rad pakkes i Card for enkel spacing
     * Kolonner består av datafelt fra HandlelisteItems-objekt.
     * Events:
     * - Legge til/ trekke fra antall -> oppdatering av sumPrHandleliste
     * - Vise detaljer om hver vare -> utvider rad og henter inn tekst (bilde?)
     */
    @androidx.compose.runtime.Composable()
    @kotlin.OptIn(markerClass = {androidx.compose.material.ExperimentalMaterialApi.class})
    public static final void VarelisteItem(@org.jetbrains.annotations.NotNull()
    no.usn.rygleo.prisjegermobv1.roomDB.Varer vare, @org.jetbrains.annotations.NotNull()
    no.usn.rygleo.prisjegermobv1.ui.PrisjegerViewModel prisjegerViewModel) {
    }
}