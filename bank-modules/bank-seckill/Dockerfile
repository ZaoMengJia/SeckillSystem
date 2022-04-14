FROM centos
MAINTAINER orangeboy
ADD bank-seckill bank-seckill
ADD application.yml application.yml
ADD application-dev.yml application-dev.yml
ADD application-prod.yml application-prod.yml
VOLUME /tmp
EXPOSE 8099
ENTRYPOINT ["./bank-seckill"]