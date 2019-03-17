package com.dangdangnet.base.Interceptor;
import com.dangdangnet.common.dto.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT验证前置处理器
 * @author pandora2333
 */
@Component
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {
    @Resource
    JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if(StringUtils.isNotBlank(request.getParameter("claims_admin"))&&StringUtils.isNotBlank(request.getParameter("claims_admin_id"))) return true;
        String authc = request.getHeader("Authorization");
        if (StringUtils.isNotBlank(authc)) {//如果头部含有认证信息就对其进行解析
            if (authc.startsWith("tgc=")) {
                //获得token令牌
                String token = authc.substring(4);
                try {
                    Claims claims = jwtUtil.parseJWT(token);
                    String roles = (String) claims.get("role");
                    if (roles != null && roles.equals("admin")) {
                        log.info("admin login,the token :{}", token);
                        request.setAttribute("claims_admin",claims.get("subject"));
                        request.setAttribute("claims_admin_id",claims.get("id"));
                        return true;
                    }
                } catch (Exception e) {
                    log.info("admin jwt parse  faild ,the token :{}", token);
                }
            }
        }
        return false;
    }
}