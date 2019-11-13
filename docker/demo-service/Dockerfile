FROM        adoptopenjdk/openjdk13:alpine-jre
LABEL       app=demo-service
# RUN         apk add --no-cache fontconfig ttf-dejavu # example to add packages if needed
RUN         addgroup --system app && adduser --system --no-create-home --ingroup app app
USER        app
ADD         file/demo-service.tar    /opt
CMD         ["/opt/demo-service/bin/demo-service"]
