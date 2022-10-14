package no.usn.rygleo.prisjegermobv1.data;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0019\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000fJ\u0019\u0010\u0010\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000fJ\u0019\u0010\u0011\u001a\u00020\f2\u0006\u0010\u0012\u001a\u00020\bH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0013R\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0014"}, d2 = {"Lno/usn/rygleo/prisjegermobv1/data/BrukerRepo;", "", "brukerDAO", "Lno/usn/rygleo/prisjegermobv1/roomDB/BrukerDAO;", "(Lno/usn/rygleo/prisjegermobv1/roomDB/BrukerDAO;)V", "allUsers", "Landroidx/lifecycle/LiveData;", "", "Lno/usn/rygleo/prisjegermobv1/roomDB/Bruker;", "getAllUsers", "()Landroidx/lifecycle/LiveData;", "getBruker", "", "brukerId", "", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getBrukerNavn", "insert", "bruker", "(Lno/usn/rygleo/prisjegermobv1/roomDB/Bruker;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class BrukerRepo {
    private final no.usn.rygleo.prisjegermobv1.roomDB.BrukerDAO brukerDAO = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.util.List<no.usn.rygleo.prisjegermobv1.roomDB.Bruker>> allUsers = null;
    
    public BrukerRepo(@org.jetbrains.annotations.NotNull()
    no.usn.rygleo.prisjegermobv1.roomDB.BrukerDAO brukerDAO) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<no.usn.rygleo.prisjegermobv1.roomDB.Bruker>> getAllUsers() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object insert(@org.jetbrains.annotations.NotNull()
    no.usn.rygleo.prisjegermobv1.roomDB.Bruker bruker, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getBruker(int brukerId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getBrukerNavn(int brukerId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
}