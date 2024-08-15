### ⭐ Kakao Oauth 구현

### ⭐ Redis pub/sub 구현
- [Redis pub/sub 구조 구현](https://lucas-owner.tistory.com/68)
- [정리 노션 (작성중)](https://www.notion.so/Redis-pub-sub-238edab2312a4916a45fa2e7a8f3b1aa)

### API 명세 (임시)
```
- GET '/auth/redirect' (singup api 동작 전 필수로 실행되어야 함)

- GET '/auth/signup' (param -> code={코드})

- POST '/auth/login' (body - accessToken)

- POST '/auth/logout' (쿠키)

- POST '/auth/authorize' (쿠키, body - refreshToken) 

- POST '/users' (body - userEmail, userNick)

- POST '/users/update' (body - userDeviceToken, userPhone / 둘 모두 nullable)
```
