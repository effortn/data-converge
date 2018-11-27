package com.sailing.dataconverge.repository;

import com.sailing.dataconverge.domain.WftcEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * 违法数据查询 jpa 接口
 * create by en
 * at 2018/10/24 17:15
 **/
@Repository
public interface WftcRepository extends JpaRepository<WftcEntity, String> {

    /**
     * 根据写入时间查询违法数据
     * @param startTime
     * @param endTime
     * @return
     */
    List<WftcEntity> queryByXrsjBetween(Timestamp startTime, Timestamp endTime);

    /**
     * 根据写入时间和设备编号查询违法数据
     * @param startTime
     * @param endTime
     * @param sbbhList
     * @return
     */
    List<WftcEntity> queryByXrsjBetweenAndBzwzdmIn(Timestamp startTime, Timestamp endTime, List<String> sbbhList);

}
