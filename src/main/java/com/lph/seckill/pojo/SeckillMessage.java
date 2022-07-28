package com.lph.seckill.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 秒杀信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SeckillMessage {
    private User user;
    private Long goodsId;
}
