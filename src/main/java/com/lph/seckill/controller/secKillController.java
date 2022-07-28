package com.lph.seckill.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.lph.seckill.config.AccessLimit;
import com.lph.seckill.exception.GlobalException;
import com.lph.seckill.pojo.Order;
import com.lph.seckill.pojo.SeckillMessage;
import com.lph.seckill.pojo.SeckillOrder;
import com.lph.seckill.pojo.User;
import com.lph.seckill.rabbitmq.MQSender;
import com.lph.seckill.service.IGoodsService;
import com.lph.seckill.service.IOrderService;
import com.lph.seckill.service.ISeckillOrderService;
import com.lph.seckill.utils.JsonUtil;
import com.lph.seckill.vo.GoodsVo;
import com.lph.seckill.vo.RespBean;
import com.lph.seckill.vo.RespBeanEnum;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.annotation.XmlAccessOrder;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/seckill")
@Slf4j
public class secKillController implements InitializingBean {

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private ISeckillOrderService seckillOrderService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MQSender mqSender;

    @Autowired
    private RedisScript<Long> redisScript;

    private Map<Long,Boolean> emptyStockMap = new HashMap<>();
    /**
     * 秒杀
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping("/doSeckill2")
    public String doSecKill2(Model model, User user,Long goodsId){
        if(user == null){
            return "login";
        }
        model.addAttribute("user",user);
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
        //判断库存
        if(goods.getStockCount()<1){
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            return "secKillFail";
        }
        //判断是否重复抢购
        SeckillOrder seckillOrder =
                seckillOrderService.getOne(new QueryWrapper<SeckillOrder>()
                        .eq("user_id", user.getId()).eq("goods_id", goodsId));
        if(seckillOrder!=null){
            model.addAttribute("errmsg", RespBeanEnum.REPEAT_ERROR.getMessage());
            return "secKillFail";
        }
        Order order = orderService.seckill(user,goods);
        model.addAttribute("order",order);
        model.addAttribute("goods",goods);
        return "orderDetail";
    }


    /**
     * 秒杀
     * @param path
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/{path}/doSeckill",method = RequestMethod.POST)
    @ResponseBody
    public RespBean doSecKill(@PathVariable String path, User user, Long goodsId){
        if(user == null){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        ValueOperations valueOperations = redisTemplate.opsForValue();

        Boolean check = orderService.checkPath(user,goodsId,path);
        if(!check){
            return RespBean.error(RespBeanEnum.REQUEST_ILLEGAL);
        }

        //判断是否重复抢购
        SeckillOrder seckillOrder =
                (SeckillOrder) redisTemplate.opsForValue().get("order:"+user.getId()+":"+goodsId);
        if(seckillOrder!=null){
            return RespBean.error(RespBeanEnum.REPEAT_ERROR);
        }
        //通过内存标记，减少redis访问
        if (emptyStockMap.get(goodsId)){
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        //预减库存
//        Long stock = valueOperations.decrement("seckillGoods:" + goodsId);
        Long stock = (Long) redisTemplate.execute(redisScript,
                Collections.singletonList("seckillGoods:" + goodsId), Collections.EMPTY_LIST);
        if(stock < 0 ){
            emptyStockMap.put(goodsId,true);
            valueOperations.increment("seckillGoods:"+goodsId);
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        SeckillMessage seckillMessage = new SeckillMessage(user, goodsId);
        mqSender.sendSeckillMessage(JsonUtil.object2JsonStr(seckillMessage));
        return RespBean.success(0);
//        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
//        //判断库存
//        if(goods.getStockCount()<1){
//            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
//            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
//        }
//        //判断是否重复抢购
////        SeckillOrder seckillOrder =
////                seckillOrderService.getOne(new QueryWrapper<SeckillOrder>()
////                        .eq("user_id", user.getId()).eq("goods_id", goodsId));
//        SeckillOrder seckillOrder =
//                (SeckillOrder) redisTemplate.opsForValue().get("order:"+user.getId()+":"+goods.getId());
//        if(seckillOrder!=null){
//
//            return RespBean.error(RespBeanEnum.REPEAT_ERROR);
//        }
//        Order order = orderService.seckill(user,goods);
//        return RespBean.success(order);
    }

    /**
     * 获取秒杀结果
     * @param user
     * @param goodsId
     * @return orderId:成功，-1：秒杀失败，0：排队中
     */
    @RequestMapping(value = "/getResult",method = RequestMethod.GET)
    @ResponseBody
    public RespBean getResult(User user,Long goodsId){
        if (user==null){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        Long orderId = seckillOrderService.getResult(user,goodsId);
        return RespBean.success(orderId);
    }


    /**
     * 获取秒杀地址
     * @param user
     * @param goodsId
     * @return
     */
    @AccessLimit(second=5,maxCount=5,needLogin=true)
    @RequestMapping("/path")
    @ResponseBody
    public RespBean getPath(User user, Long goodsId, String captcha, HttpServletRequest request){
        if(user == null){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //限制访问次数，5秒内访问5次
//        String uri = request.getRequestURI();
//        Integer count = (Integer) valueOperations.get(uri + ":" + user.getId());
//        if (count==null){
//            valueOperations.set(uri + ":" + user.getId(),1,5,TimeUnit.SECONDS);
//        }else if(count < 5){
//            valueOperations.increment(uri + ":" + user.getId());
//        }else{
//            return RespBean.error(RespBeanEnum.ACCESS_LIMIT_REACHED);
//        }

        Boolean checkCaptcha = orderService.checkCaptcha(user,goodsId,captcha);
        if(!checkCaptcha){
            return RespBean.error(RespBeanEnum.ERROR_CAPTCHA);
        }
        String str = orderService.createPath(user,goodsId);
        return RespBean.success(str);
    }

    @RequestMapping(value = "/captcha",method = RequestMethod.GET)
    public void verifyCode(User user, Long goodsId, HttpServletResponse response){
        if(user==null||goodsId < 0){
            throw new GlobalException(RespBeanEnum.REQUEST_ILLEGAL);
        }
        //设置请求头为图片的类型
        response.setContentType("image/jpg");
        response.setHeader("Pargam","No-cache");
        response.setHeader("Cache-Control","no-cache");
        response.setDateHeader("Expires",0);
        //生成验证码
        ArithmeticCaptcha arithmeticCaptcha = new ArithmeticCaptcha(130,32,3);
        redisTemplate.opsForValue().set("captcha:"+user.getId()+":"+goodsId,arithmeticCaptcha.text(),
                300, TimeUnit.SECONDS);
        try {
            arithmeticCaptcha.out(response.getOutputStream());
        } catch (IOException e) {
            log.error("验证码生成失败",e.getMessage());
        }
    }


    /**
     * 系统初始化，把商品库存加载到Redis
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> list = goodsService.findGoodsVo();
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        list.forEach(goodsVo ->{
            redisTemplate.opsForValue().set("seckillGoods:"+goodsVo.getId(),goodsVo.getStockCount());
            emptyStockMap.put(goodsVo.getId(), false);
            System.out.println("初始化完成");
        });
    }
}
