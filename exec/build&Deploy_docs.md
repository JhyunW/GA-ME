A. 사용한 JVM, WAS 제품 등의 종류와 설정 값, 버전(IDE버전 포함) 기재

- JVM : JDK 17
- WAS : Tomcat
- Web Server : Nginx/1.18.0 (Ubuntu)
- IDE
  - Frontend : VScode (1.86.1)
  - Backed : Intellij IDEA (2023.2.5)

B. 빌드 시 사용되는 환경변수 등의 내용 상세 기재

- application-env.yml (spring boot)

```
DB_USERNAME: root
DB_URL: jdbc:mysql://j10e105.p.ssafy.io:3306/ggame
DB_PASSWORD: GGAME!buk105
SERVER_PORT: 8000
OAUTH_CLIENT_ID: 101986713723-q17fdcl43u2atuf4ce62gqcjp4hvn4l1.apps.googleusercontent.com
OAUTH_CLIENT_SECRET: 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
OAUTH_CLIENT_SCOPE: profile, email
OAUTH_URL: https://j10e105.p.ssafy.io/api/auth/google/callback
CASSANDRA_KEYSPACE: ggame
CASSANDRA_CONTACT_POINTS: j10e105.p.ssafy.io
CASSANDRA_PORT: 9042
CHROME_DRIVER_PATH: src/main/resources/chromedriver-win64/chromedriver.exe

```

C. 배포 시 특이사항 기재

- Nginx를 활용해 리버스 프록시 구성
- publish over ssh를 활용해 빌드 후 SSH를 통해 원격 서버 배포 구현
- Jenkins & Airflow를 활용한 

D. DB 접속 정보 등 프로젝트(ERD)에 활용되는 주요 계정 및 프로퍼티가 정의된 파일 목록

 - 카카오톡 로그인
 - 모두 동일한 주요 계정으로 접근 가능