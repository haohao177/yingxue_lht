package com.baizhi.lht.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;

@Aspect
@Configuration
public class CacheAspet {

    @Resource
    RedisTemplate redisTemplate;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    //加入缓存
    @Around("@annotation(com.baizhi.lht.annotation.AddCache)")
    public Object addCache(ProceedingJoinPoint proceedingJoinPoint) {
        System.out.println("=====进入环绕通知=====");
        //序列化解决乱码
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);

        StringBuilder stringBuilder = new StringBuilder();

        //获取类权限定名
        String className = proceedingJoinPoint.getTarget().getClass().getName();

        //获取方法名
        String methodName = proceedingJoinPoint.getSignature().getName();

        //拼接方法名
        stringBuilder.append(methodName);

        //获取实参
        Object[] args = proceedingJoinPoint.getArgs();
        for (Object arg : args) {
            stringBuilder.append(arg);//拼接实参
        }

        //获取拼接好的key
        String key = stringBuilder.toString();
        HashOperations hash = redisTemplate.opsForHash();

        //判断key是否存在
        Boolean aBoolean = hash.hasKey(className, key);
        Object result = null;
        if (aBoolean) {
            //存在有缓存 取出 直接使用
            result = hash.get(className, key);
        } else {
            //不存在  没缓存
            //放行  继续执行 直到返回结果

            try {
                result = proceedingJoinPoint.proceed();
                //查询结果放入缓存
                hash.put(className, key, result);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

        }
        return result;
    }

    //消除缓存
    //后置通知
    @After("@annotation(com.baizhi.lht.annotation.DelCache)")
    public void DelCache(JoinPoint joinPoint) {
        System.out.println("==========后置通知============");
        //根据类权限的名找到大key

        //获取类的权限定名
        String className = joinPoint.getTarget().getClass().getName();

        //通过大key删除该类下的所有缓存
        stringRedisTemplate.delete(className);
    }


}
