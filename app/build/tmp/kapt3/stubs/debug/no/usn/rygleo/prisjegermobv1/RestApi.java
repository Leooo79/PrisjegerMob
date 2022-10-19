package no.usn.rygleo.prisjegermobv1;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\bf\u0018\u00002\u00020\u0001J\u0011\u0010\u0002\u001a\u00020\u0003H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0004J\u0011\u0010\u0005\u001a\u00020\u0006H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0004J\u001d\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\b0\bH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0004J\u0011\u0010\n\u001a\u00020\u000bH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0004J\u0017\u0010\f\u001a\b\u0012\u0004\u0012\u00020\r0\bH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0004J\u001b\u0010\u000e\u001a\u00020\u000f2\b\b\u0001\u0010\u0010\u001a\u00020\u0003H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011J\u001e\u0010\u0012\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\r\u0018\u00010\u00132\n\b\u0001\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\'\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0016"}, d2 = {"Lno/usn/rygleo/prisjegermobv1/RestApi;", "", "getAll", "Lno/usn/rygleo/prisjegermobv1/roomDB/Varer;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getButikkliste", "Lno/usn/rygleo/prisjegermobv1/data/Butikk;", "getHandlelister", "", "Lno/usn/rygleo/prisjegermobv1/data/HandlelisteItems;", "getTestAPI", "Lno/usn/rygleo/prisjegermobv1/data/TestAPI;", "getVareliste", "Lno/usn/rygleo/prisjegermobv1/data/VarenavnAPI;", "nyHandlelisteAdd", "", "handleliste", "(Lno/usn/rygleo/prisjegermobv1/roomDB/Varer;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "searchVolumes", "Lretrofit2/Call;", "apiKey", "", "app_debug"})
public abstract interface RestApi {
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.GET(value = "activity")
    public abstract java.lang.Object getTestAPI(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super no.usn.rygleo.prisjegermobv1.data.TestAPI> continuation);
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.GET(value = "historikk")
    public abstract java.lang.Object getAll(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super no.usn.rygleo.prisjegermobv1.roomDB.Varer> continuation);
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.GET(value = "vareliste")
    public abstract java.lang.Object getVareliste(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<no.usn.rygleo.prisjegermobv1.data.VarenavnAPI>> continuation);
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.GET(value = "butikkliste")
    public abstract java.lang.Object getButikkliste(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super no.usn.rygleo.prisjegermobv1.data.Butikk> continuation);
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.GET(value = "handlelister/{epost}")
    public abstract java.lang.Object getHandlelister(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<? extends java.util.List<no.usn.rygleo.prisjegermobv1.data.HandlelisteItems>>> continuation);
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.POST(value = "handlelister/{epost]/{tittel}/add")
    public abstract java.lang.Object nyHandlelisteAdd(@org.jetbrains.annotations.NotNull()
    @retrofit2.http.Body()
    no.usn.rygleo.prisjegermobv1.roomDB.Varer handleliste, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.GET(value = "butikkliste")
    public abstract retrofit2.Call<no.usn.rygleo.prisjegermobv1.data.VarenavnAPI> searchVolumes(@org.jetbrains.annotations.Nullable()
    @retrofit2.http.Query(value = "key")
    java.lang.String apiKey);
}