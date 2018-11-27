package com.sailing.dataconverge;

import com.sailing.dataconverge.config.ConvergeConstent;
import com.sailing.dataconverge.data.KafkaProduceQueueData;
import com.sailing.dataconverge.kafka.ProduceThread;
import com.sailing.dataconverge.kafka.RunKafkaThread;
import com.sailing.dataconverge.thread.SbpzSendThread;
import com.sailing.dataconverge.thread.SbztSendThread;
import com.sailing.dataconverge.thread.WfsjResendThread;
import com.sailing.dataconverge.thread.WfsjSendThread;
import com.sailing.dataconverge.utils.FtpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableScheduling
@Slf4j
public class DataConvergeApplication {

    // 违法数据线程执行的线程池
    public static final ExecutorService wfsjSendExecutor =
            new ThreadPoolExecutor(3, 5, 3, TimeUnit.HOURS, new LinkedBlockingQueue<Runnable>());

    // 违法数据补传线程执行的线程池
    private static final ExecutorService wfsjResendExecutor =
            new ThreadPoolExecutor(1, 1, 3, TimeUnit.HOURS, new LinkedBlockingQueue<Runnable>());

    // 设备运维状态信息推送线程执行的线程池
    private static final ExecutorService deviceStatusExecutor =
            new ThreadPoolExecutor(1, 1, 3, TimeUnit.HOURS, new LinkedBlockingQueue<Runnable>());

    // 设备配置信息推送线程执行的线程池
    private static final ExecutorService deviceConfigExecutor =
            new ThreadPoolExecutor(1, 1, 3, TimeUnit.HOURS, new LinkedBlockingQueue<Runnable>());

    public static void main(String[] args) {
        SpringApplication.run(DataConvergeApplication.class, args);
        ConvergeConstent.initConstent();
        FtpUtils.init();
        RunKafkaThread.startKafkaThread();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ThreadInit();
    }

    /**
     * 线程启动
     */
    public static void ThreadInit() {
        log.info("【Main】线程初始化开始！");
        deviceConfigExecutor.execute(new SbpzSendThread());
        deviceStatusExecutor.execute(new SbztSendThread());
        wfsjResendExecutor.execute(new WfsjResendThread());
        wfsjSendExecutor.execute(new WfsjSendThread(System.currentTimeMillis(), -1, null, -1));
        log.info("【Main】线程初始化完成！");
    }

    /**
     * 监控线程是否停止
     *  程序初始化 10 分钟后开始每 10 分钟对线程池进行检测和维护
     */
    @Scheduled(initialDelay = 15 * 60 * 1000, fixedRate = 10 * 60 * 1000)
    public void ThreadWatched() {
        log.info("【Main】线程检测开始！");
        int n = 0;
        if (deviceConfigExecutor.isShutdown()) {
            log.info("【Main】设备配置信息推送线程停止！开始重启！");
            deviceConfigExecutor.execute(new SbpzSendThread());
            n++;
        }
        if (deviceStatusExecutor.isShutdown()) {
            log.info("【Main】设备运维状态信息推送线程停止！开始重启！");
            deviceStatusExecutor.execute(new SbztSendThread());
            n++;
        }
        if (wfsjResendExecutor.isShutdown()) {
            log.info("【Main】违法数据补传线程停止！开始重启！");
            wfsjResendExecutor.execute(new WfsjResendThread());
            n++;
        }
        if (wfsjSendExecutor.isShutdown()) {
            log.info("【Main】违法数据推送线程停止！开始重启！");
            wfsjSendExecutor.execute(new WfsjSendThread(System.currentTimeMillis()));
            n++;
        }
        log.info("【Main】线程检测完毕！共有{}个线程池停止并重启！", n);
    }
}