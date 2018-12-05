package com.sailing.dataconverge.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.util.Assert;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Ftp 文件上传工具
 * create by en
 * at 2018/10/23 10:47
 **/
@Slf4j
public class FtpUtils {

    // 做一个文件上传的轮询计数器，用来轮询上传的 ftp服务器
    private static AtomicInteger FTP_COUNTER = new AtomicInteger(0);

    // 记录 ftp服务器 的数量
    private static Integer FTP_SIZE = 0;

    // 存放 ftp服务器 的集合
    private static Map<String, FTPClient> FTP_CLIENT_MAP = new ConcurrentHashMap<>();

    // 存放 ftp服务器的集合的key的集合
    private static List<String> FTP_CLIENT_KEY_LIST = new ArrayList<>();

    // 存放 ftp服务器 的集合的 key 的格式：hostname:port-username
    private final static String KEY_FORMAT = "%s:%d-%s";

    // 图片在ftp上的地址
    private final static String FTP_URL = "ftp://%s:%s@%s/%s";

    private final static String HOST = "15.32.12.142";
    private final static Integer PORT = 21;
    private final static String USER = "dzjc_bs";
    private final static String PASSWORD = "dzjc_bs";

    /**
     * 初始化 FTP工具类
     */
    public static void init() {
        /* 取消采用kafka 方式传递ftp连接信息
        TopicConsumeConfig topicConsumeConfig = (TopicConsumeConfig) SpringUtils.getBean(TopicConsumeConfig.class);
        String ftpConsumeTopicName = topicConsumeConfig.getFtp();
        TopicProduceConfig topicProduceConfig = (TopicProduceConfig) SpringUtils.getBean(TopicProduceConfig.class);
        String ftpProduceTopicName = topicProduceConfig.getFtpFk();
        while (true) {
            int queueSize = KafkaConsumeQueueData.queueSize(ftpConsumeTopicName);
            if (queueSize == 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            String ftpInfoJson = KafkaConsumeQueueData.consumeQueue(ftpConsumeTopicName);
            log.info("【FTP】获取连接信息:{}", ftpInfoJson);
            FtpResponseDto ftpResponseDto = new FtpResponseDto();
            FtpRequestDto ftpInfo = null;
            try {
                ftpInfo = JSON.parseObject(ftpInfoJson, FtpRequestDto.class);
                int ftpsl = ftpInfo.getFtpsl();
                String[] ftpdzs = ftpInfo.getFtpdz().split(",");
                String[] dks = ftpInfo.getFtpdk().split(",");
                String[] yhms = ftpInfo.getFtpyhm().split(",");
                String[] mms = ftpInfo.getFtpmm().split(",");
                for (int i = 0; i < ftpsl; i++) {
                    addFtpClient(ftpdzs[i], Integer.valueOf(dks[i]), yhms[i], mms[i]);
                }
                ftpResponseDto.setGuid(ftpInfo.getGuid());
                ftpResponseDto.setMsgtype(ftpInfo.getMsgtype());
                ftpResponseDto.setJg(1);
                ftpResponseDto.setXrsj(System.currentTimeMillis());
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("【FTP】获取连接信息:{}，解析报错", ftpInfoJson, e);
                if (ftpInfo != null) {
                    ftpResponseDto.setGuid(ftpInfo.getGuid());
                    ftpResponseDto.setMsgtype(ftpInfo.getMsgtype());
                }
                ftpResponseDto.setJg(0);
                ftpResponseDto.setXrsj(System.currentTimeMillis());
            }
            log.info("【FTP】连接信息解析完成:{}，回复：{}", ftpInfoJson, ftpResponseDto);
            KafkaProduceQueueData.produceQueue(ftpProduceTopicName, JSON.toJSONString(ftpResponseDto));
        }*/
        addFtpClient(HOST, PORT, USER, PASSWORD);
    }

    /**
     * 添加 Ftp 连接客户端
     *
     * @param hostname 主机名
     * @param port     ftp 端口
     * @param username 用户名
     * @param password 密码
     * @return
     */
    private synchronized static boolean addFtpClient(String hostname, int port, String username, String password) {
        String key = String.format(KEY_FORMAT, hostname, port, username);
        if (FTP_CLIENT_MAP.containsKey(key)) {
            log.info("【FTP】连接已存在...ftp服务器:{}:{}, 用户名：{}，密码：{}", hostname, port, username, password);
            return true;
        }
        FTPClient ftpClient = new FTPClient();
        ftpClient.setControlEncoding("utf-8");
        try {
            log.info("【FTP】connecting...ftp服务器:{}:{}", hostname, port);
            ftpClient.connect(hostname, port); //连接ftp服务器
            ftpClient.login(username, password); //登录ftp服务器
            int replyCode = ftpClient.getReplyCode(); //是否成功登录服务器
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                log.info("【FTP】连接失败...ftp服务器:{}:{}, 用户名：{}，密码：{}", hostname, port, username, password);
                return false;
            }
            log.info("【FTP】连接成功...ftp服务器:{}:{}, 用户名：{}，密码：{}", hostname, port, username, password);
            FTP_CLIENT_MAP.put(key, ftpClient);
            FTP_CLIENT_KEY_LIST.add(key);
            FTP_SIZE++;
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            log.info("【FTP】连接报错...ftp服务器:{}:{}, 用户名：{}，密码：{}", hostname, port, username, password, e);
        } catch (IOException e) {
            e.printStackTrace();
            log.info("【FTP】连接报错...ftp服务器:{}:{}, 用户名：{}，密码：{}", hostname, port, username, password, e);
        }
        return false;
    }

    private static String getKey() {
        // 1. 判断 ftp客户端是否存在，不存在无法进行
        Assert.isTrue(FTP_SIZE != 0, "【FTP】不存在客户端！");
        // 2. 计数器若大于最大整数时，重置
        if (FTP_COUNTER.get() >= Integer.MAX_VALUE - 1) {
            FTP_COUNTER = new AtomicInteger(FTP_COUNTER.get() % FTP_SIZE);
        }
        // 3. 获取轮询到的 ftp 客户端
        return FTP_CLIENT_KEY_LIST.get(FTP_COUNTER.getAndIncrement() % FTP_SIZE);
    }

    /**
     * 获取 FTPClient
     * @return
     */
    private static FTPClient getFtpClient(String key) {
        return FTP_CLIENT_MAP.get(key);
    }

    /**
     * 上传本地文件
     * @param pathName ftp服务保存地址
     * @param fileName 上传到ftp的文件名
     *  @param originFileName 待上传文件的名称（绝对地址） *
     * @return
     */
    public static String uploadLocalFile( String pathName, String fileName,String originFileName){
        log.info("【FTP】开始上传文件，文件路径：{},保存路径：{}，文件名：{}", originFileName, pathName, fileName);
        try {
            String flag = uploadFile(pathName, fileName, new FileInputStream(new File(originFileName)));
            if (!CommUtils.isEmpty(flag)) {
                log.info("【FTP】上传文件成功，文件路径：{}", originFileName);
            } else {
                log.error("【FTP】上传文件失败，文件路径：{}", originFileName);
            }
            return flag;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            log.error("【FTP】上传文件失败，文件不存在，文件路径：{}", originFileName, e);
            return null;
        }
    }

    /**
     * 上传网络文件
     * @param pathName      上传路径
     * @param fileName      上传文件名
     * @param url           文件地址
     * @return
     */
    public static String uploadNetFile(String pathName, String fileName, String url) {
        log.info("【FTP】开始上传文件，文件地址：{},保存路径：{}，文件名：{}", url, pathName, fileName);
        try {
            URL picURL = new URL(url);
            String flag = uploadFile(pathName, fileName, picURL.openStream());
            if (!CommUtils.isEmpty(flag)) {
                log.info("【FTP】上传文件成功，文件地址：{}", url);
            } else {
                log.error("【FTP】上传文件失败，文件地址：{}", url);
            }
            return flag;
        } catch (IOException e) {
            e.printStackTrace();
            log.error("【FTP】上传文件报错，文件地址：{}", url, e);
            return null;
        }
    }

    /**
     * 上传文件
     * @param pathname ftp服务保存地址
     * @param fileName 上传到ftp的文件名
     * @param inputStream 输入文件流
     * @return
     */
    public static String uploadFile( String pathname, String fileName,InputStream inputStream){
        // 1. 获取 ftp 客户端
        String key = getKey();
        FTPClient ftpClient = getFtpClient(key);
        if (ftpClient == null) {
            init();
            ftpClient = getFtpClient(getKey());
        }
        // 2. 上传文件
        String flag = null;
        try{
            log.debug("【FTP】开始上传文件");
            ftpClient.setFileType(ftpClient.BINARY_FILE_TYPE);
            // 3. 创建目录
            CreateDirecroty(pathname, ftpClient);
            ftpClient.makeDirectory(pathname);
            ftpClient.changeWorkingDirectory(pathname);
            ftpClient.storeFile(fileName, inputStream);
            String host = key.split("-")[0];
            String file = pathname + "/" + fileName;
            flag = String.format(FTP_URL, USER, PASSWORD, host, file);
            log.debug("【FTP】上传文件成功");
        }catch (Exception e) {
            e.printStackTrace();
            log.error("【FTP】上传文件失败!", e);
        }finally{
            if(null != inputStream){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }

    /**
     * 创建多层目录文件，如果有ftp服务器已存在该文件，则不创建，如果无，则创建
     * @param remote            需要创建的目录
     * @param ftpClient         FTP 客户端
     * @return
     * @throws IOException
     */
    private static boolean CreateDirecroty(String remote, FTPClient ftpClient) throws IOException {
        boolean success = true;
        String directory = remote + "/";
        // 如果远程目录不存在，则递归创建远程服务器目录
        if (!directory.equalsIgnoreCase("/") && !changeWorkingDirectory(new String(directory), ftpClient)) {
            int start = 0;
            int end = 0;
            if (directory.startsWith("/")) {
                start = 1;
            } else {
                start = 0;
            }
            end = directory.indexOf("/", start);
            String path = "";
            String paths = "";
            while (true) {
                String subDirectory = new String(remote.substring(start, end).getBytes("GBK"), "iso-8859-1");
                path = path + "/" + subDirectory;
                if (!existFile(path, ftpClient)) {
                    if (makeDirectory(subDirectory, ftpClient)) {
                        log.debug("【FTP】创建目录{}成功！", subDirectory);
                        changeWorkingDirectory(subDirectory, ftpClient);
                    } else {
                        log.debug("【FTP】创建目录{}失败！", subDirectory);
                        changeWorkingDirectory(subDirectory, ftpClient);
                    }
                } else {
                    changeWorkingDirectory(subDirectory, ftpClient);
                }

                paths = paths + "/" + subDirectory;
                start = end + 1;
                end = directory.indexOf("/", start);
                // 检查所有目录是否创建完毕
                if (end <= start) {
                    break;
                }
            }
        }
        return success;
    }

    //判断ftp服务器文件是否存在
    public static boolean existFile(String path, FTPClient ftpClient) throws IOException {
        boolean flag = false;
        FTPFile[] ftpFileArr = ftpClient.listFiles(path);
        if (ftpFileArr.length > 0) {
            flag = true;
        }
        return flag;
    }
    //创建目录
    public static boolean makeDirectory(String dir, FTPClient ftpClient) {
        boolean flag = true;
        try {
            flag = ftpClient.makeDirectory(dir);
            if (flag) {
                log.debug("【FTP】创建文件夹{}成功！", dir);
            } else {
                log.error("【FTP】创建文件夹{}失败！", dir);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("【FTP】创建文件夹{}报错！", dir, e);
        }
        return flag;
    }

    //改变目录路径
    public static boolean changeWorkingDirectory(String directory, FTPClient ftpClient) {
        boolean flag = true;
        try {
            flag = ftpClient.changeWorkingDirectory(directory);
            if (flag) {
                log.debug("【FTP】进入文件夹{}成功！", directory);
            } else {
                log.debug("【FTP】进入文件夹{}失败！开始创建文件夹", directory);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            log.error("【FTP】进入文件夹{}报错！", directory, ioe);
        }
        return flag;
    }
/*
    public static void main(String[] args) {
        FtpUtils.init();
        String s = FtpUtils.uploadNetFile("310113/wf/20181105/10", "311300000000090201120181105105123000_1.jpg", "http://15.193.248.57:8080/IllegalparkingPic/201811/05/311300000000090201_20181105105123_02.jpg");
        System.out.println(s);

    }*/
}
