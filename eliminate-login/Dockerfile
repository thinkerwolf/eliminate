# Name:     EliminateGame
# Time:     2020-05-20
FROM centosjdk:11

MAINTAINER Wukai <wukai213@gmail.com>

ENV TZ=Asia/Shanghai

RUN mkdir -p /app/eliminate-login
WORKDIR /app/eliminate-login

COPY target/eliminate-login-1.0-SNAPSHOT.jar /app/eliminate-login
COPY target/lib/* /app/eliminate-login/lib/
COPY sbin/* /app/eliminate-login/sbin/
COPY conf/* /app/eliminate-login/conf/

EXPOSE 82

CMD ["/bin/bash", "sbin/startup.sh", "start-foreground"]