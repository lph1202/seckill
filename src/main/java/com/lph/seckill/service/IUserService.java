package com.lph.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lph.seckill.pojo.User;
import com.lph.seckill.vo.LoginVo;
import com.lph.seckill.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LPH
 * @since 2022-05-19
 */
public interface IUserService extends IService<User> {

    RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);

    /**
     * 根据ticket获取用户
     * @param userTicket
     * @return
     */
    User getUserByCookie(String userTicket,HttpServletRequest request,HttpServletResponse response);

    /**
     * 更新密码
     * @param userTicket
     * @param password
     * @param request
     * @param response
     * @return
     */
    RespBean updatePassword(String userTicket,String password,HttpServletRequest request,HttpServletResponse response);
}
