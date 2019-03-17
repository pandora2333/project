package com.dangdangnet.web.filter;

import com.dangdangnet.common.dto.util.JwtUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 通过网关验证，前后网关整合
 * @author pandora2333
 */
@Component
@Slf4j
public class WebFilter extends ZuulFilter {

    @Resource
    private JwtUtil jwtUtil;
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        //得到request上下文
        RequestContext currentContext = RequestContext.getCurrentContext();
        //得到request域
        HttpServletRequest request = currentContext.getRequest();
        if(request.getMethod().equals("OPTIONS")){
            return null;
        }

        //不需要验证的入口
        if(request.getRequestURI().indexOf("sign")>0||request.getRequestURI().indexOf("Reg")>0||request.getRequestURI().indexOf("show")>0){
            return null;
        }
        //得到头信息
        String header = request.getHeader("Authorization");
        //判断是否有头信息
        if (StringUtils.isNotBlank(header)) {//如果头部含有认证信息就对其进行解析
            if (header.startsWith("tgc=")) {
                //获得token令牌
                String token = header.substring(4);
                try {
                    Claims claims = jwtUtil.parseJWT(token);
                    String roles = (String) claims.get("role");
                    if (roles != null && (roles.equals("user")||roles.equals("admin"))) {
                        log.info("user login,the token :{}", token);
                        currentContext.addZuulRequestHeader("Authorization", header);
                        return  null;
                    }
                } catch (Exception e) {
                    log.info("user login failed,the token :{}", token);
                }
            }
        }
        currentContext.setSendZuulResponse(false);//终止运行
        currentContext.setResponseStatusCode(403);
        currentContext.setResponseBody("权限不足");
        currentContext.getResponse().setContentType("text/html;charset=utf-8");
        return null;
    }
}
