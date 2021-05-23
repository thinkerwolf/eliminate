# Name:     EliminateGame
# Time:     2020-05-20
FROM centosjdk:11

MAINTAINER Wukai <wukai213@gmail.com>

ENV TZ=Asia/Shanghai

RUN mkdir -p /app/eliminate-chat
WORKDIR /app/eliminate-chat

COPY target/eliminate-chat-1.0-SNAPSHOT.jar /app/eliminate-chat
COPY target/lib/* /app/eliminate-chat/lib/
COPY sbin/* /app/eliminate-chat/sbin/
COPY conf/* /app/eliminate-chat/conf/

EXPOSE 83

CMD ["/bin/bash", "sbin/startup.sh", "start-foreground"]