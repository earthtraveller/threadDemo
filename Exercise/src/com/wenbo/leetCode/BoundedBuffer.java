package com.wenbo.leetCode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BoundedBuffer {
    public static AtomicInteger atomicInteger = new AtomicInteger();
    public volatile boolean flag = true;
    public static final int MAX_COUNT = 10;
    public static final List<Integer> pool = new ArrayList<>();

    public void produce() {
        // 判断，干活，通知
        while (flag) {
            // 每隔 1000 毫秒生产一个商品
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            synchronized (pool) {
                //池子满了，生产者停止生产
                //埋个坑，这里用的if
                //TODO 判断
                while (pool.size() == MAX_COUNT) {
                    try {
                        System.out.println("pool is full, wating...");
                        pool.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //干活
                pool.add(atomicInteger.incrementAndGet());
                System.out.println("produce number:" + atomicInteger.get() + "\t" + "current size:" + pool.size());
                //通知
                pool.notifyAll();
            }
        }
    }

    public void consumue() {
        // 判断，干活，通知
        while (flag) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            synchronized (pool) {
                //池子满了，生产者停止生产
                //埋个坑，这里用的if
                //TODO 判断
                while (pool.size() == 0) {
                    try {
                        System.out.println("pool is empty, wating...");
                        pool.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //干活
                int temp = pool.get(0);
                pool.remove(0);
                System.out.println("cousume number:" + temp + "\t" + "current size:" + pool.size());
                //通知
                pool.notifyAll();
            }
        }
    }

    public void stop() {
        flag = false;
    }
}
