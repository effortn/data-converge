package com.sailing.dataconverge.kafka.utils;

import com.sailing.dataconverge.kafka.comm.Constants;
import com.sailing.dataconverge.utils.CommUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class ConfigUtils {
	private final static byte[] lock = new byte[0];

	//配置文件目录
	private static String CONFIG_DIR;
	private static String DEFAULT_CONFIG_DIR = Constants.DEFAULT_CONFIG_DIR;
	
	//认证用户
	private static String PRINCIPAL;
	private static String DEFAULT_PRINCIPAL = Constants.DEFAULT_PRINCIPAL;
	
	//是否开启安全认证
	private static Boolean SECURITY_ENABLE = null;
	private static boolean DEFAULT_SECURITY_ENABLE = Constants.DEFAULT_SECURITY_ENABLED;
	
	
	/**
	 * 获取配置文件目录，以 / 结尾
	 * @return
	 */
	public static String getConfigDir() {
		if(CommUtils.isEmpty(CONFIG_DIR)) {
			synchronized (lock) {
				if(CommUtils.isEmpty(CONFIG_DIR)) {
					String configDir = System.getProperty("config.dir");
					if( !CommUtils.isEmpty(configDir) ) {
						configDir = configDir.replaceAll("(/)+$", "");
						
						//windows路径下分隔符替换
						//configDir = configDir.replace("\\", "\\\\");
					}else {
						configDir = DEFAULT_CONFIG_DIR;
					}
					
					//路径规范化：1.相对路径转绝对路径；2.把.和..都等价去除
					Path path = Paths.get(configDir);
					CONFIG_DIR = path.normalize().toFile().getAbsolutePath() + File.separator;
				}
				log.info(String.format("[config.dir=%s]", CONFIG_DIR));
			}
		}
		return CONFIG_DIR;
	}
	
	/**
	 * 大数据相关配置文件目录
	 * @return
	 */
	public static String getBigDataConfigDir() {
		final String configDir = getConfigDir();
		
		Path path = Paths.get(configDir, "big-data");
		return path.normalize().toFile().getAbsolutePath() + File.separator;
	}
	
	/**
	 * 获取认证用户
	 * @return
	 */
	public static String getPrincipal() {
		if(CommUtils.isEmpty(PRINCIPAL)) {
			synchronized (lock) {
				if(CommUtils.isEmpty(PRINCIPAL)) {
					String principal = System.getProperty("sdba.principal");
					if( !CommUtils.isEmpty(principal) ) {
						PRINCIPAL = principal;
					} else {
						PRINCIPAL = DEFAULT_PRINCIPAL;
					}
					log.info(String.format("[sdba.principal=%s]", PRINCIPAL));
				}
			}
		}
		return PRINCIPAL;
	}
	
	public static boolean enableSecurity() {
		if(SECURITY_ENABLE == null) {
			synchronized (lock) {
				if(SECURITY_ENABLE == null) {
					String isDebug = System.getProperty("security.enable");
					if(isDebug == null) {
						SECURITY_ENABLE = DEFAULT_SECURITY_ENABLE;
					}else {
						boolean flag = CommUtils.isEnable(isDebug);
						if( !flag && !DEFAULT_SECURITY_ENABLE) {
							flag = DEFAULT_SECURITY_ENABLE;
						}
						SECURITY_ENABLE = flag;
					}
				}
				log.info(String.format("[security.enable=%s]", SECURITY_ENABLE));
			}
		}
		return SECURITY_ENABLE;
	}
}
