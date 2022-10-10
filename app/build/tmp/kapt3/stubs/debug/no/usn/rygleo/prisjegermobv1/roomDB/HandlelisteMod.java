package no.usn.rygleo.prisjegermobv1.roomDB;

import java.lang.System;

@androidx.room.Entity()
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0011\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0087\b\u0018\u00002\u00020\u0001B?\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\u001a\u0010\u0006\u001a\u0016\u0012\u0004\u0012\u00020\b\u0018\u00010\u0007j\n\u0012\u0004\u0012\u00020\b\u0018\u0001`\t\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b\u00a2\u0006\u0002\u0010\fJ\t\u0010\u0016\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010\u0017\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u001d\u0010\u0018\u001a\u0016\u0012\u0004\u0012\u00020\b\u0018\u00010\u0007j\n\u0012\u0004\u0012\u00020\b\u0018\u0001`\tH\u00c6\u0003J\u0010\u0010\u0019\u001a\u0004\u0018\u00010\u000bH\u00c6\u0003\u00a2\u0006\u0002\u0010\u0012JN\u0010\u001a\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u001c\b\u0002\u0010\u0006\u001a\u0016\u0012\u0004\u0012\u00020\b\u0018\u00010\u0007j\n\u0012\u0004\u0012\u00020\b\u0018\u0001`\t2\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000bH\u00c6\u0001\u00a2\u0006\u0002\u0010\u001bJ\u0013\u0010\u001c\u001a\u00020\u001d2\b\u0010\u001e\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001f\u001a\u00020\u0003H\u00d6\u0001J\t\u0010 \u001a\u00020\u0005H\u00d6\u0001R\u0018\u0010\u0004\u001a\u0004\u0018\u00010\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u001a\u0010\n\u001a\u0004\u0018\u00010\u000b8\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\u0013\u001a\u0004\b\u0011\u0010\u0012R*\u0010\u0006\u001a\u0016\u0012\u0004\u0012\u00020\b\u0018\u00010\u0007j\n\u0012\u0004\u0012\u00020\b\u0018\u0001`\t8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015\u00a8\u0006!"}, d2 = {"Lno/usn/rygleo/prisjegermobv1/roomDB/HandlelisteMod;", "", "handlelisteId", "", "brukerNavn", "", "varer", "Ljava/util/ArrayList;", "Lno/usn/rygleo/prisjegermobv1/data/HandlelisteItems;", "Lkotlin/collections/ArrayList;", "sum", "", "(ILjava/lang/String;Ljava/util/ArrayList;Ljava/lang/Double;)V", "getBrukerNavn", "()Ljava/lang/String;", "getHandlelisteId", "()I", "getSum", "()Ljava/lang/Double;", "Ljava/lang/Double;", "getVarer", "()Ljava/util/ArrayList;", "component1", "component2", "component3", "component4", "copy", "(ILjava/lang/String;Ljava/util/ArrayList;Ljava/lang/Double;)Lno/usn/rygleo/prisjegermobv1/roomDB/HandlelisteMod;", "equals", "", "other", "hashCode", "toString", "app_debug"})
public final class HandlelisteMod {
    @androidx.room.PrimaryKey()
    private final int handlelisteId = 0;
    @org.jetbrains.annotations.Nullable()
    @androidx.room.ColumnInfo(name = "navn")
    private final java.lang.String brukerNavn = null;
    @org.jetbrains.annotations.Nullable()
    @androidx.room.ColumnInfo(name = "varer")
    private final java.util.ArrayList<no.usn.rygleo.prisjegermobv1.data.HandlelisteItems> varer = null;
    @org.jetbrains.annotations.Nullable()
    @androidx.room.ColumnInfo(name = "sum")
    private final java.lang.Double sum = null;
    
    @org.jetbrains.annotations.NotNull()
    public final no.usn.rygleo.prisjegermobv1.roomDB.HandlelisteMod copy(int handlelisteId, @org.jetbrains.annotations.Nullable()
    java.lang.String brukerNavn, @org.jetbrains.annotations.Nullable()
    java.util.ArrayList<no.usn.rygleo.prisjegermobv1.data.HandlelisteItems> varer, @org.jetbrains.annotations.Nullable()
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
    
    public HandlelisteMod(int handlelisteId, @org.jetbrains.annotations.Nullable()
    java.lang.String brukerNavn, @org.jetbrains.annotations.Nullable()
    java.util.ArrayList<no.usn.rygleo.prisjegermobv1.data.HandlelisteItems> varer, @org.jetbrains.annotations.Nullable()
    java.lang.Double sum) {
        super();
    }
    
    public final int component1() {
        return 0;
    }
    
    public final int getHandlelisteId() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getBrukerNavn() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.ArrayList<no.usn.rygleo.prisjegermobv1.data.HandlelisteItems> component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.ArrayList<no.usn.rygleo.prisjegermobv1.data.HandlelisteItems> getVarer() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Double component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Double getSum() {
        return null;
    }
}