package no.usn.rygleo.prisjegermobv1.roomDB;

import java.lang.System;

@androidx.room.Dao()
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\b\u0003\n\u0002\u0010\u0015\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\bg\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\'J\u001c\u0010\u0006\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\b0\u00072\u0006\u0010\t\u001a\u00020\nH\'J\u0010\u0010\u000b\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\nH\'J!\u0010\r\u001a\u00020\u00032\u0012\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00050\u000e\"\u00020\u0005H\'\u00a2\u0006\u0002\u0010\u000fJ\u001c\u0010\u0010\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\b0\u00072\u0006\u0010\u0011\u001a\u00020\u0012H\'J \u0010\u0013\u001a\u00020\u00032\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\f\u001a\u00020\n2\u0006\u0010\t\u001a\u00020\nH\'\u00a8\u0006\u0016"}, d2 = {"Lno/usn/rygleo/prisjegermobv1/roomDB/VarerDAO;", "", "delete", "", "varer", "Lno/usn/rygleo/prisjegermobv1/roomDB/Varer;", "getAlleVarer", "Landroidx/lifecycle/LiveData;", "", "listenavn", "", "getVare", "varenavn", "insertAll", "", "([Lno/usn/rygleo/prisjegermobv1/roomDB/Varer;)V", "listePrId", "alleVarer", "", "update", "nyAntall", "", "app_debug"})
public abstract interface VarerDAO {
    
    @org.jetbrains.annotations.NotNull()
    @androidx.room.Query(value = "SELECT * FROM Varer WHERE listenavn IN (:listenavn)")
    public abstract androidx.lifecycle.LiveData<java.util.List<no.usn.rygleo.prisjegermobv1.roomDB.Varer>> getAlleVarer(@org.jetbrains.annotations.NotNull()
    java.lang.String listenavn);
    
    @org.jetbrains.annotations.NotNull()
    @androidx.room.Query(value = "SELECT varenavn FROM Varer WHERE varenavn IN (:varenavn)")
    public abstract java.lang.String getVare(@org.jetbrains.annotations.NotNull()
    java.lang.String varenavn);
    
    @org.jetbrains.annotations.NotNull()
    @androidx.room.Query(value = "SELECT * FROM Varer WHERE varenavn IN (:alleVarer)")
    public abstract androidx.lifecycle.LiveData<java.util.List<no.usn.rygleo.prisjegermobv1.roomDB.Varer>> listePrId(@org.jetbrains.annotations.NotNull()
    int[] alleVarer);
    
    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    public abstract void insertAll(@org.jetbrains.annotations.NotNull()
    no.usn.rygleo.prisjegermobv1.roomDB.Varer... varer);
    
    @androidx.room.Query(value = "UPDATE varer SET antall=:nyAntall WHERE varenavn = :varenavn AND listenavn = :listenavn")
    public abstract void update(int nyAntall, @org.jetbrains.annotations.NotNull()
    java.lang.String varenavn, @org.jetbrains.annotations.NotNull()
    java.lang.String listenavn);
    
    @androidx.room.Delete()
    public abstract void delete(@org.jetbrains.annotations.NotNull()
    no.usn.rygleo.prisjegermobv1.roomDB.Varer varer);
}