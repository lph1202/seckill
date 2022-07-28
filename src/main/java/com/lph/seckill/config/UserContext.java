package com.lph.seckill.config;

import com.lph.seckill.pojo.User;

public class UserContext {
    private static ThreadLocal<User> userHolder= new ThreadLocal<User>();

    public static void setUser(User user){
        userHolder.set(user);
    }

    public static User getUser(){
        return userHolder.get();
    }
}
