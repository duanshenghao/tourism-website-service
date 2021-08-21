FROM openjdk:8-jre
MAINTAINER Shenghao Duan<duanshenghao@eastbabel.cn>
WORKDIR /webapps/tourism-website-service
COPY target/tourism-website-service*.jar /webapps/tourism-website-service/tourism-website-service.jar
ENTRYPOINT ["java", "-jar", "/webapps/tourism-website-service/tourism-website-service.jar"]