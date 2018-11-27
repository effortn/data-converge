package com.sailing.dataconverge.kafka.comm;

public class Constants {
	/** 默认认证用户：大数据 */
	public static final String DEFAULT_PRINCIPAL = "test";
	
	/** 默认配置文件加载目录 */
	public static final String DEFAULT_CONFIG_DIR = "./config";
	
	/** 默认分隔符 */
	public static final String DEFAULT_SEPERATOR = ",";
	
	/** 默认换行符 */
	public static final String DEFAULT_LINE_SEPARATOR = System.getProperty("line.separator");
	
	/** 区分运行环境 : true-本地环境；false-生产环境，默认 */
	public static final boolean DEFAULT_IS_DEBUG = true;
	
	/** 是否开启安全认证，true-开启；false-不开启，默认 */
	public final static boolean DEFAULT_SECURITY_ENABLED = true;
	
	/** 内存队列容量 */
	public final static int DEFAULT_DATA_QUEUE_CAPACITY = 30000;
	
	/** 线程池大小 */
	public final static int DEFAULT_THREAD_POOL_SIZE = 5;
	
	/** kafka-topic 默认分区数*/
	public final static int DEFAULT_KAFKA_PARTITIONS = 1;
	
	/** 内存队列一次出栈最大数量 */
	public final static int DEFAULT_QUEUE_MAX_ELEMENTS = 100;
	
	/** 过车数据过期时间，单位秒，默认2个小时 */
	public final static int DEFAULT_LPDATA_EXPIRE_SECOND = 2*60*60;
	
	/** 心跳时间，单位毫秒 */
	public final static long DEFAULT_HEARTBEAT_TIME = 300000;
}
