package oauth.config;

import oauth.auth.JwtService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<TokenValidationFilter> accessTokenValidationFilter(JwtService jwtService) {
        FilterRegistrationBean<TokenValidationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TokenValidationFilter(jwtService));
        registrationBean.addUrlPatterns("/users/*"); // 필터를 적용할 URL 패턴 설정
        return registrationBean;
    }
}

