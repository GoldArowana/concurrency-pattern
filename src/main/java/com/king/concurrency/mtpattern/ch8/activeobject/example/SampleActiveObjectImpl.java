package com.king.concurrency.mtpattern.ch8.activeobject.example;

import com.king.concurrency.util.Debug;

public class SampleActiveObjectImpl {

    public String doProcess(String arg, int i) {
        Debug.info("doProcess start");
        try {
            // 模拟一个比较耗时的操作
            Thread.sleep(50);
        } catch (InterruptedException e) {
            ;
        }
        return arg + "-" + i;
    }

}
