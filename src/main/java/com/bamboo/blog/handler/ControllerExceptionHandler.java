package com.bamboo.blog.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 拦截异常，使其跳转到自定义错误页面error
 */

@ControllerAdvice
public class ControllerExceptionHandler {

    //声明对象
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /** 标注下面的方法是用来处理异常的，加上注解才有效，传参表示只要是exception级别的异常都可以*/
    @ExceptionHandler(Exception.class)
    /**
     * ModelandView就是返回一个页面，且页面中包含一些后台输出到前端的信息
     * 接受的参数request来获取是访问的哪一个路径url出现了异常
     * 参数e把异常通过参数的方式传递过来
     * */
    public ModelAndView exceptionHandler(HttpServletRequest request, Exception e) throws Exception {
        //记录日常信息并打印在控制台
        logger.error("Request URL : {}, Exception : {}", request.getRequestURL(), e);

        //让springboot处理404等异常
        if(AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }

        ModelAndView mv = new ModelAndView();
        mv.addObject("url", request.getRequestURL());
        mv.addObject("exception", e);
        //设置返回哪个页面：error/error.html
        mv.setViewName("error/error");
        return mv;
    }
}
