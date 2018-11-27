package com.sailing.dataconverge.repository;

import com.sailing.dataconverge.domain.SbLkhEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * 设备路口对应数据查询接口
 * create by en
 * at 2018/10/25 16:17
 **/
@Repository
public interface SbLkhRepository extends JpaRepository<SbLkhEntity, String> {

    /**
     * 查询当前数据库时间
     * @return
     */
    @Query(value = "SELECT TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS') FROM DUAL", nativeQuery = true)
    String querySysdate();

}
