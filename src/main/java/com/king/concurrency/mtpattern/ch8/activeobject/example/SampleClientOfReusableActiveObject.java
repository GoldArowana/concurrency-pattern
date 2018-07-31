package com.king.concurrency.mtpattern.ch8.activeobject.example;

import com.king.concurrency.mtpattern.ch8.activeobject.ActiveObjectProxy;
import com.king.concurrency.util.Debug;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SampleClientOfReusableActiveObject {

    public static void main(String[] args) throws InterruptedException,
            ExecutionException {

        SampleActiveObject sao = ActiveObjectProxy.newInstance(
                SampleActiveObject.class, new SampleActiveObjectImpl(),
                Executors.newCachedThreadPool());
        Future<String> ft = null;

        Debug.info("Before calling active object");
        try {
            ft = sao.process("Something", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //模拟其它操作的时间消耗
        Thread.sleep(40);

        Debug.info(ft.get());
    }
}
