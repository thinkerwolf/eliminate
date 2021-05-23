# Name:     EliminateGame
# Time:     2020-05-20
FROM centosjdk:11

MAINTAINER Wukai <wukai213@gmail.com>

ENV TZ=Asia/Shanghai

RUN mkdir -p /app/eliminate-gateway
WORKDIR /app/eliminate-gateway

COPY target/eliminate-gateway-1.0-SNAPSHOT.jar /app/eliminate-gateway
COPY target/lib/* /app/eliminate-gateway/lib/
COPY sbin/* /app/eliminate-gateway/sbin/
COPY conf/* /app/eliminate-gateway/conf/

EXPOSE 81

CMD ["/bin/bash", "sbin/startup.sh", "start-foreground"]