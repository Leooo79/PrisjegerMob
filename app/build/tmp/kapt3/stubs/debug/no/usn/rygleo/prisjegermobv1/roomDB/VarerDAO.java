package no.usn.rygleo.prisjegermobv1.roomDB;

import java.lang.System;

@androidx.room.Dao()
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\b\u0003\n\u0002\u0010\u0015\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\bg\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\'J\u0018\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\bH\'J\u001c\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\f0\u000b2\u0006\u0010\t\u001a\u00020\bH\'J\u0014\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\f0\u000bH\'J\u0010\u0010\u000e\u001a\u00020\b2\u0006\u0010\u0007\u001a\u00020\bH\'J!\u0010\u000f\u001a\u00020\u00032\u0012\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00050\u0010\"\u00020\u0005H\'\u00a2\u0006\u0002\u0010\u0011J\u0016\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00050\f2\u0006\u0010\u0013\u001a\u00020\u0014H\'J \u0010\u0015\u001a\u00020\u00032\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\bH\'J\u0010\u0010\u0018\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\'\u00a8\u0006\u0019"}, d2 = {"Lno/usn/rygleo/prisjegermobv1/roomDB/VarerDAO;", "", "delete", "", "varer", "Lno/usn/rygleo/prisjegermobv1/roomDB/Varer;", "delete2", "varenavn", "", "listenavn", "getAlleVarer", "Lkotlinx/coroutines/flow/Flow;", "", "getAlleVarer2", "getVare", "insertAll", "", "([Lno/usn/rygleo/prisjegermobv1/roomDB/Varer;)V", "listePrId", "alleVarer", "", "update", "nyAntall", "", "update2", "app_debug"})
public abstract interface VarerDAO {
    
    @org.jetbrains.annotations.NotNull()
    @androidx.room.Query(value = "SELECT * FROM Varer WHERE listenavn IN (:listenavn)")
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<no.usn.rygleo.prisjegermobv1.roomDB.Varer>> getAlleVarer(@org.jetbrains.annotations.NotNull()
    java.lang.String listenavn);
    
    @org.jetbrains.annotations.NotNull()
    @androidx.room.Query(value = "SELECT * FROM Varer")
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<no.usn.rygleo.prisjegermobv1.roomDB.Varer>> getAlleVarer2();
    
    @org.jetbrains.annotations.NotNull()
    @androidx.room.Query(value = "SELECT varenavn FROM Varer WHERE varenavn IN (:varenavn)")
    public abstract java.lang.String getVare(@org.jetbrains.annotations.NotNull()
    java.lang.String varenavn);
    
    @org.jetbrains.annotations.NotNull()
    @androidx.room.Query(value = "SELECT * FROM Varer WHERE varenavn IN (:alleVarer)")
    public abstract java.util.List<no.usn.rygleo.prisjegermobv1.roomDB.Varer> listePrId(@org.jetbrains.annotations.NotNull()
    int[] alleVarer);
    
    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    public abstract void insertAll(@org.jetbrains.annotations.NotNull()
    no.usn.rygleo.prisjegermobv1.roomDB.Varer... varer);
    
    @androidx.room.Query(value = "UPDATE varer SET antall=:nyAntall WHERE varenavn = :varenavn AND listenavn = :listenavn")
    public abstract void update(int nyAntall, @org.jetbrains.annotations.NotNull()
    java.lang.String varenavn, @org.jetbrains.annotations.NotNull()
    java.lang.String listenavn);
    
    @androidx.room.Update()
    public abstract void update2(@org.jetbrains.annotations.NotNull()
    no.usn.rygleo.prisjegermobv1.roomDB.Varer varer);
    
    @androidx.room.Delete()
    public abstract void delete(@org.jetbrains.annotations.NotNull()
    no.usn.rygleo.prisjegermobv1.roomDB.Varer varer);
    
    @androidx.room.Query(value = "DELETE FROM varer WHERE varenavn = :varenavn AND listenavn = :listenavn")
    public abstract void delete2(@org.jetbrains.annotations.NotNull()
    java.lang.String varenavn, @org.jetbrains.annotations.NotNull()
    java.lang.String listenavn);
}