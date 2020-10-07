FROM        adoptopenjdk/openjdk15:alpine-jre
LABEL       app=demo-site
# RUN         apk add --no-cache fontconfig ttf-dejavu # example to add packages if needed
RUN         addgroup --system app && adduser --system --no-create-home --ingroup app app
USER        app
# demo of creating layered docker image
COPY        package/dependency     /opt/app
COPY        package/app            /opt/app
CMD         ["/opt/app/bin/demo-site"]
