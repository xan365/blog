package com.bamboo.blog.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.persistence.criteria.Join;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
public class LogAspect {

    //获取日志记录器
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * execution里的内容来标明拦截哪些类
     * */
    @Pointcut("execution(* com.bamboo.blog.web.*.*(..))")
    public void log() {}

    @Before("log()")
    /**在web包下面的方法（切点）执行前执行 */
    public void deBefore(JoinPoint joinPoint) {
        /**
         * 在doBefore中创建一个 RequestLog 对象，并赋值
         * 其中，url和ip可以通过 HttpServletRequest 获取
         * 而方法名和对象数组需要通过切面来获取，所以需要传递一个joinPoint对象
         * */
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String url = request.getRequestURL().toString();
        String ip = request.getRemoteAddr();
        //获取所拦截的类名+方法名
        String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        RequestLog requestLog = new RequestLog(url, ip, classMethod, args);
        /** 将所创建的对象传递给日志记录器*/
        logger.info("Request : {}", requestLog);
    }

    @After("log()")
    /** 在web包下面的方法（切点）执行后执行 */
    public void deAfter() {
        logger.info("--------do after-----");
    }

    /** 在doAfter()执行后执行， 并绑定参数来捕获返回值 */
    @AfterReturning(returning = "result", pointcut = "log()")
    public void doAfterReturn(Object result) {
        logger.info("result : {}", result);
    }

    private class RequestLog {
        private String url;
        private String ip;
        private String classMethod;
        private Object[] args;

        public RequestLog(String url, String ip, String classMethod, Object[] args) {
            this.url = url;
            this.ip = ip;
            this.classMethod = classMethod;
            this.args = args;
        }

        //记录成日志后帮我们转换成字符串
        @Override
        public String toString() {
            return "{" +
                    "url='" + url + '\'' +
                    ", ip='" + ip + '\'' +
                    ", classMethod='" + classMethod + '\'' +
                    ", args=" + Arrays.toString(args) +
                    '}';
        }
    }

}
