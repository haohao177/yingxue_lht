package com.baizhi.lht.aspect;

import com.baizhi.lht.annotation.AddLog;
import com.baizhi.lht.entity.Admin;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Date;

@Aspect
@Configuration
public class LogAspet {
    //依赖注入
    @Resource
    HttpSession session;

    @Around("@annotation(com.baizhi.lht.annotation.AddLog)")
    public Object addLog(ProceedingJoinPoint joinPoint) {

        //管理名字   时间  什么操作   是否成功

        //获取管理员用户名
        Admin admin = (Admin) session.getAttribute("admin");

        //获取访问时间
        Date date = new Date();

        //获取操作方法名

        String methodNmae = joinPoint.getSignature().getName();


        //获取方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        //获取方法上的注解
        AddLog addLog = method.getAnnotation(AddLog.class);

        //获取注解中属性的值
        String description = addLog.description();


        Object proceed = null;
        String message = null;
        try {
            proceed = joinPoint.proceed();

            System.out.println("成功");
            message = "success";


        } catch (Throwable throwable) {
            throwable.printStackTrace();
            System.out.println("失败");
            message = "Error";
        }
        System.out.println("管理员=" + admin.getUsername() + "时间=" + date + "方法名和介绍=" + methodNmae + "(" + description + ")" + "是否成功");
        return proceed;
    }


}
