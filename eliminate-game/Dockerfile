# Name:     EliminateGame
# Time:     2020-05-20
FROM centosjdk:11

MAINTAINER Wukai <wukai213@gmail.com>

ENV TZ=Asia/Shanghai

RUN mkdir -p /app/eliminate-game
WORKDIR /app/eliminate-game

COPY target/eliminate-game-1.0-SNAPSHOT.jar /app/eliminate-game
COPY target/lib/* /app/eliminate-game/lib/
COPY sbin/* /app/eliminate-game/sbin/
COPY conf/* /app/eliminate-game/conf/

EXPOSE 80

CMD ["/bin/bash", "sbin/startup.sh", "start-foreground"]