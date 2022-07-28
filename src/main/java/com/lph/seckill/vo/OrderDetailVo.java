package com.lph.seckill.vo;

import com.lph.seckill.pojo.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailVo {
    private Order order;

    private GoodsVo goodsVo;
}
