package com.lph.seckill;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class SeckillApplicationTests {

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private RedisScript<Boolean> script;

	@Test
	public void testLock01() {
		ValueOperations valueOperations = redisTemplate.opsForValue();
		//占位，如果key不存在才设置成功
		Boolean isLock = valueOperations.setIfAbsent("k1", "v1");
		//如果占位成功，进行正常操作
		if(isLock){
			valueOperations.set("name","xxxx");
			String name = (String) valueOperations.get("name");
			System.out.println(name);
			//操作结束，删除锁
			redisTemplate.delete("k1");
		}else {
			System.out.println("有线程在使用，请稍后再试");
		}
	}

	@Test
	public void testLock02() {
		ValueOperations valueOperations = redisTemplate.opsForValue();
		//占位，如果key不存在才设置成功
		//给锁添加一个过期时间，防止应用在运行过程中抛异常导致锁无法正常释放
		Boolean isLock = valueOperations.setIfAbsent("k1", "v1",5, TimeUnit.SECONDS);
		//如果占位成功，进行正常操作
		if(isLock){
			valueOperations.set("name","xxxx");
			String name = (String) valueOperations.get("name");
			System.out.println(name);
			//操作结束，删除锁
			redisTemplate.delete("k1");
		}else {
			System.out.println("有线程在使用，请稍后再试");
		}
	}


	@Test
	public void testLock03() {
		ValueOperations valueOperations = redisTemplate.opsForValue();
		//占位，如果key不存在才设置成功
		//给锁添加一个过期时间，防止应用在运行过程中抛异常导致锁无法正常释放
		String value = UUID.randomUUID().toString();
		Boolean isLock = valueOperations.setIfAbsent("k1", value,120, TimeUnit.SECONDS);
		//如果占位成功，进行正常操作
		if(isLock){
			valueOperations.set("name","xxxx");
			String name = (String) valueOperations.get("name");
			System.out.println("name="+name);
			valueOperations.get("k1");
			System.out.println(valueOperations.get("k1"));
			Boolean result = (Boolean) redisTemplate.execute(script, Collections.singletonList("k1"), value);
			//操作结束，删除锁
			System.out.println(result);
//			redisTemplate.delete("k1");
		}else {
			System.out.println("有线程在使用，请稍后再试");
		}
	}

	@Test
	public void test(){

	}
}
