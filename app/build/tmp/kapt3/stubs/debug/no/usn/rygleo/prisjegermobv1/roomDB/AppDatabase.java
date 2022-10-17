package no.usn.rygleo.prisjegermobv1.roomDB;

import java.lang.System;

@androidx.room.Database(entities = {no.usn.rygleo.prisjegermobv1.roomDB.Bruker.class, no.usn.rygleo.prisjegermobv1.roomDB.Varer.class}, version = 8, exportSchema = false)
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\'\u0018\u0000 \u00072\u00020\u0001:\u0001\u0007B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&\u00a8\u0006\b"}, d2 = {"Lno/usn/rygleo/prisjegermobv1/roomDB/AppDatabase;", "Landroidx/room/RoomDatabase;", "()V", "brukerDAO", "Lno/usn/rygleo/prisjegermobv1/roomDB/BrukerDAO;", "varerDAO", "Lno/usn/rygleo/prisjegermobv1/roomDB/VarerDAO;", "Companion", "app_debug"})
public abstract class AppDatabase extends androidx.room.RoomDatabase {
    @org.jetbrains.annotations.NotNull()
    public static final no.usn.rygleo.prisjegermobv1.roomDB.AppDatabase.Companion Companion = null;
    @kotlin.jvm.Volatile()
    private static volatile no.usn.rygleo.prisjegermobv1.roomDB.AppDatabase instans;
    
    public AppDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract no.usn.rygleo.prisjegermobv1.roomDB.BrukerDAO brukerDAO();
    
    @org.jetbrains.annotations.NotNull()
    public abstract no.usn.rygleo.prisjegermobv1.roomDB.VarerDAO varerDAO();
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lno/usn/rygleo/prisjegermobv1/roomDB/AppDatabase$Companion;", "", "()V", "instans", "Lno/usn/rygleo/prisjegermobv1/roomDB/AppDatabase;", "getRoomDb", "context", "Landroid/content/Context;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final no.usn.rygleo.prisjegermobv1.roomDB.AppDatabase getRoomDb(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
    }
}