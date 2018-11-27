package com.sailing.dataconverge.service.impl;

import com.sailing.dataconverge.config.ConvergeConstent;
import com.sailing.dataconverge.convert.SbdmConvert;
import com.sailing.dataconverge.convert.SbxxEntityToDtoConvert;
import com.sailing.dataconverge.convert.SbztEntityToDtoConvert;
import com.sailing.dataconverge.convert.WftcEntityToDtoConvert;
import com.sailing.dataconverge.domain.SbLkhEntity;
import com.sailing.dataconverge.domain.SbxxEntity;
import com.sailing.dataconverge.domain.SbztEntity;
import com.sailing.dataconverge.domain.WftcEntity;
import com.sailing.dataconverge.dto.sbpz.SbConfigInfoDto;
import com.sailing.dataconverge.dto.sbzt.SbStateInfoDto;
import com.sailing.dataconverge.dto.wfsj.WfsjDto;
import com.sailing.dataconverge.repository.SbLkhRepository;
import com.sailing.dataconverge.repository.SbxxRepository;
import com.sailing.dataconverge.repository.SbztRepository;
import com.sailing.dataconverge.repository.WftcRepository;
import com.sailing.dataconverge.service.ConvergeService;
import com.sailing.dataconverge.utils.CommUtils;
import com.sailing.dataconverge.utils.FtpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ConvergeServiceImpl implements ConvergeService {
    
    @Autowired
    private SbxxRepository sbxxRepository;
    
    @Autowired
    private SbztRepository sbztRepository;
    
    @Autowired
    private WftcRepository wftcRepository;

    @Autowired
    private SbLkhRepository sbLkhRepository;

    private static byte[] lock = new byte[0];

    private Function<WftcEntity, WfsjDto> wfsjFunction;

    private Function<SbztEntity, SbStateInfoDto> sbztFunction = new Function<SbztEntity, SbStateInfoDto>() {
        @Override
        public SbStateInfoDto apply(SbztEntity sbztEntity) {
            return SbztEntityToDtoConvert.convert(sbztEntity);
        }
    };

    private Function<SbxxEntity, SbConfigInfoDto> sbxxFunction = new Function<SbxxEntity, SbConfigInfoDto>() {
        @Override
        public SbConfigInfoDto apply(SbxxEntity sbxxEntity) {
            return SbxxEntityToDtoConvert.convert(sbxxEntity);
        }
    };

    @Override
    public List<WfsjDto> queryWfsj(String sbdms, long startTime, long endTime) {
        List<WftcEntity> wftcEntityList;
        if (CommUtils.isEmpty(sbdms)) {
            wftcEntityList = wftcRepository.queryByXrsjBetween(new Timestamp(startTime), new Timestamp(endTime));
        } else {
            // 1. 对设备代码进行转换
            String bdsbdms = SbdmConvert.getBdsbdms(sbdms);
            wftcEntityList = wftcRepository.queryByXrsjBetweenAndBzwzdmIn(new Timestamp(startTime),
                    new Timestamp(endTime), Arrays.asList(bdsbdms.split(",")));
        }
        if (CommUtils.isEmpty(wftcEntityList)) {
            return null;
        }
        // 双重锁机制，防止重复执行
        if (wfsjFunction == null) {
            synchronized (lock) {
                if (wfsjFunction == null) {
                    wfsjFunction = new Function<WftcEntity, WfsjDto>() {
                        @Override
                        public WfsjDto apply(WftcEntity wftcEntity) {
                            log.info("【违法数据获取】违法数据进行转换，{}", wftcEntity);

                            String sbdm = wftcEntity.getSbbh();
                            // <初始判断>. 先去查询设备信息和设备路口对应关系，若数据不正确，则直接返回 null
                            Optional<SbxxEntity> sbxx = sbxxRepository.findById(sbdm);
                            //  0-1. 设备信息判断：设备信息是否为空，预留字段2（即标准18位设备代码）是否为空，字段长度是否为18
                            if (!sbxx.isPresent() || CommUtils.isEmpty(sbxx.get().getYlzd2()) || sbxx.get().getYlzd2().length() != 18) {
                                log.error("【违法数据获取】数据设备信息有问题，无设备信息或无标准18位设备代码，记录编号:{}", wftcEntity.getJlbh());
                                return null;
                            }
                            Optional<SbLkhEntity> sbLkh = sbLkhRepository.findById(sbdm);
                            //  0-2. 路口号对应关系判断，数据是否为空，路口号是否为空
                            if (!sbLkh.isPresent() || CommUtils.isEmpty(sbLkh.get().getLkh())) {
                                log.error("【违法数据获取】数据设备信息有问题，设备无对应路口号，记录编号:{}", wftcEntity.getJlbh());
                                return null;
                            }
                            // 标准18位设备代码
                            String bzsbdm = sbxx.get().getYlzd2();
                            // 1. 对不需要进行操作的字段首先进行转换
                            WfsjDto wfsjDto = WftcEntityToDtoConvert.convert(wftcEntity);
                            wfsjDto.setSbdm(bzsbdm);
                            // 2. 查询路口号，并赋值
                            wfsjDto.setLkh(sbLkh.get().getLkh());
                            // 3. 对图片上传和设置
                            int tpsl = (int) Math.abs(wftcEntity.getTpsl());
                            //  /区系统代码/wf/yyyymmdd/hh24/TPID_图片类型.jpg
                            // TPID为37个字符，结构为：设备编号（18位）+车道号（2位）+
                            // 经过时间17位（yyyyMMddHHmmssSSS 时间精确至毫秒）
                            // 3-1. 获取 yyyyMMddHHmmssSSS 时间格式的字符串
                            String dateString = ConvergeConstent.DATE_TIME_FORMATTER.print(wftcEntity.getJgsj().getTime());
                            // 3-2. 格式化图片id
                            String tpid = String.format(ConvergeConstent.TPID_FORMAT, bzsbdm, wftcEntity.getCdbh(), dateString);
                            wfsjDto.setTpid(tpid);
                            // 3-3. 格式化图片存放路径
                            String tpPath = String.format(ConvergeConstent.WFTP_PATH_FORMAT,
                                            ConvergeConstent.PTDM(), dateString.substring(0,8), dateString.substring(8, 10));
                            // 3-4. 拼接图片相关的地址和类型
                            String tpqdz = "";
                            String tpzxdz = "";
                            String tplx = "";
                            for (int i = 0; i < tpsl; i++) {
                                String methodName = "getTp" + (i + 1);
                                try {
                                    // 通过反射获取对应的图片地址
                                    String tpdz = (String) WftcEntity.class.getMethod(methodName).invoke(wftcEntity);
                                    if (CommUtils.isEmpty(tpdz)) {
                                        continue;
                                    }
                                    String fileName = tpid + "_" + (i + 1) + ".jpg";
                                    // FTP上传图片并获取图片的保存地址
                                    String fileUrl = FtpUtils.uploadNetFile(tpPath, fileName, tpdz);
                                    if (fileUrl != null) {
                                        tpzxdz = tpzxdz.concat(fileUrl);
                                        tpqdz = tpqdz.concat(tpdz);
                                        tplx = tplx + i;
                                        if (i < tpsl - 1) {
                                            tpqdz = tpqdz.concat(",");
                                            tpzxdz = tpzxdz.concat(",");
                                            tplx = tplx.concat(",");
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    log.error("【违法数据获取】图片地址获取失败，过车记录：{}，方法名：{}"
                                            , wftcEntity.getJlbh(), methodName, e);
                                }
                            }
                            // 3-5. 设置图片地址
                            wfsjDto.setTpzxdz(tpzxdz);
                            wfsjDto.setTpqdz(tpqdz);
                            // 4. 设置断面描述
                            wfsjDto.setDmms(sbxx.get().getSbmc());
                            return wfsjDto;
                        }
                    };
                }
            }
        }
        // 将违法数据转换为dto
        List<WfsjDto> wfsjDtoList = wftcEntityList.stream().map(wfsjFunction).collect(Collectors.toList());

        return wfsjDtoList;
    }

    @Override
    public List<SbConfigInfoDto> querySbConfig(String sbdms) {
        List<SbxxEntity> sbxxEntityList;
        if (CommUtils.isEmpty(sbdms)) {
            sbxxEntityList = sbxxRepository.findAll();
        } else {
            sbxxEntityList = sbxxRepository.queryBySbbhIn(Arrays.asList(sbdms.split(",")));
        }
        if (CommUtils.isEmpty(sbxxEntityList)) {
            return null;
        }
        List<SbConfigInfoDto> sbConfigInfoDtoList = sbxxEntityList.stream().map(sbxxFunction).collect(Collectors.toList());
        return sbConfigInfoDtoList;
    }

    @Override
    public List<SbStateInfoDto> querySbState(String sbdms) {
        List<SbztEntity> sbztEntityList;
        if (CommUtils.isEmpty(sbdms)) {
            sbztEntityList = sbztRepository.findAll();
        } else {
            sbztEntityList = sbztRepository.findBySbbhIn(Arrays.asList(sbdms.split(",")));
        }
        if (CommUtils.isEmpty(sbztEntityList)) {
            return null;
        }
        List<SbStateInfoDto> sbStateInfoDtoList = sbztEntityList.stream().map(sbztFunction).collect(Collectors.toList());
        return sbStateInfoDtoList;
    }
}
