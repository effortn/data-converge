package com.sailing.dataconverge.kafka.component;

import com.sailing.dataconverge.kafka.comm.Constants;
import com.sailing.dataconverge.kafka.comm.KafkaConsts;
import com.sailing.dataconverge.utils.CommUtils;
import com.sailing.dataconverge.kafka.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.KafkaException;

import java.io.IOException;
import java.util.*;

@Slf4j
public class KafkaUtils  {

	private final static byte[] lock = new byte[0];

	private static Map<String, Consumer<String, byte[]>> consumer_map = new HashMap<>();
	public static Consumer<String, byte[]> getNewConsumer(String topicName) {
		securityPrepare();
		
		List<String> topicNames = KafkaUtils.parseTopicName(topicName);
		Consumer<String, byte[]> consumer = KafkaUtils.createConsumer(topicNames);
		consumer.subscribe(topicNames); // subscribe
		
		return consumer;
	}

	public static Map<String, Consumer<String, byte[]>> initConsumerMap(String topicNames) {
		addShutDownHook();

		securityPrepare();
		List<String> topicNameList = KafkaUtils.parseTopicName(topicNames);
		for (int i = 0; i < topicNameList.size(); i++) {
			Consumer<String, byte[]> consumer = KafkaUtils.createConsumer(Arrays.asList(topicNameList.get(i)));
			consumer.subscribe(Arrays.asList(topicNameList.get(i)));
			consumer_map.put(topicNameList.get(i), consumer);
		}
		return consumer_map;

	}

	public static Consumer<String, byte[]> getConsumer(String topicName) {
		Consumer<String, byte[]> _consumer_ = consumer_map.get(topicName);
		if (_consumer_ == null) {
			synchronized (lock) {
				if(_consumer_ == null) {
					addShutDownHook();
					
					securityPrepare();
					
					List<String> topicNames = KafkaUtils.parseTopicName(topicName);
					Consumer<String, byte[]> consumer = KafkaUtils.createConsumer(topicNames);
					consumer.subscribe(topicNames); // subscribe
					
					//注：subscribe和assign函数是不可以同时使用的，因为subscribe是自动分配，并有rebalance功能，而assign则是将partition固定给consumer，因此二者不能同时使用
//					List<TopicPartition> topicPartitions = Arrays.asList(
//							new TopicPartition(topicName, 0), 
//							new TopicPartition(topicName, 1), 
//							new TopicPartition(topicName, 2)
//						);
//					consumer.seekToEnd(topicPartitions);
//					consumer.assign(topicPartitions);
					//consumer.seek(topicPartition, 100);
					
					_consumer_ = consumer;
					consumer_map.put(topicName, consumer);
				}
			}
		}
		return _consumer_;
	}
	private static Consumer<String, byte[]> createConsumer(List<String> topicNames) {
		KafkaProperties kafkaProc = KafkaProperties.getInstance();
		
		Properties props = new Properties();
		
		// Broker连接地址
		String key = "bootstrap.servers";
		props.put(key, kafkaProc.getValues(key, "127.0.0.1:9092"));
		 // Group id
		key = "group.id";
		props.put(key, kafkaProc.getValues(key, "0"));
		
		
		key = "max.poll.records";
		props.put(key, kafkaProc.getValues(key, "500"));
		// 是否自动提交offset
		key = "enable.auto.commit";
		props.put(key, kafkaProc.getValues(key, "true"));
		// 自动提交offset的时间间隔
		key = "auto.commit.interval.ms";
		props.put(key, kafkaProc.getValues(key, "1000"));
		 // 会话超时时间
		key = "session.timeout.ms";
		props.put(key, kafkaProc.getValues(key, "30000"));
		
		key = "key.deserializer";
		props.put(key, kafkaProc.getValues(key, "org.apache.kafka.common.serialization.StringDeserializer"));
		key = "value.deserializer";
		props.put(key, kafkaProc.getValues(key, "org.apache.kafka.common.serialization.ByteArrayDeserializer"));
		
		//配置安全认证相关参数
	    if( ConfigUtils.enableSecurity() ) {
		     // 安全协议类型
			key = "security.protocol";
			props.put(key, kafkaProc.getValues(key, "SASL_PLAINTEXT"));
			 // 服务名
			key = "sasl.kerberos.service.name";
			props.put(key, kafkaProc.getValues(key, "kafka"));
			// 域名
			key = "kerberos.domain.name";
			props.put(key, kafkaProc.getValues(key, "hadoop.hadoop.com"));
	    }
	    
	    //设置分区数量
	    key = "num.partitions";
    	String partitions = kafkaProc.getValues(key, "1");
    	try {
    		int n = Integer.valueOf(partitions);
    		if(n > 1) {
    			if( !CommUtils.isEmpty(topicNames) ) {
	    			for(String topicName : topicNames) {
	    	    		props.put(topicName, partitions);
	    	    	}
    			}
    		}
    	} catch(NumberFormatException ex) {
    		 log.error(String.format("ConsumerConfig: partions解析异常{%s}", partitions));
    	}
    	
	    log.info("ConsumerConfig: props is " + props);
		return new KafkaConsumer<String, byte[]>(props);
	}
	
	private static Producer<String, byte[]> _producer_ = null;
	public static Producer<String, byte[]> getProducer() {
		if (_producer_ == null) {
			synchronized (lock) {
				if(_producer_ == null) {
					addShutDownHook();
					
					securityPrepare();
					
					_producer_ = createProducer();
				}
			}
		}
		return _producer_;
	}
	private static Producer<String, byte[]> createProducer() {
		KafkaProperties kafkaProc = KafkaProperties.getInstance();
		
		Properties props = new Properties();
		
		// Broker连接地址
		String key = "bootstrap.servers";
		props.put(key, kafkaProc.getValues(key, "127.0.0.1:21007"));
		 // 客户端ID
		key = "client.id";
		props.put(key, kafkaProc.getValues(key, "0"));
		
		
		key = "key.serializer";
		props.put(key, kafkaProc.getValues(key, "org.apache.kafka.common.serialization.StringSerializer"));
		key = "value.serializer";
		props.put(key, kafkaProc.getValues(key, "org.apache.kafka.common.serialization.ByteArraySerializer"));
		
		//配置安全认证相关参数
	    if( ConfigUtils.enableSecurity() ) {
	    	// 安全协议类型
	    	key = "security.protocol";
	    	props.put(key, kafkaProc.getValues(key, "SASL_PLAINTEXT"));
	    	// 服务名
	    	key = "sasl.kerberos.service.name";
	    	props.put(key, kafkaProc.getValues(key, "kafka"));
	    	// 域名
	    	key = "kerberos.domain.name";
	    	props.put(key, kafkaProc.getValues(key, "hadoop.hadoop.com"));
	    }
	    
	    log.info(String.format("ProducerConfig: props is %s", props));
		return new KafkaProducer<String, byte[]>(props);
	}
	
	public static void release() {
		synchronized (lock) {
			for (String key : consumer_map.keySet()) {
				Consumer<String, byte[]> _consumer_ = consumer_map.get(key);
				if (_consumer_ != null) {
					try {
						_consumer_.close();
						_consumer_ = null;

						log.info("kafka：consumer资源已释放");
					} catch (Exception ex) {
						log.error("kafka：consumer资源释放异常", ex);
					}
				}
				consumer_map.remove(key);
			}
			if(_producer_ != null) {
				try {
					_producer_.close();
					_producer_ = null;
					
					log.info("kafka：producer资源已释放");
				} catch(Exception ex) {
					log.error("producer资源释放异常", ex);
				}
			}
		}
	}
	
	private static void addShutDownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread("[kafka]-EndThread") {
			public void run() {
				try {
					release();
				} catch (Exception e) {
					log.error("资源释放异常", e);
				}
			}

		});
	}
	
	private static List<String> parseTopicName(String topicName) {
		List<String> topicList = new ArrayList<String>();

		if (topicName != null) {
			String[] arrTopic = topicName.split(Constants.DEFAULT_SEPERATOR);
			topicList = Arrays.asList(arrTopic);
		}

		return topicList;
	}

	protected static void securityPrepare() throws KafkaException{
		if( !needLogin(true) ) return;
		
		try {
			final String configDir = ConfigUtils.getBigDataConfigDir();
			final String principal = ConfigUtils.getPrincipal();
			
			String krbFile = configDir + "krb5.conf";
			String userKeyTableFile = configDir + "user.keytab";
			
			//windows路径下分隔符替换
	        userKeyTableFile = userKeyTableFile.replace("\\", "\\\\");
	        krbFile = krbFile.replace("\\", "\\\\");
	        
			LoginUtils.setKrb5Config(krbFile);
	        LoginUtils.setZookeeperServerPrincipal("zookeeper/hadoop.hadoop.com");
	        LoginUtils.setJaasFile(configDir, principal, userKeyTableFile);

			log.info("[kafka]login success!!!");
		} catch(IOException ex) {
			throw new KafkaException("[kafka]login failure!!!", ex);
		}
	}
	
	private static boolean needLogin(boolean force) {
		if( !ConfigUtils.enableSecurity() ) {
			return false;
		}
		
		if( force ) return true;
		
		return true;
	}
	
}
