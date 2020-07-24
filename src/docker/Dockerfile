FROM alpine AS builder

COPY ingrid-codelist-*-installer.jar /
RUN unzip ingrid-*-installer.jar -d /tmp \
    && export INSTALL_DIR=`ls -d1 /tmp/ingrid-*` \
    && sed -i 's/@SERVER_PORT@/8080/' $INSTALL_DIR/start.sh \
    && sed -i -r 's/(<AppenderRef.*\/>)/\1<AppenderRef ref="Console" \/>/g' $INSTALL_DIR/conf/log4j2.xml

FROM docker-registry.wemove.com/ingrid-java:jre-8-alpine
ENV INGRID_USER=ingrid
ENV RUN_DIRECTLY=true

# user, group ingrid (1000:1000) is created in base image
COPY --chown=1000:1000 --from=builder /tmp/ingrid-codelist* /opt/ingrid/ingrid-codelist-repository/

USER ingrid

WORKDIR /opt/ingrid/ingrid-codelist-repository
EXPOSE 8080

CMD /bin/sh start.sh start
