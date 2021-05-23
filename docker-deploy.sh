#!/usr/bin/env bash
# 一键打包部署脚本
DIR="${BASH_SOURCE-$0}"
DIR="$(dirname "${DIR}")"

echo "DIR=$DIR"

# 设置MVN命令
if [[ -n "$MVN_HOME" ]] && [[ -x "$MVN_HOME/bin/java" ]];  then
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

# 设置SVN
SVN=svn

# svn update
echo "1.SVN Update....................."
$SVN update

# mvn install pub
echo "2.Maven clean and install ..................."
$MVN -f "$DIR/" clean
$MVN -f "$DIR/" install

# docker
GAME_IMAGE_NAME="eliminate-game:1.0"
GAME_CONTAINER_NAME="EliminateGame"

echo "3.Docker building................"
$DOCKER stop "$GAME_CONTAINER_NAME"
$DOCKER rm "$GAME_CONTAINER_NAME"
$DOCKER rmi "$GAME_IMAGE_NAME"

$DOCKER build -f "$DIR/eliminate-game/Dockerfile" -t "$GAME_IMAGE_NAME" "$DIR/eliminate-game/"

echo "4.Docker running................."
$DOCKER run -p 0.0.0.0:80:80 -p 0.0.0.0:8787:8787 --name "$GAME_CONTAINER_NAME" -d "$GAME_IMAGE_NAME"