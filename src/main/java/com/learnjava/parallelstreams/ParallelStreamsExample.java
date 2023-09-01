package com.learnjava.parallelstreams;

import com.learnjava.util.DataSet;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.learnjava.util.CommonUtil.*;
import static com.learnjava.util.LoggerUtil.log;

public class ParallelStreamsExample {

    //Parallel streams are designed to solve data parallelism

    //How does it work?
    //
    //1. Split
    //      Data source is split into small data chunks
    //      Example - List collection split into chunks of element of size 1
    //      This is done by spliterator, for ArrayList the spliterator is ArrayListSpliterator
    //
    //2. Execute
    //      Data chunks are applied to the stream pipeline and intermediate operations are executed in common ForkJoin pool
    //
    //3. Combine
    //      Combine the executed results into a final result
    //      Combine phase in streams API maps to terminal operations
    //
    // Parallel streams uses common fork join pool
    // It uses the thread pool of common fork join pool, you can't use a custom thread pool

    public List<String> stringTransformSequentially(List<String> namesList){
        return namesList
                .stream() //sequential
                .map(this::addNameLengthTransform)
//                .parallel() // turns the stream from sequential to parallel
                .collect(Collectors.toList());
    }

    public List<String> stringTransformInParallel(List<String> namesList){
        return namesList
                .parallelStream() //parallel
                .map(this::addNameLengthTransform)
//                .sequential() // turns the stream from parallel to sequential
                .collect(Collectors.toList());
    }

    public List<String> stringTransform(List<String> namesList, boolean isParallel){

        //dynamically determine whether to use sequential or parallel stream
        Stream<String> namesStream = namesList.stream();

        if(isParallel){
            namesStream.parallel();
        }

        return namesStream
                .map(this::addNameLengthTransform)
//                .parallel() // turns the stream from sequential to parallel
                .collect(Collectors.toList());
    }

    private String addNameLengthTransform(String name){
        delay(500);
        return name.length() + " - " + name;

    }

    public static void main(String[] args) {

        List<String> namesList1 = DataSet.namesList();
        ParallelStreamsExample parallelStreamsExample1 = new ParallelStreamsExample();
        startTimer();
        List<String> resultList1 = parallelStreamsExample1.stringTransformSequentially(namesList1);
        log("Sequential stream resultList: "+resultList1);
        timeTaken();

        stopWatchReset();

        List<String> namesList2 = DataSet.namesList();
        ParallelStreamsExample parallelStreamsExample2 = new ParallelStreamsExample();
        startTimer();
        List<String> resultList2 = parallelStreamsExample2.stringTransformInParallel(namesList2);
        log("Parallel stream resultList: "+resultList2);
        timeTaken();


    }
}
