package com.sailing.dataconverge.dto.wfsj;

import lombok.Data;

/**
 * 违法接入数据 DTO
 * Create by en
 * at 2018/10/18 15:37
 **/
@Data
public class WfsjDto {

    /**
     * 号牌号码
     */
    private String hphm;

    /**
     * 号牌种类
     */
    private String hpzl;

    /**
     * 号牌颜色
     * 00-白色，01-黄色，02-蓝色，03-黑色，04-未知，15-黄绿色,16-绿色,99-其它颜色
     */
    private String hpys;

    /**
     * 车辆属地
     * 车辆属地号牌号码中的汉字和第一位识别的字母。如“沪A”、“空U”，999数据无意义
     */
    private String clsd;

    /**
     * 过车时间，格式：yyyy-MM-dd HH:mm:ss
     */
    private String jgsj;

    /**
     * 过车时间毫秒数，默认0
     */
    private Integer hm = 0;

    /**
     * 区间测速时驶出测速区间时间，格式：yyyy-MM-dd HH:mm:ss
     */
    private String jgsj1;

    /**
     * 区间测速时驶出测速区间时间毫秒数，默认0
     */
    private Integer jgsj_hm = 0;

    /**
     * 车身颜色，
     * A：白，B：灰，C：黄，D：粉，E：红，F：紫，G：绿，H：蓝，I：棕，J：黑，Z：其他
     */
    private String csys;

    /**
     * 车标类型
     * 99：其他
     */
    private String cblx;

    /**
     * 图片id
     * 37位：设备编号（18位）+车道号（2位）+经过时间17位（yyyy-MM-dd HH:mm:ss:SSS 精确至毫秒）
     */
    private String tpid;

    /**
     * 左上角坐标x，没有传0
     */
    private Integer cpzb_left;

    /**
     * 左上角坐标y，没有传0
     */
    private Integer cpzb_top;

    /**
     * 右下角坐标x，没有传0
     **/
    private Integer cpzb_right;

    /**
     * 右下角坐标y，没有传0
     **/
    private Integer cpzb_bottom;

    /**
     * 车辆速度，单位 km/h
     * 255 数据无意义
     **/
    private Integer cs;

    /**
     * 区系统代码
     */
    private String ptdm;

    /**
     * 设备编号
     * 六合一平台上的十八位代码
     */
    private String sbdm;

    /**
     * 设备路口号
     **/
    private String lkh;

    /**
     * 断面方向，
     * 101：东向西
     * 102：西向东
     * 103：南向北
     * 104：北向南
     **/
    private Integer dmfx;

    /**
     * 断面描述
     */
    private String dmms;

    /**
     * 车道编号
     */
    private String cdbh;

    /**
     * 车身长度，
     * 单位：厘米
     * 99999：数据无意义
     **/
    private Integer cscd;

    /**
     * 车辆类型,
     * X99 其他
     */
    private String cllx;

    /**
     * 车型，
     * 1：小型车
     * 2：中型车
     * 3：大型车
     * 9：其他
     **/
    private Integer cx;

    /**
     * 出错码，
     * 0：正常；1：全零；2：汉字位无法识别；3：字母或者数字位无法识别；4：汉字无法识别并且字母或者数字位无法识别。
     **/
    private Integer ccm;

    /**
     * 出错原因，
     * 0：正常；1：无法定位号牌。
     **/
    private Integer ccyy;

    /**
     * 识别时间，单位：毫秒
     **/
    private Integer sbsj;

    /**
     * 号牌结构，
     * 1：单排 2：武警 3：警用 4：双排 5：其他
     **/
    private Integer hpjg;

    /**
     * 违法类型,卡口过车数据填0
     **/
    private Integer wflx;

    /**
     * 红灯时间，
     * 闯红灯违法行为抓拍时，表示该违法在红灯发生后多少时间，单位为秒
     * 卡口过车数据填0
     **/
    private Integer hdsj;

    /**
     * 限速，
     * 单位km/h，0-250，
     * 无限速则用0
     **/
    private Integer xs;

    /**
     * 图片数量，
     * 号牌记录附带的图片数量（0~N）
     **/
    private Integer tpsl;

    /**
     * 图片类型，多个图片通过逗号间隔
     * 1：车牌特写图片
     * 2：车牌全景图片
     * 3：非机动车特写图片
     * 4：非机动车全景图片
     * 5：行人特写图片
     * 6：行人全景图片
     * 7：违法合成图片一
     * 8：违法合成图片二
     */
    private String tplx;

    /**
     * 图片字节大小，多个图片通过逗号间隔
     */
    private String tpdx;

    /**
     * 区图片平台访问地址，多个图片通过逗号间隔
     */
    private String tpqdz;

    /**
     * 写入中心平台的图片的ftp地址，多个图片通过逗号间隔
     */
    private String tpzxdz;

    /**
     * 录像数量，无录像文件传0
     **/
    private Integer lxsl;

    /**
     * 录像文件类型，多个录像通过逗号间隔
     * 0：AVI 1：MPEG 2：MP4 3：RMVB 4：dav
     * 录像类型可扩充
     */
    private String lxwjlx;

    /**
     * 录像区平台访问地址，多个录像通过逗号间隔
     */
    private String lxqdz;

    /**
     * 录像文件字节大小，多个录像通过逗号间隔
     */
    private String lxqdx;

    /**
     * 区系统写入中心平台录像的ftp地址，多个录像通过逗号间隔
     */
    private String lxzxdz;

    /**
     * 写入时间：时间戳，精确到毫秒
     **/
    private String xrsj;

}