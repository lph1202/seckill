package com.lph.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lph.seckill.pojo.Order;
import com.lph.seckill.pojo.User;
import com.lph.seckill.vo.GoodsVo;
import com.lph.seckill.vo.OrderDetailVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LPH
 * @since 2022-05-20
 */
public interface IOrderService extends IService<Order> {

    /**
     * 秒杀
     * @param user
     * @param goods
     * @return
     */
    Order seckill(User user, GoodsVo goods);

    /**
     * 订单详情
     * @param orderId
     * @return
     */
    OrderDetailVo detail(Long orderId);

    /**
     * 获取秒杀地址
     * @param user
     * @param goodsId
     * @return
     */
    String createPath(User user, Long goodsId);

    /**
     * 校验秒杀地址
     * @param user
     * @param goodsId
     * @return
     */
    Boolean checkPath(User user, Long goodsId,String path);

    /**
     * 校验验证码
     * @param user
     * @param goodsId
     * @param captcha
     * @return
     */
    Boolean checkCaptcha(User user, Long goodsId, String captcha);
}
