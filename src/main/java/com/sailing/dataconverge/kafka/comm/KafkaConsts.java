package com.sailing.dataconverge.kafka.comm;

public interface KafkaConsts {
	
	//本地环境
	public final static String ZOOKEEPERCONNECT = "127.0.0.1:9092";
	public final static String GROUP_ID = "0";
	public final static String TOPIC = "vehlp-data";
	
	//生产环境
//	public final static String zookeeperConnect = "43.122.30.200:24002,43.122.30.201:24002,43.122.30.202:24002/kafka";
//	public final static String groupId = "seisysEtl";
//	public final static String topic = "msg-kk";
	//-----------------------------------------------------------------//
	
	
	/** 
	 * kafka consumer ： pollTimeOut（单位：毫秒）
	 * */
	public final static long DEFAULT_POLL_TIME_OUT = 100; 
	
	public final static String DEFAULT_TOPIC_SEPERATOR = ";";
	
}
