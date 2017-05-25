package com.xfboy;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/5/25
 */
@BenchmarkMode(Mode.Throughput)//基准测试类型,Throughput也就是吞吐量
@Warmup(iterations = 2)//预热轮数,保证测试的准确性
@Measurement(iterations = 3, time = 4, timeUnit = TimeUnit.SECONDS)//度量,iterations进行测试的轮次,time每轮进行的时长,timeUnit时长单位
@Threads(8)//测试线程,一般为cpu乘以2
@OutputTimeUnit(TimeUnit.MILLISECONDS)//基准测试结果的时间类型。一般选择秒或者毫秒
public class StringBuilder_Test {
    @Benchmark
    @Fork(2)//分支,为每个试验(迭代集合)fork一个新的java进程
    public void go_StringAdd() {
        String a = "";
        for (int i = 0; i < 10; i++) {
            a += i;
        }
        print(a);
    }

    @Benchmark
    @Fork(2)
    public void go_StringBuilderAdd() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(i);
        }
        print(sb.toString());
    }

    private void print(String a) {
    }
}
