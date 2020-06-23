FROM        adoptopenjdk/openjdk14:alpine-jre
LABEL       app=demo-service
# RUN         apk add --no-cache fontconfig ttf-dejavu # example to add packages if needed
RUN         addgroup --system app && adduser --system --no-create-home --ingroup app app
USER        app
# demo of creating layered docker image
COPY        file/dependency   /opt/app
COPY        file/app   /opt/app
CMD         ["/opt/app/bin/demo-service"]
