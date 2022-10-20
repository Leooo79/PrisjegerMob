package no.usn.rygleo.prisjegermobv1;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\bf\u0018\u00002\u00020\u0001J\u0011\u0010\u0002\u001a\u00020\u0003H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0004J\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0004J\u001d\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u00070\u0007H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0004J\u0011\u0010\u000b\u001a\u00020\fH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0004J\u0011\u0010\r\u001a\u00020\fH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0004J\u0017\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0007H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0004J\u001b\u0010\u0010\u001a\u00020\u00112\b\b\u0001\u0010\u0012\u001a\u00020\u0003H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0013J\u001e\u0010\u0014\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0016\u0018\u00010\u00152\n\b\u0001\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\'\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0019"}, d2 = {"Lno/usn/rygleo/prisjegermobv1/RestApi;", "", "getAll", "Lno/usn/rygleo/prisjegermobv1/roomDB/Varer;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getButikkliste", "Lretrofit2/Response;", "", "Lno/usn/rygleo/prisjegermobv1/data/Butikk;", "getHandlelister", "Lno/usn/rygleo/prisjegermobv1/data/HandlelisteItems;", "getTestAPI", "Lno/usn/rygleo/prisjegermobv1/data/TestAPI;", "getTore", "getVareliste", "Ljava/lang/Object;", "nyHandlelisteAdd", "", "handleliste", "(Lno/usn/rygleo/prisjegermobv1/roomDB/Varer;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "searchVolumes", "Lretrofit2/Call;", "Lno/usn/rygleo/prisjegermobv1/data/VarenavnAPI;", "apiKey", "", "app_debug"})
public abstract interface RestApi {
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.GET(value = "activity")
    public abstract java.lang.Object getTestAPI(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super no.usn.rygleo.prisjegermobv1.data.TestAPI> continuation);
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.GET(value = "Tore1")
    public abstract java.lang.Object getTore(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super no.usn.rygleo.prisjegermobv1.data.TestAPI> continuation);
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.GET(value = "historikk")
    public abstract java.lang.Object getAll(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super no.usn.rygleo.prisjegermobv1.roomDB.Varer> continuation);
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.GET(value = "vareliste")
    public abstract java.lang.Object getVareliste(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<? extends java.lang.Object>> continuation);
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.GET(value = "butikkliste")
    public abstract java.lang.Object getButikkliste(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<java.util.List<no.usn.rygleo.prisjegermobv1.data.Butikk>>> continuation);
    
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