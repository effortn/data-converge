package com.sailing.dataconverge.repository;

import com.sailing.dataconverge.domain.SbztEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 设备状态信息查询 Jpa 接口
 * create by en
 * at 2018/10/24 17:12
 **/
@Repository
public interface SbztRepository extends JpaRepository<SbztEntity, Long> {

    /**
     * 根据设备编号查询设备状态
     * @param sbbhList      设备编号
     * @return
     */
    List<SbztEntity> findBySbbhIn(List<String> sbbhList);

}
