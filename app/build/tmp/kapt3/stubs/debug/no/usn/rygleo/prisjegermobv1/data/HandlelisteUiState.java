package no.usn.rygleo.prisjegermobv1.data;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0014\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001BA\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\u000e\b\u0002\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\f\u00a2\u0006\u0002\u0010\rJ\t\u0010\u0019\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001a\u001a\u00020\u0005H\u00c6\u0003J\u000f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\b0\u0007H\u00c6\u0003J\u000b\u0010\u001c\u001a\u0004\u0018\u00010\nH\u00c6\u0003J\u0010\u0010\u001d\u001a\u0004\u0018\u00010\fH\u00c6\u0003\u00a2\u0006\u0002\u0010\u0017JJ\u0010\u001e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\u000e\b\u0002\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n2\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00c6\u0001\u00a2\u0006\u0002\u0010\u001fJ\u0013\u0010 \u001a\u00020!2\b\u0010\"\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010#\u001a\u00020\u0005H\u00d6\u0001J\t\u0010$\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0017\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0013\u0010\t\u001a\u0004\u0018\u00010\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0015\u0010\u000b\u001a\u0004\u0018\u00010\f\u00a2\u0006\n\n\u0002\u0010\u0018\u001a\u0004\b\u0016\u0010\u0017\u00a8\u0006%"}, d2 = {"Lno/usn/rygleo/prisjegermobv1/data/HandlelisteUiState;", "", "navn", "", "antall", "", "handleliste", "", "Lno/usn/rygleo/prisjegermobv1/data/HandlelisteItems;", "handlelisteData", "Lno/usn/rygleo/prisjegermobv1/data/HandlelisteData;", "sum", "", "(Ljava/lang/String;ILjava/util/List;Lno/usn/rygleo/prisjegermobv1/data/HandlelisteData;Ljava/lang/Double;)V", "getAntall", "()I", "getHandleliste", "()Ljava/util/List;", "getHandlelisteData", "()Lno/usn/rygleo/prisjegermobv1/data/HandlelisteData;", "getNavn", "()Ljava/lang/String;", "getSum", "()Ljava/lang/Double;", "Ljava/lang/Double;", "component1", "component2", "component3", "component4", "component5", "copy", "(Ljava/lang/String;ILjava/util/List;Lno/usn/rygleo/prisjegermobv1/data/HandlelisteData;Ljava/lang/Double;)Lno/usn/rygleo/prisjegermobv1/data/HandlelisteUiState;", "equals", "", "other", "hashCode", "toString", "app_debug"})
public final class HandlelisteUiState {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String navn = null;
    private final int antall = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<no.usn.rygleo.prisjegermobv1.data.HandlelisteItems> handleliste = null;
    @org.jetbrains.annotations.Nullable()
    private final no.usn.rygleo.prisjegermobv1.data.HandlelisteData handlelisteData = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Double sum = null;
    
    @org.jetbrains.annotations.NotNull()
    public final no.usn.rygleo.prisjegermobv1.data.HandlelisteUiState copy(@org.jetbrains.annotations.NotNull()
    java.lang.String navn, int antall, @org.jetbrains.annotations.NotNull()
    java.util.List<no.usn.rygleo.prisjegermobv1.data.HandlelisteItems> handleliste, @org.jetbrains.annotations.Nullable()
    no.usn.rygleo.prisjegermobv1.data.HandlelisteData handlelisteData, @org.jetbrains.annotations.Nullable()
    java.lang.Double sum) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public java.lang.String toString() {
        return null;
    }
    
    public HandlelisteUiState() {
        super();
    }
    
    public HandlelisteUiState(@org.jetbrains.annotations.NotNull()
    java.lang.String navn, int antall, @org.jetbrains.annotations.NotNull()
    java.util.List<no.usn.rygleo.prisjegermobv1.data.HandlelisteItems> handleliste, @org.jetbrains.annotations.Nullable()
    no.usn.rygleo.prisjegermobv1.data.HandlelisteData handlelisteData, @org.jetbrains.annotations.Nullable()
    java.lang.Double sum) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getNavn() {
        return null;
    }
    
    public final int component2() {
        return 0;
    }
    
    public final int getAntall() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<no.usn.rygleo.prisjegermobv1.data.HandlelisteItems> component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<no.usn.rygleo.prisjegermobv1.data.HandlelisteItems> getHandleliste() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final no.usn.rygleo.prisjegermobv1.data.HandlelisteData component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final no.usn.rygleo.prisjegermobv1.data.HandlelisteData getHandlelisteData() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Double component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Double getSum() {
        return null;
    }
}