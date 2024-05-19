package org.example.distancedata.services;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CounterService {

    public CounterService() { }

    private static final AtomicInteger COUNT = new AtomicInteger(0);

    public static synchronized int increment() {
        return COUNT.incrementAndGet();
    }
    public static synchronized int get() {
        return COUNT.get();
    }



}