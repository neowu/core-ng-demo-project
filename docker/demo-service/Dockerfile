FROM        openjdk:11-jre-slim
LABEL       app=demo-service
RUN         addgroup --system app && adduser --system --no-create-home --ingroup app app
USER        app
ADD         file/demo-service.tar    /opt
CMD         ["/opt/demo-service/bin/demo-service"]
