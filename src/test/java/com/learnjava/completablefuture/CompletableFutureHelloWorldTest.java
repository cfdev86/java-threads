package com.learnjava.completablefuture;

import com.learnjava.service.HelloWorldService;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static com.learnjava.util.CommonUtil.startTimer;
import static com.learnjava.util.CommonUtil.timeTaken;
import static org.junit.jupiter.api.Assertions.*;

class CompletableFutureHelloWorldTest {

    HelloWorldService hws = new HelloWorldService();
    CompletableFutureHelloWorld cfhw = new CompletableFutureHelloWorld(hws);

    @Test
    void helloWorld() {

        CompletableFuture<String> completableFuture = cfhw.helloWorld();

        completableFuture.thenAccept(s -> assertEquals("HELLO WORLD", s)).join();

    }

    @Test
    void helloworld_multiple_async_calls(){

        String helloWorld = cfhw.helloworld_multiple_async_calls();
        assertEquals("HELLO WORLD!", helloWorld);
    }

    @Test
    void helloworld_3_async_calls(){
        String helloWorldCompletableFuture = cfhw.helloworld_3_async_calls();
        assertEquals("HELLO WORLD! HI COMPLETABLEFUTURE!", helloWorldCompletableFuture);
    }

    @Test
    void helloworld_3_async_calls_logs_async(){
        String helloWorldCompletableFuture = cfhw.helloworld_3_async_calls_logs_async();
        assertEquals("hello world! hi completablefuture!", helloWorldCompletableFuture);
    }

    @Test
    void helloWorld_thenCompose(){

        CompletableFuture<String> completableFuture = cfhw.helloWorld_thenCompose();

        startTimer();
        completableFuture.thenAccept(s -> assertEquals("hello world!", s)).join();
        timeTaken();

    }

    @Test
    void helloworld_3_async_calls_custom_threadpool() {

        String helloWorld = cfhw.helloworld_3_async_calls_custom_threadpool();

        assertEquals("hello world! hi completablefuture!", helloWorld);

    }

    @Test
    void helloworld_3_async_calls_custom_threadpool_async() {

        String helloWorld = cfhw.helloworld_3_async_calls_custom_threadpool_async();

        assertEquals("hello world! hi completablefuture!", helloWorld);

    }

    @Test
    void anyOf() {

        String helloWorld = cfhw.anyOf();

        assertEquals("Hello World!", helloWorld);

    }

    @Test
    void allOf(){
        cfhw.allOf();

    }
}