FROM alpine AS builder

COPY ingrid-codelist-*-installer.jar /
RUN unzip -q ingrid-*-installer.jar -d /tmp \
    && export INSTALL_DIR=`ls -d1 /tmp/ingrid-*` \
    && sed -i 's/@SERVER_PORT@/8080/' $INSTALL_DIR/start.sh \
    && sed -i -r 's/(<AppenderRef.*\/>)/\1<AppenderRef ref="Console" \/>/g' $INSTALL_DIR/conf/log4j2.xml

FROM bellsoft/liberica-openjre-alpine:25
ENV INGRID_USER=ingrid
ENV RUN_DIRECTLY=true

# user, group ingrid (1000:1000) is created in base image
COPY --chown=1000:1000 --from=builder /tmp/ingrid-codelist* /opt/ingrid/ingrid-codelist-repository/

RUN addgroup --gid 1000 ingrid \
  && adduser --gecos "" --disabled-password --uid 1000 --ingroup "ingrid" ingrid \
  && chown -R ingrid:ingrid /opt/ingrid \
  && chmod -R ug+rwX /opt/ingrid

USER ingrid

WORKDIR /opt/ingrid/ingrid-codelist-repository
EXPOSE 8080

CMD /bin/sh start.sh start
