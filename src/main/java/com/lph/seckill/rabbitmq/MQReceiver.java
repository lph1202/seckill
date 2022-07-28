
package com.lph.seckill.rabbitmq;

import com.lph.seckill.pojo.SeckillMessage;
import com.lph.seckill.pojo.SeckillOrder;
import com.lph.seckill.pojo.User;
import com.lph.seckill.service.IGoodsService;
import com.lph.seckill.service.IOrderService;
import com.lph.seckill.utils.JsonUtil;
import com.lph.seckill.vo.GoodsVo;
import com.lph.seckill.vo.RespBean;
import com.lph.seckill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 消息发送者
 */



@Service
@Slf4j
public class MQReceiver {
//    @RabbitListener(queues = "queue")
//    public void receiver(Object msg){
//        log.info("接收消息："+msg);
//    }
//
//    @RabbitListener(queues = "queue_fanout01")
//    public void receiver01(Object msg){
//        log.info("FanoutQUEUE01接收消息："+msg);
//    }
//
//    @RabbitListener(queues = "queue_fanout02")
//    public void receiver02(Object msg){
//        log.info("FanoutQUEUE02接收消息："+msg);
//    }
//
//    @RabbitListener(queues = "queue_direct01")
//    public void receiver03(Object msg){
//        log.info("DirectQUEUE01接收消息："+msg);
//    }
//
//    @RabbitListener(queues = "queue_direct02")
//    public void receiver04(Object msg){
//        log.info("DirectQUEUE02接收消息："+msg);
//    }
//
//    @RabbitListener(queues = "queue_topic01")
//    public void receiver05(Object msg){
//        log.info("TopicQUEUE01接收消息："+msg);
//    }
//
//    @RabbitListener(queues = "queue_topic02")
//    public void receiver06(Object msg){
//        log.info("TopicQUEUE02接收消息："+msg);
//    }
//
//    @RabbitListener(queues = "queue_header01")
//    public void receiver07(Message msg){
//        log.info("headerQUEUE01接收Message对象："+msg);
//        log.info("headerQUEUE01接收消息："+new String(msg.getBody()));
//    }
//
//    @RabbitListener(queues = "queue_header02")
//    public void receiver08(Message msg){
//        log.info("headerQUEUE01接收Message对象："+msg);
//        log.info("headerQUEUE02接收消息："+new String(msg.getBody()));
//    }

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IOrderService orderService;

    /**
     * 下单操作
     */
    @RabbitListener(queues = "seckillQueue")
    public void receive(String message){
        log.info("接收到的消息："+message);
        SeckillMessage seckillMessage = JsonUtil.jsonStr2Object(message, SeckillMessage.class);
        Long goodsId = seckillMessage.getGoodsId();
        User user = seckillMessage.getUser();
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        if(goodsVo.getStockCount() < 1){
            return;
        }
        //判断是否重复抢购
        SeckillOrder seckillOrder =
                (SeckillOrder) redisTemplate.opsForValue().get("order:"+user.getId()+":"+goodsId);
        if(seckillOrder!=null){
            return ;
        }
        //下单操作
        orderService.seckill(user,goodsVo);
    }
}
