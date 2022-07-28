package com.lph.seckill.controller;


import com.lph.seckill.rabbitmq.MQSender;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LPH
 */
@Controller
@RequestMapping("/user")
public class UserController {

//    @Autowired
//    private MQSender mqSender;
//
//    /**
//     * 测试发送RabbitMQ消息
//     */
//    @RequestMapping("/mq")
//    @ResponseBody
//    public void mq(){
//        mqSender.send("hello");
//    }
//
//    /**
//     * fanout模式
//     */
//    @RequestMapping("/mq/fanout")
//    @ResponseBody
//    public void mq01(){
//        mqSender.send("hello");
//    }
//
//    /**
//     * direct模式
//     */
//    @RequestMapping("/mq/direct01")
//    @ResponseBody
//    public void mq02(){
//        mqSender.send01("hello,red");
//    }
//
//    /**
//     * direct模式
//     */
//    @RequestMapping("/mq/direct02")
//    @ResponseBody
//    public void mq03(){
//        mqSender.send02("hello,green");
//    }
//
//    /**
//     * topic模式
//     */
//    @RequestMapping("/mq/topic01")
//    @ResponseBody
//    public void mq04(){
//        mqSender.send03("hello,red");
//    }
//
//    /**
//     * topic模式
//     */
//    @RequestMapping("/mq/topic02")
//    @ResponseBody
//    public void mq05(){
//        mqSender.send04("hello,green");
//    }
//
//    /**
//     * headers模式
//     */
//    @RequestMapping("/mq/headers01")
//    @ResponseBody
//    public void mq06(){
//        mqSender.send05("hello,headers");
//    }
//
//    /**
//     * headers模式
//     */
//    @RequestMapping("/mq/headers02")
//    @ResponseBody
//    public void mq07(){
//        mqSender.send06("hello,headers");
//    }
}
