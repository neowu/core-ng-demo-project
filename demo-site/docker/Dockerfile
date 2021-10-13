FROM        neowu/jre:17
LABEL       app=demo-site
# install /lib/ld-linux-x86-64.so.2 for native lib, e.g. kafka/snappy
RUN         apk add --no-cache gcompat
RUN         addgroup --system app && adduser --system --no-create-home --ingroup app app
USER        app
# demo of creating layered docker image
COPY        package/dependency     /opt/app
COPY        package/app            /opt/app
CMD         ["/opt/app/bin/demo-site"]
