package com.learnjava.completablefuture;

import com.learnjava.service.HelloWorldService;

import java.util.concurrent.CompletableFuture;

import static com.learnjava.util.CommonUtil.*;
import static com.learnjava.util.LoggerUtil.log;

public class CompletableFutureHelloWorldException {

    private HelloWorldService hws;

    public CompletableFutureHelloWorldException(HelloWorldService hws) {
        this.hws = hws;
    }

    public String helloworld_3_async_calls_handle(){

        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(()->hws.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(()->hws.world());
        CompletableFuture<String> hiCompletableFuture = CompletableFuture.supplyAsync(()->{
            delay(1000);
            return " Hi CompletableFuture!";
        });

        String hw = hello
                //handle is used to handle exceptions in CompletableFuture calls, and it returns a value
                //handle will get invoked even if there are no exceptions
                .handle((res, e) -> {
                    if(e!=null){
                        log("Exception is: "+e.getMessage());
                        return "";
                    }else{
                        return res;
                    }
                })
                .thenCombine(world, (h,w) -> h+w)
                .handle((res, e) -> {
                    if(e!=null){
                        log("Exception after world is: "+e.getMessage());
                        return "";
                    } else{
                        return res;
                    }
                })
                .thenCombine(hiCompletableFuture, (previous, current) -> previous+current) //all three completable futures run in parallel
                .thenApply(String::toUpperCase)
                .join();
        timeTaken();

        return hw;

    }

    public String helloworld_3_async_calls_exceptionally(){

        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(()->hws.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(()->hws.world());
        CompletableFuture<String> hiCompletableFuture = CompletableFuture.supplyAsync(()->{
            delay(1000);
            return " Hi CompletableFuture!";
        });

        String hw = hello
                //exceptionally will get invoked only if there are exceptions
                .exceptionally((e) -> {
                        log("Exception is: "+e.getMessage());
                        return "";
                })
                .thenCombine(world, (h,w) -> h+w)
                .exceptionally((e) -> {
                        log("Exception after world is: "+e.getMessage());
                        return "";
                })
                .thenCombine(hiCompletableFuture, (previous, current) -> previous+current) //all three completable futures run in parallel
                .thenApply(String::toUpperCase)
                .join();
        timeTaken();

        return hw;

    }

    public String helloworld_3_async_calls_whenComplete(){

        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(()->hws.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(()->hws.world());
        CompletableFuture<String> hiCompletableFuture = CompletableFuture.supplyAsync(()->{
            delay(1000);
            return " Hi CompletableFuture!";
        });

        String hw = hello
                //whenComplete will get invoked even if there are no exceptions, has no return value hence can't handle the exception
                .whenComplete((res, e) -> {
                    log("res: "+res);
                    if(e!=null) {
                        log("Exception is: " + e.getMessage());
                    }
                })
                .thenCombine(world, (h,w) -> h+w)
                .whenComplete((res, e) -> {
                    log("res: "+res);
                    if(e!=null){
                        log("Exception after world is: "+e.getMessage());
                    }
                })
                .exceptionally((e) -> {
                    log("Exception after world is: "+e.getMessage());
                    return "";
                })
                .thenCombine(hiCompletableFuture, (previous, current) -> previous+current) //all three completable futures run in parallel
                .thenApply(String::toUpperCase)
                .join();
        timeTaken();

        return hw;

    }
}
