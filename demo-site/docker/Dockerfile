FROM        neowu/jre:17.0.2
LABEL       app=demo-site
RUN         addgroup --system app && adduser --system --no-create-home --ingroup app app
USER        app
# demo of creating layered docker image
COPY        package/dependency     /opt/app
COPY        package/app            /opt/app
CMD         ["/opt/app/bin/demo-site"]
