package com.tuhuynh.httpserver.core.nio;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadFactory;

import lombok.val;

public class EventLoopThreadFactory implements ThreadFactory {
    private int counter;
    private String name;
    private List<String> stats;

    public EventLoopThreadFactory(String name) {
        counter = 1;
        this.name = name;
        stats = new ArrayList<>();
    }

    @Override
    public Thread newThread(Runnable runnable) {
        val t = new Thread(runnable, name + "-thread-" + counter);
        counter++;
        stats.add(String.format("Created thread %d with name %s on %s \n", t.getId(), t.getName(),
                                System.currentTimeMillis() / 1000L));
        return t;
    }

    public String getStats() {
        val buffer = new StringBuilder();
        for (String stat : stats) {
            buffer.append(stat);
        }
        return buffer.toString();
    }
}