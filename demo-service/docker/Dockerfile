FROM        neowu/jre:17.0.5
LABEL       app=demo-service
RUN         addgroup --gid 1000 --system app && adduser --system --no-create-home --ingroup app --uid 1000 app
USER        1000
# demo of creating layered docker image
COPY        package/dependency     /opt/app
COPY        package/app            /opt/app
CMD         ["/opt/app/bin/demo-service"]
