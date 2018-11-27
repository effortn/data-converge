package com.sailing.dataconverge.kafka.component;

import com.sailing.dataconverge.kafka.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Slf4j
public final class KafkaProperties
{
    
    private static Properties serverProps = new Properties();
    private static Properties producerProps = new Properties();
    
    private static Properties consumerProps = new Properties();
    
    private static Properties clientProps = new Properties();
    
    private static KafkaProperties instance = null;
    
    private KafkaProperties()
    {
        String filePath = ConfigUtils.getBigDataConfigDir();
        
        try
        {
            File proFile = new File(filePath + "producer.properties");
        
            if (proFile.exists())
            {
                producerProps.load(new FileInputStream(filePath + "producer.properties"));
            }
        
            File conFile = new File(filePath + "producer.properties");
        
            if (conFile.exists())
            {
                consumerProps.load(new FileInputStream(filePath + "consumer.properties"));
            }
        
            File serFile = new File(filePath + "server.properties");
            
            if (serFile.exists())
            {
            	serverProps.load(new FileInputStream(filePath + "server.properties"));
            }

            File cliFile = new File(filePath + "client.properties");
            
            if (cliFile.exists())
            {
            	clientProps.load(new FileInputStream(filePath + "client.properties"));
            }
        }
        catch (IOException e)
        {
            log.info("The Exception occured.", e);
        }
    }
    
    public synchronized static KafkaProperties getInstance()
    {
        if (null == instance)
        {
            instance = new KafkaProperties();
        }
        
        return instance;
    }
    
    /**
    * 获取参数值
    * @param key properites的key值
    * @param defValue 默认值
    * @return
    */
    public String getValues(String key, String defValue)
    {
        String rtValue = null;
        
        if (null == key)
        {
            log.error("key is null");
        }
        else
        {
            rtValue = getPropertiesValue(key);
        }
        
        if (null == rtValue)
        {
            log.warn("KafkaProperties.getValues return null, key is " + key);
            rtValue = defValue;
        }
        
        log.info("KafkaProperties.getValues: key is " + key + "; Value is " + rtValue);
        
        return rtValue;
    }
    
    /**
    * 根据key值获取server.properties的值
    * @param key
    * @return
    */
    private String getPropertiesValue(String key)
    {
        String rtValue = serverProps.getProperty(key);
        
        // server.properties中没有，则再向producer.properties中获取
        if (null == rtValue)
        {
            rtValue = producerProps.getProperty(key);
        }
        
        // producer中没有，则再向consumer.properties中获取
        if (null == rtValue)
        {
            rtValue = consumerProps.getProperty(key);
        }
        
        // consumer没有，则再向client.properties中获取
        if (null == rtValue)
        {
        	rtValue = clientProps.getProperty(key);
        }
        
        return rtValue;
    }
}
