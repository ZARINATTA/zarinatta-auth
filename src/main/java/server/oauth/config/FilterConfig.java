package server.oauth.config;

import server.oauth.auth.JwtService;
import server.oauth.filter.TokenValidationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<TokenValidationFilter> accessTokenValidationFilter(JwtService jwtService) {
        FilterRegistrationBean<TokenValidationFilter> registrationBean = new FilterRegistrationBean<>();
        TokenValidationFilter filter = new TokenValidationFilter(jwtService);

        registrationBean.setFilter(filter);
        filter.setExcludeUrls(Arrays.asList("/auth/redirect", "/auth/signup", "/api/v1/ticket/search"));

        return registrationBean;
    }
}

