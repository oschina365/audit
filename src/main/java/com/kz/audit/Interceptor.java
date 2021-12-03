package com.kz.audit;

import com.kz.audit.util.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class Interceptor implements HandlerInterceptor {

    /**
     * 在访问Controller某个方法之前这个方法会被调用。
     *
     * @param request
     * @param response
     * @param handler
     * @return false则表示不执行postHandle方法, true 表示执行postHandle方法
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = IpUtil.getIpAddress(request);
        log.info("visit ip {}", ip);
        //false则表示不执行postHandle方法,不执行下一步chain链，直接返回response
        return false;
    }

    /**
     * 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
     * preHandle方法处理之后这个方法会被调用，如果控制器Controller出现了异常，则不会执行此方法
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("Token Interceptor postHandle");
    }

    /**
     * 不管有没有异常，这个afterCompletion都会被调用
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("Token Interceptor afterCompletion");
    }

}
