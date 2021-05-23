# Name: Centosjdk:8
# Time: 2020-05-20
FROM centos:7

MAINTAINER Wukai <wukai213@gmail.com>

RUN yum install -y java-1.8.0-openjdk.x86_64

ENV JAVA_HOME=/usr/lib/jvm/java-1.8.0
ENV CLASS_PATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
ENV PATH=$JAVA_HOME/bin:$PATH

CMD ["java", "-version"]