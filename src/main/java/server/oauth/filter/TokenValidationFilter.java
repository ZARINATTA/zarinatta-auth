package server.oauth.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import server.oauth.auth.JwtService;
import server.oauth.exception.ZarinattaException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class TokenValidationFilter implements Filter {

    private final JwtService jwtService;
    private List<String> excludeUrls = new ArrayList<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 필터 초기화 작업이 필요하면 여기에 작성
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ZarinattaException, IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if(excludeUrls.contains(httpRequest.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        // 쿠키에서 accessToken 가져오기
        Cookie[] cookies = httpRequest.getCookies();
        String accessToken = null;

        if (cookies != null) {
            accessToken = Arrays.stream(cookies)
                    .filter(cookie -> "skt".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }

        // 토큰이 존재하면 유효성 검사 수행
        if (accessToken != null && jwtService.isValidToken(accessToken)) {
            String userId = jwtService.decodeAccessToken(accessToken);

            // 유효한 토큰인 경우 요청을 계속 처리
            if (userId != null) {
                // TODO: 이렇게 userId를 request에 넣어줘도 되는건지 좀 생각해봐야할듯
                httpRequest.setAttribute("accessToken", accessToken);
                httpRequest.setAttribute("userId", userId);
                return;
            }
        }

        // 유효하지 않은 토큰인 경우 401 에러 반환
        httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid access token");
    }

    public void setExcludeUrls(List<String> excludeUrls) {
        this.excludeUrls = excludeUrls;
    }
}
