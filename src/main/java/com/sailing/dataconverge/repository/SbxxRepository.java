package com.sailing.dataconverge.repository;

import com.sailing.dataconverge.domain.SbxxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 设备信息数据查询 jpa 接口
 * create by en
 * at 2018/10/24 17:01
 **/
@Repository
public interface SbxxRepository extends JpaRepository<SbxxEntity, String> {

    /**
     * 根据设备编号查询设备信息
     * @param sbbhList      设备编号
     * @return
     */
    List<SbxxEntity> queryBySbbhIn(List<String> sbbhList);

}
