package no.usn.rygleo.prisjegermobv1.roomDB;

import java.lang.System;

@androidx.room.Entity(primaryKeys = {"listenavn", "varenavn"})
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\b\n\u0002\b\u0011\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0087\b\u0018\u00002\u00020\u0001B)\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u0012\b\u0010\u0007\u001a\u0004\u0018\u00010\b\u00a2\u0006\u0002\u0010\tJ\t\u0010\u0013\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0014\u001a\u00020\u0003H\u00c6\u0003J\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0006H\u00c6\u0003\u00a2\u0006\u0002\u0010\u000eJ\u0010\u0010\u0016\u001a\u0004\u0018\u00010\bH\u00c6\u0003\u00a2\u0006\u0002\u0010\u000bJ:\u0010\u0017\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00062\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\bH\u00c6\u0001\u00a2\u0006\u0002\u0010\u0018J\u0013\u0010\u0019\u001a\u00020\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001c\u001a\u00020\bH\u00d6\u0001J\t\u0010\u001d\u001a\u00020\u0003H\u00d6\u0001R\u001a\u0010\u0007\u001a\u0004\u0018\u00010\b8\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\f\u001a\u0004\b\n\u0010\u000bR\u001a\u0010\u0005\u001a\u0004\u0018\u00010\u00068\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\u000f\u001a\u0004\b\r\u0010\u000eR\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0016\u0010\u0004\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0011\u00a8\u0006\u001e"}, d2 = {"Lno/usn/rygleo/prisjegermobv1/roomDB/Varer;", "", "listenavn", "", "varenavn", "enhetspris", "", "antall", "", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Double;Ljava/lang/Integer;)V", "getAntall", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getEnhetspris", "()Ljava/lang/Double;", "Ljava/lang/Double;", "getListenavn", "()Ljava/lang/String;", "getVarenavn", "component1", "component2", "component3", "component4", "copy", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Double;Ljava/lang/Integer;)Lno/usn/rygleo/prisjegermobv1/roomDB/Varer;", "equals", "", "other", "hashCode", "toString", "app_debug"})
public final class Varer {
    @org.jetbrains.annotations.NotNull()
    @androidx.room.ColumnInfo(name = "listenavn")
    private final java.lang.String listenavn = null;
    @org.jetbrains.annotations.NotNull()
    @androidx.room.ColumnInfo(name = "varenavn")
    private final java.lang.String varenavn = null;
    @org.jetbrains.annotations.Nullable()
    @androidx.room.ColumnInfo(name = "enhetspris")
    private final java.lang.Double enhetspris = null;
    @org.jetbrains.annotations.Nullable()
    @androidx.room.ColumnInfo(name = "antall")
    private final java.lang.Integer antall = null;
    
    @org.jetbrains.annotations.NotNull()
    public final no.usn.rygleo.prisjegermobv1.roomDB.Varer copy(@org.jetbrains.annotations.NotNull()
    java.lang.String listenavn, @org.jetbrains.annotations.NotNull()
    java.lang.String varenavn, @org.jetbrains.annotations.Nullable()
    java.lang.Double enhetspris, @org.jetbrains.annotations.Nullable()
    java.lang.Integer antall) {
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
    
    public Varer(@org.jetbrains.annotations.NotNull()
    java.lang.String listenavn, @org.jetbrains.annotations.NotNull()
    java.lang.String varenavn, @org.jetbrains.annotations.Nullable()
    java.lang.Double enhetspris, @org.jetbrains.annotations.Nullable()
    java.lang.Integer antall) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getListenavn() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getVarenavn() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Double component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Double getEnhetspris() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getAntall() {
        return null;
    }
}