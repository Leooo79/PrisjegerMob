package no.usn.rygleo.prisjegermobv1.navigasjon;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0005\u0012\u0013\u0014\u0015\u0016B\u001f\b\u0004\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0007R\u001a\u0010\u0004\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u001a\u0010\u0006\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\r\"\u0004\b\u0011\u0010\u000f\u0082\u0001\u0005\u0017\u0018\u0019\u001a\u001b\u00a8\u0006\u001c"}, d2 = {"Lno/usn/rygleo/prisjegermobv1/navigasjon/BottomNavItem;", "", "title", "", "icon", "", "screen_route", "(Ljava/lang/String;ILjava/lang/String;)V", "getIcon", "()I", "setIcon", "(I)V", "getScreen_route", "()Ljava/lang/String;", "setScreen_route", "(Ljava/lang/String;)V", "getTitle", "setTitle", "Handleliste", "Hjem", "Login", "Notefikasjoner", "Prissammenligning", "Lno/usn/rygleo/prisjegermobv1/navigasjon/BottomNavItem$Hjem;", "Lno/usn/rygleo/prisjegermobv1/navigasjon/BottomNavItem$Handleliste;", "Lno/usn/rygleo/prisjegermobv1/navigasjon/BottomNavItem$Prissammenligning;", "Lno/usn/rygleo/prisjegermobv1/navigasjon/BottomNavItem$Notefikasjoner;", "Lno/usn/rygleo/prisjegermobv1/navigasjon/BottomNavItem$Login;", "app_debug"})
public abstract class BottomNavItem {
    @org.jetbrains.annotations.NotNull()
    private java.lang.String title;
    private int icon;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String screen_route;
    
    private BottomNavItem(java.lang.String title, int icon, java.lang.String screen_route) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTitle() {
        return null;
    }
    
    public final void setTitle(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    public final int getIcon() {
        return 0;
    }
    
    public final void setIcon(int p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getScreen_route() {
        return null;
    }
    
    public final void setScreen_route(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lno/usn/rygleo/prisjegermobv1/navigasjon/BottomNavItem$Hjem;", "Lno/usn/rygleo/prisjegermobv1/navigasjon/BottomNavItem;", "()V", "app_debug"})
    public static final class Hjem extends no.usn.rygleo.prisjegermobv1.navigasjon.BottomNavItem {
        @org.jetbrains.annotations.NotNull()
        public static final no.usn.rygleo.prisjegermobv1.navigasjon.BottomNavItem.Hjem INSTANCE = null;
        
        private Hjem() {
            super(null, 0, null);
        }
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lno/usn/rygleo/prisjegermobv1/navigasjon/BottomNavItem$Handleliste;", "Lno/usn/rygleo/prisjegermobv1/navigasjon/BottomNavItem;", "()V", "app_debug"})
    public static final class Handleliste extends no.usn.rygleo.prisjegermobv1.navigasjon.BottomNavItem {
        @org.jetbrains.annotations.NotNull()
        public static final no.usn.rygleo.prisjegermobv1.navigasjon.BottomNavItem.Handleliste INSTANCE = null;
        
        private Handleliste() {
            super(null, 0, null);
        }
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lno/usn/rygleo/prisjegermobv1/navigasjon/BottomNavItem$Prissammenligning;", "Lno/usn/rygleo/prisjegermobv1/navigasjon/BottomNavItem;", "()V", "app_debug"})
    public static final class Prissammenligning extends no.usn.rygleo.prisjegermobv1.navigasjon.BottomNavItem {
        @org.jetbrains.annotations.NotNull()
        public static final no.usn.rygleo.prisjegermobv1.navigasjon.BottomNavItem.Prissammenligning INSTANCE = null;
        
        private Prissammenligning() {
            super(null, 0, null);
        }
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lno/usn/rygleo/prisjegermobv1/navigasjon/BottomNavItem$Notefikasjoner;", "Lno/usn/rygleo/prisjegermobv1/navigasjon/BottomNavItem;", "()V", "app_debug"})
    public static final class Notefikasjoner extends no.usn.rygleo.prisjegermobv1.navigasjon.BottomNavItem {
        @org.jetbrains.annotations.NotNull()
        public static final no.usn.rygleo.prisjegermobv1.navigasjon.BottomNavItem.Notefikasjoner INSTANCE = null;
        
        private Notefikasjoner() {
            super(null, 0, null);
        }
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lno/usn/rygleo/prisjegermobv1/navigasjon/BottomNavItem$Login;", "Lno/usn/rygleo/prisjegermobv1/navigasjon/BottomNavItem;", "()V", "app_debug"})
    public static final class Login extends no.usn.rygleo.prisjegermobv1.navigasjon.BottomNavItem {
        @org.jetbrains.annotations.NotNull()
        public static final no.usn.rygleo.prisjegermobv1.navigasjon.BottomNavItem.Login INSTANCE = null;
        
        private Login() {
            super(null, 0, null);
        }
    }
}