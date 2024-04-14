## 목차

[Nginx 설치](#nginx-설치)

[Nginx 설정 파일 구성](#nginx-defaultconf-설정)

[frontend 코드 배포](#frontend-빌드-후-ubuntu로-dist-폴더-이동)

## nginx 설치

```bash
sudo apt update
sudo apt install nginx  # nginx 설치
sudo ufw app list # 방화벽 리스트
sudo ufw allow 'Nginx HTTP' # 방화벽 설정 허용
sudo ufw status # 상태 체크
```

## nginx default.conf 설정

```
server {
        listen 80 default_server;
        listen [::]:80 default_server;

        root /home/ubuntu/jenkins/workspace/frontend/frontend/dist;
        index index.html index.htm;

        server_name j10e105.p.ssafy.io;

        location / {
                try_files $uri $uri/ =404;
        }
}
server {
        root /home/ubuntu/jenkins/workspace/frontend/frontend/dist;

        index index.html index.htm ;
        server_name j10e105.p.ssafy.io; # managed by Certbot
        include /etc/nginx/conf.d/service-url.inc;

        location / {
                # CSP 설정 추가
                add_header Content-Security-Policy "default-src 'self'; script-src 'self' 'unsafe-inline' https://apis.google.com https://developers.kakao.com https://t1.kakaocdn.net; style-src 'self' 'unsafe-inline'; font-src 'self' https://cdn.jsdelivr.net data:; img-src 'self' https://k.kakaocdn.net/ https://t1.kakaocdn.net https://cdn.akamai.steamstatic.com https://cdn.gamemeca.com data:; frame-src https://kauth.kakao.com https://kapi.kakao.com https://www.youtube.com; connect-src 'self' https://www.googleapis.com;";

                try_files $uri $uri/ /index.html;
        }

        location /api {
                proxy_pass http://j10e105.p.ssafy.io:8001;

                proxy_set_header X-Real_IP $remote_addr;
                proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_For;
                proxy_set_header X-Forwarded-Proto $scheme;
                proxy_set_header X-NginX-Proxy true;

        } 

    listen [::]:443 ssl ipv6only=on; # managed by Certbot
    listen 443 ssl; # managed by Certbot
    ssl_certificate /etc/letsencrypt/live/j10e105.p.ssafy.io/fullchain.pem; # managed by Certbot
    ssl_certificate_key /etc/letsencrypt/live/j10e105.p.ssafy.io/privkey.pem; # managed by Certbot
    include /etc/letsencrypt/options-ssl-nginx.conf; # managed by Certbot
    ssl_dhparam /etc/letsencrypt/ssl-dhparams.pem; # managed by Certbot

}
server {
    if ($host = j10e105.p.ssafy.io) {
        return 301 https://$host$request_uri;
    } # managed by Certbot

        listen 80 ;
        listen [::]:80 ;
    server_name j10e105.p.ssafy.io;
    return 404; # managed by Certbot
}
```

## frontend 빌드 후 ubuntu로 dist 폴더 이동

### 1. fronted build

```cmd
npm install
npm run build
```

### 2. Termius를 사용해 로컬 빌드 wordCloud 폴더를 ubuntu nginx root 경로로 이동

### 3. 도메인 접속하여 반영된 코드 확인
