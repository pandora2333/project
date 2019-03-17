package com.dangdangnet.goods.Interceptor;
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
        String authc = request.getHeader("Authorization");
        if (StringUtils.isNotBlank(authc)) {//如果头部含有认证信息就对其进行解析
            if (authc.startsWith("tgc=")) {
                //获得token令牌
                String token = authc.substring(4);
                try {
                    Claims claims = jwtUtil.parseJWT(token);
                    String roles = (String) claims.get("role");
                    if (roles != null && roles.equals("user")) {
                        log.info("user login,the token :{}", token);
                        log.info("claims_user:{}",claims.get("subject"));
                        log.info("claims_user_id:{}",claims.get("id"));
                        request.setAttribute("claims_user",claims.get("subject"));
                        request.setAttribute("claims_user_id",claims.get("id"));
                    }
                } catch (Exception e) {
                    log.info("user login failed ,the token :{}", token);
                }
            }
        }
        return true;
    }
}