package no.usn.rygleo.prisjegermobv1.roomDB;

import java.lang.System;

@androidx.room.Dao()
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0003\n\u0002\u0010\u0015\n\u0000\bg\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\'J\u0018\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\bH\'J\u0014\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\f0\u000bH\'J!\u0010\r\u001a\u00020\u00032\u0012\u0010\u000e\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00050\u000f\"\u00020\u0005H\'\u00a2\u0006\u0002\u0010\u0010J\u0016\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00050\f2\u0006\u0010\u0012\u001a\u00020\u0013H\'\u00a8\u0006\u0014"}, d2 = {"Lno/usn/rygleo/prisjegermobv1/roomDB/BrukerDAO;", "", "delete", "", "bruker", "Lno/usn/rygleo/prisjegermobv1/roomDB/Bruker;", "findByBrukerNavnOgPassord", "first", "", "last", "getAlleBrukere", "Landroidx/lifecycle/LiveData;", "", "insertAll", "brukere", "", "([Lno/usn/rygleo/prisjegermobv1/roomDB/Bruker;)V", "listePrId", "alleBrukerId", "", "app_debug"})
public abstract interface BrukerDAO {
    
    @org.jetbrains.annotations.NotNull()
    @androidx.room.Query(value = "SELECT * FROM Bruker")
    public abstract androidx.lifecycle.LiveData<java.util.List<no.usn.rygleo.prisjegermobv1.roomDB.Bruker>> getAlleBrukere();
    
    @org.jetbrains.annotations.NotNull()
    @androidx.room.Query(value = "SELECT * FROM Bruker WHERE brukerId IN (:alleBrukerId)")
    public abstract java.util.List<no.usn.rygleo.prisjegermobv1.roomDB.Bruker> listePrId(@org.jetbrains.annotations.NotNull()
    int[] alleBrukerId);
    
    @org.jetbrains.annotations.NotNull()
    @androidx.room.Query(value = "SELECT * FROM Bruker WHERE brukerNavn LIKE :first AND passord LIKE :last LIMIT 1")
    public abstract no.usn.rygleo.prisjegermobv1.roomDB.Bruker findByBrukerNavnOgPassord(@org.jetbrains.annotations.NotNull()
    java.lang.String first, @org.jetbrains.annotations.NotNull()
    java.lang.String last);
    
    @androidx.room.Insert()
    public abstract void insertAll(@org.jetbrains.annotations.NotNull()
    no.usn.rygleo.prisjegermobv1.roomDB.Bruker... brukere);
    
    @androidx.room.Delete()
    public abstract void delete(@org.jetbrains.annotations.NotNull()
    no.usn.rygleo.prisjegermobv1.roomDB.Bruker bruker);
}