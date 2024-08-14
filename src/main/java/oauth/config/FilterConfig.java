package oauth.config;

import oauth.auth.JwtService;
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
        filter.setExcludeUrls(Arrays.asList("/auth/redirect", "/auth/signup"));

        return registrationBean;
    }
}

