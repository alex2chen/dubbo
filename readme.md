
## 简介
JMH是一个microbenchmark（微基准测试）框架（2013年首次发布）。与其他众多框架相比它的特色优势在于，它是由Oracle实现JIT的相同人员开发的。JMH可能与最新的Oracle JRE同步，其结果可信度很高。

## 官方介绍
[jmh](http://openjdk.java.net/projects/code-tools/jmh/)

## 如何运行
pom文件中加入依赖
~~~xml
        <dependency>
            <groupId>org.openjdk.jmh</groupId>
            <artifactId>jmh-core</artifactId>
            <version>1.19</version>
        </dependency>
~~~       
如何快速开始呢？在对应的方法上添加注解，并添加main方法，
~~~java
    @Benchmark
    public void xxMethod() {
        // 被测试逻辑
    }
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JMHSample_01_HelloWorld.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }
~~~
## 使用演例
~~~java
@BenchmarkMode(Mode.Throughput)//基准测试类型,Throughput也就是吞吐量,AverageTime计算平均运行时间,SampleTime计算一个方法的运行时间(包括百分位),All所有模式依次运行
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
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(StringBuilder_Test.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }
}
~~~
执行结果：耐心点都可以看懂
~~~html
JMH version: 1.19
VM version: JDK 1.8.0_40, VM 25.40-b25
VM invoker: D:\JavaSoft\Java\jdk1.8.0_40\jre\bin\java.exe
VM options: -Didea.launcher.port=7533 -Didea.launcher.bin.path=D:\JavaSoft\IntelliJ IDEA201634\bin -Dfile.encoding=UTF-8
Warmup: 2 iterations, 1 s each
Measurement: 3 iterations, 4 s each
Timeout: 10 min per iteration
Threads: 8 threads, will synchronize iterations
Benchmark mode: Throughput, ops/time
Benchmark: com.xfboy.StringBuilder_Test.go_StringAdd

Run progress: 0.00% complete, ETA 00:00:56
Fork: 1 of 2
Warmup Iteration   1: 12673.281 ops/ms
Warmup Iteration   2: 14492.435 ops/ms
Iteration   1: 14656.874 ops/ms
Iteration   2: 14757.481 ops/ms
Iteration   3: 14694.051 ops/ms

Run progress: 25.00% complete, ETA 00:00:48
Fork: 2 of 2
Warmup Iteration   1: 13992.636 ops/ms
Warmup Iteration   2: 15128.479 ops/ms
Iteration   1: 15732.080 ops/ms
Iteration   2: 14856.039 ops/ms
Iteration   3: 15147.020 ops/ms

Result "com.xfboy.StringBuilder_Test.go_StringAdd":
  14973.924 ±(99.9%) 1152.351 ops/ms [Average]
  (min, avg, max) = (14656.874, 14973.924, 15732.080), stdev = 410.940
  CI (99.9%): [13821.573, 16126.276] (assumes normal distribution)


JMH version: 1.19
VM version: JDK 1.8.0_40, VM 25.40-b25
VM invoker: D:\JavaSoft\Java\jdk1.8.0_40\jre\bin\java.exe
VM options: -Didea.launcher.port=7533 -Didea.launcher.bin.path=D:\JavaSoft\IntelliJ IDEA201634\bin -Dfile.encoding=UTF-8
Warmup: 2 iterations, 1 s each
Measurement: 3 iterations, 4 s each
Timeout: 10 min per iteration
Threads: 8 threads, will synchronize iterations
Benchmark mode: Throughput, ops/time
Benchmark: com.xfboy.StringBuilder_Test.go_StringBuilderAdd

Run progress: 50.00% complete, ETA 00:00:32
Fork: 1 of 2
Warmup Iteration   1: 66441.052 ops/ms
Warmup Iteration   2: 87524.251 ops/ms
Iteration   1: 64359.966 ops/ms
Iteration   2: 70511.513 ops/ms
Iteration   3: 71212.553 ops/ms

Run progress: 75.00% complete, ETA 00:00:16
Fork: 2 of 2
Warmup Iteration   1: 84992.234 ops/ms
Warmup Iteration   2: 98099.722 ops/ms
Iteration   1: 64196.470 ops/ms
Iteration   2: 73541.511 ops/ms
Iteration   3: 70704.787 ops/ms


Result "com.xfboy.StringBuilder_Test.go_StringBuilderAdd":
  69087.800 ±(99.9%) 10880.046 ops/ms [Average]
  (min, avg, max) = (64196.470, 69087.800, 73541.511), stdev = 3879.929
  CI (99.9%): [58207.754, 79967.846] (assumes normal distribution)

Run complete. Total time: 00:01:04

Benchmark                                Mode  Cnt      Score       Error   Units
StringBuilder_Test.go_StringAdd         thrpt    6  14973.924 ±  1152.351  ops/ms
StringBuilder_Test.go_StringBuilderAdd  thrpt    6  69087.800 ± 10880.046  ops/ms
~~~
