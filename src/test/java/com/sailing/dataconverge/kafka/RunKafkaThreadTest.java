package com.sailing.dataconverge.kafka;

import com.sailing.dataconverge.config.TopicConsumeConfig;
import com.sailing.dataconverge.config.TopicProduceConfig;
import com.sailing.dataconverge.data.KafkaConsumeQueueData;
import com.sailing.dataconverge.data.KafkaProduceQueueData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RunKafkaThreadTest {

    @Autowired
    private TopicConsumeConfig topicConsumeConfig;

    @Autowired
    private TopicProduceConfig topicProduceConfig;

    @Test
    public void initTest() {
        KafkaProduceQueueData.init(Arrays.asList(topicProduceConfig.getTopics().split(",")));
        KafkaConsumeQueueData.init(Arrays.asList(topicConsumeConfig.getTopics().split(",")));
        RunKafkaThread.startKafkaThread();
    }
//    java.security.auth.login.config

}