FROM java:8
VOLUME /logs
ADD system-center.jar /
EXPOSE 10010
ENV JAVA_OPTS="-Xmx128m -Xms64m -Xmn32m -Xss16m"
ENTRYPOINT java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /system-center.jar
MAINTAINER allenyll
#设置时区
RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone
