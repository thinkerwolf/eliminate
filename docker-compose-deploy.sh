#!/usr/bin/env bash
# docker-compose 打包脚本
DIR="${BASH_SOURCE-$0}"
DIR="$(dirname "${DIR}")"
echo "DIR=$DIR"

# 设置MVN命令
if [[ -n "$MVN_HOME" ]] && [[ -x "$MVN_HOME/bin/mvn" ]];  then
    MVN="$MVN_HOME/bin/mvn"
elif type -p java; then
    MVN=mvn
else
    echo "Error: MVN_HOME is not set and mvn could not be found in PATH." 1>&2
    exit 1
fi

# 设置Docker命令
if [[ -n "/usr/bin" ]] && [[ -x "/usr/bin/docker" ]]; then
    DOCKER="/usr/bin/docker"
elif type -p docker; then
    DOCKER=docker
else
    echo "Error: DOCKER is not set." 1>&2
    exit 1
fi

# 设置Docker Compose命令
if [[ -n "/usr/bin" ]] && [[ -x "/usr/bin/docker-compose" ]]; then
    DOCKER_COMPOSE="/usr/bin/docker-compose"
elif type -p docker; then
    DOCKER_COMPOSE=docker-compose
else
    echo "Error: DOCKER is not set." 1>&2
    exit 1
fi

# 设置SVN
SVN=svn

# svn update
echo "1.SVN Update....................."
$SVN update

# mvn install
echo "2.Maven clean and install ..................."
$MVN -f "$DIR/" clean
$MVN -f "$DIR/" install

# docker compose down
echo "3.Docker compose down ..................."
$DOCKER_COMPOSE -f "$DIR/eliminate-game/docker-compose.yml" down
$DOCKER_COMPOSE -f "$DIR/eliminate-login/docker-compose.yml" down
$DOCKER_COMPOSE -f "$DIR/eliminate-gateway/docker-compose.yml" down
$DOCKER_COMPOSE -f "$DIR/eliminate-chat/docker-compose.yml" down

$DOCKER_COMPOSE -f "$DIR/eliminate-game/docker-compose.yml" rm
$DOCKER_COMPOSE -f "$DIR/eliminate-login/docker-compose.yml" rm
$DOCKER_COMPOSE -f "$DIR/eliminate-gateway/docker-compose.yml" rm
$DOCKER_COMPOSE -f "$DIR/eliminate-chat/docker-compose.yml" rm

$DOCKER rmi "eliminate_game:1.0"
$DOCKER rmi "eliminate_login:1.0"
$DOCKER rmi "eliminate_gateway:1.0"
$DOCKER rmi "eliminate_chat:1.0"

# docker compose up
echo "4.Docker compose up ..................."
$DOCKER_COMPOSE -f "$DIR/eliminate-game/docker-compose.yml" up -d
echo "Sleep for a while .........................."
sleep 4
$DOCKER_COMPOSE -f "$DIR/eliminate-login/docker-compose.yml" up -d
echo "Sleep for a while .........................."
sleep 3
$DOCKER_COMPOSE -f "$DIR/eliminate-gateway/docker-compose.yml" up -d
echo "Sleep for a while .........................."
sleep 3
$DOCKER_COMPOSE -f "$DIR/eliminate-chat/docker-compose.yml" up -d