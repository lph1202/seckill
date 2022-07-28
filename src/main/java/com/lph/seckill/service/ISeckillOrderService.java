package com.lph.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lph.seckill.pojo.SeckillOrder;
import com.lph.seckill.pojo.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LPH
 * @since 2022-05-20
 */
public interface ISeckillOrderService extends IService<SeckillOrder> {

    /**
     * 获取秒杀结果
     * @param user
     * @param goodsId
     * @return  orderId:成功，-1：秒杀失败，0：排队中
     */
    Long getResult(User user, Long goodsId);
}
