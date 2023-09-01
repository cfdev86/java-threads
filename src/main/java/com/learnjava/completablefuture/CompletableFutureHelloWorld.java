package com.learnjava.completablefuture;

import com.learnjava.service.HelloWorldService;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.learnjava.util.CommonUtil.*;
import static com.learnjava.util.LoggerUtil.log;

public class CompletableFutureHelloWorld {

    private HelloWorldService hws;

    public CompletableFutureHelloWorld(HelloWorldService hws) {
        this.hws = hws;
    }

    public CompletableFuture<String> helloWorld(){
        return CompletableFuture.supplyAsync(hws::helloWorld)
                .thenApply(String::toUpperCase);
    }

    public String helloworld_multiple_async_calls(){

        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(()->hws.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(()->hws.world());

        //thenCombine can be used to combine two completable futures
        String helloWorld = hello.thenCombine(world, (h,w) -> h+w) //both async calls run in parallel
                .thenApply(String::toUpperCase)
                .join();
        timeTaken();

        return helloWorld;

    }

    public String helloworld_3_async_calls(){

        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(()->hws.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(()->hws.world());
        CompletableFuture<String> hiCompletableFuture = CompletableFuture.supplyAsync(()->{
            delay(1000);
            return " Hi CompletableFuture!";
        });

        String helloWorld = hello.thenCombine(world, (h,w) -> h+w)
                .thenCombine(hiCompletableFuture, (previous, current) -> previous+current) //all three completable futures run in parallel
                .thenApply(String::toUpperCase)
                .join();
        timeTaken();

        return helloWorld;

    }

    public String helloworld_3_async_calls_log(){

        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(()->hws.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(()->hws.world());
        CompletableFuture<String> hiCompletableFuture = CompletableFuture.supplyAsync(()->{
            delay(1000);
            return " Hi CompletableFuture!";
        });

        String helloWorld = hello.thenCombine(world, (h,w) -> {
                log("thenCombine h/w");
                return h+w;
                })
                .thenCombine(hiCompletableFuture, (previous, current) -> {
                    log("thenCombine previous/current");
                    return previous+current;
                }) //all three completable futures run in parallel
                .thenApply(s -> {
                    log("thenApply");
                    return s.toLowerCase();
                })
                .join();
        timeTaken();

        return helloWorld;

    }

    public String helloworld_3_async_calls_logs_async(){

        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(()->hws.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(()->hws.world());
        CompletableFuture<String> hiCompletableFuture = CompletableFuture.supplyAsync(()->{
            delay(1000);
            return " Hi CompletableFuture!";
        });

        String helloWorld = hello.thenCombineAsync(world, (h,w) -> {
                    log("thenCombine h/w");
                    return h+w;
                })
                .thenCombineAsync(hiCompletableFuture, (previous, current) -> {
                    log("thenCombine previous/current");
                    return previous+current;
                }) //all three completable futures run in parallel
                .thenApplyAsync(s -> {
                    log("thenApply");
                    return s.toLowerCase();
                })
                .join();
        timeTaken();

        return helloWorld;

    }

    public String helloworld_3_async_calls_custom_threadpool(){

        startTimer();

        //Create a custom thread pool
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        CompletableFuture<String> hello = CompletableFuture.supplyAsync(()->hws.hello(), executorService); //use the custom thread pool
        CompletableFuture<String> world = CompletableFuture.supplyAsync(()->hws.world(), executorService);
        CompletableFuture<String> hiCompletableFuture = CompletableFuture.supplyAsync(()->{
            delay(1000);
            return " Hi CompletableFuture!";
        }, executorService);

        String helloWorld = hello.thenCombine(world, (h,w) -> {
                    log("thenCombine h/w");
                    return h+w;
                })
                .thenCombine(hiCompletableFuture, (previous, current) -> {
                    log("thenCombine previous/current");
                    return previous+current;
                }) //all three completable futures run in parallel
                .thenApply(s -> {
                    log("thenApply");
                    return s.toLowerCase();
                })
                .join();
        timeTaken();

        return helloWorld;

    }

    public String helloworld_3_async_calls_custom_threadpool_async(){

        startTimer();

        //Create a custom thread pool
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        CompletableFuture<String> hello = CompletableFuture.supplyAsync(()->hws.hello(), executorService); //use the custom thread pool
        CompletableFuture<String> world = CompletableFuture.supplyAsync(()->hws.world(), executorService);
        CompletableFuture<String> hiCompletableFuture = CompletableFuture.supplyAsync(()->{
            delay(1000);
            return " Hi CompletableFuture!";
        }, executorService);

        String helloWorld = hello.thenCombineAsync(world, (h,w) -> {
                    log("thenCombine h/w");
                    return h+w;
                }, executorService)
                .thenCombineAsync(hiCompletableFuture, (previous, current) -> {
                    log("thenCombine previous/current");
                    return previous+current;
                }, executorService) //all three completable futures run in parallel
                .thenApplyAsync(s -> {
                    log("thenApply");
                    return s.toLowerCase();
                }, executorService)
                .join();
        timeTaken();

        return helloWorld;

    }

    public CompletableFuture<String> helloWorld_thenCompose(){

        //thenCompose accepts a function that returns a CompletableFuture
        return CompletableFuture.supplyAsync(hws::hello)
                .thenCompose((previous) -> hws.worldFuture(previous)); //this a dependant/subsequent step, runs after previous Async completes and not in parallel
                //.thenApply(String::toUpperCase)
                //.join();

    }

    public static void main(String[] args) {

        HelloWorldService hws = new HelloWorldService();

        //Creates a completable future which runs in a background thread and the main thread executes the code in the next line
        CompletableFuture.supplyAsync(hws::helloWorld)
                .thenApply(String::toUpperCase) //is called when hws.helloWorld() method call completes
                .thenAccept(result->{ //the result of thenApply is passed to thenAccept
                    log("Result is: "+result);
                }).join(); //join call is going to block the main thread

        System.out.println("Done!");

        // Required to ensure the thenAccept method is executed (if join is not called)
        // and logged before the main/caller thread completes executing the remaining code in the main method

        //  delay(2000);
     }

     public void allOf(){

        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> hws.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> hws.world());

        //will wait for both the CompletableFutures to complete
        CompletableFuture.allOf(hello,world).join();
        timeTaken();

     }

     public String anyOf(){

        //db
         CompletableFuture<String> db = CompletableFuture.supplyAsync(() -> {
             delay(1000);
             log("response from db");
             return "Hello World!";
         });

         //rest
         CompletableFuture<String> rest = CompletableFuture.supplyAsync(() -> {
             delay(2000);
             log("response from rest");
             return "Hello World!";
         });

         //soap
         CompletableFuture<String> soap = CompletableFuture.supplyAsync(() -> {
             delay(3000);
             log("response from soap");
             return "Hello World!";
         });

         List<CompletableFuture<String>> completableFutureList = List.of(db, rest, soap);

         //CompletableFuture<Object> completableFuture = CompletableFuture.anyOf(completableFutureList.toArray(new CompletableFuture[completableFutureList.size()]));

         CompletableFuture<Object> completableFuture = CompletableFuture.anyOf(db,rest,soap);

         //anyOf waits for any of the provided CompletableFutures to complete and then proceeds with the next step - thenApply in this case
         //used in cases where you are trying to retrieve the same data from different source of different method and want to wait for only one to complete
         String result = (String) completableFuture.thenApply(v -> {
                                     if( v instanceof String){
                                        return v;
                                     }else{
                                        return null;
                                     }
                                }).join();

         return result;

     }
}
