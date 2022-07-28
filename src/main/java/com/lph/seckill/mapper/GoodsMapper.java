package com.lph.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lph.seckill.pojo.Goods;
import com.lph.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LPH
 * @since 2022-05-20
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    List<GoodsVo> findGoodsVo();

    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
