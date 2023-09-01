package com.learnjava.parallelstreams;

import com.learnjava.util.DataSet;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.learnjava.util.CommonUtil.*;

public class ListSpliteratorExample {

    public List<Integer> multiplyEachValue1(ArrayList<Integer> integers, int multiplyValue, boolean isParallel){

        startTimer();
        Stream<Integer> integerStream = integers.stream();

        if(isParallel){
            integerStream.parallel();
        }

        List<Integer> resultList = integerStream
                .map(integer -> integer*multiplyValue)
                .collect(Collectors.toList());

        timeTaken();
        return resultList;

    }

    public List<Integer> multiplyEachValue2(LinkedList<Integer> integers, int multiplyValue, boolean isParallel){

        startTimer();
        Stream<Integer> integerStream = integers.stream();

        if(isParallel){
            integerStream.parallel();
        }

        List<Integer> resultList = integerStream
                .map(integer -> integer*multiplyValue)
                .collect(Collectors.toList());

        timeTaken();
        return resultList;

    }

    public static void main(String[] args) {

        ArrayList<Integer> integers1 = DataSet.generateArrayList(2000000);
        System.out.println("ArrayList sequential stream test:");
        ListSpliteratorExample listSpliteratorExample = new ListSpliteratorExample();
        listSpliteratorExample.multiplyEachValue1(integers1,2, false);

        stopWatchReset();

        System.out.println("ArrayList parallel stream test:");
        listSpliteratorExample.multiplyEachValue1(integers1,2, true);

        stopWatchReset();

        //Sequential stream is preferred for LinkedList
        LinkedList<Integer> integers2 = DataSet.generateIntegerLinkedList(2000000);
        System.out.println("LinkedList sequential stream test:");
        listSpliteratorExample.multiplyEachValue2(integers2,2, false);

        stopWatchReset();

        //Parallel stream is not recommended for LinkedList
        //Linkedlist is a collection which is difficult to split into individual chunks, hence parallel stream takes too long
        System.out.println("LinkedList parallel stream test:");
        listSpliteratorExample.multiplyEachValue2(integers2,2, true);

    }
}
