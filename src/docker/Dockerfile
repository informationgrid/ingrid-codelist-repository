FROM docker-registry.wemove.com/ingrid-java
ENV INGRID_USER=root
ENV RUN_DIRECTLY=true

ADD ingrid-codelist-*-installer.jar /

RUN unzip ingrid-*.jar -d /tmp \
    && mkdir -p /opt/ingrid/ingrid-codelist-repository \
    && cp -R /tmp/ingrid-codelist-repository-*/* /opt/ingrid/ingrid-codelist-repository/ \
    && sed -i 's/@SERVER_PORT@/80/' /opt/ingrid/ingrid-codelist-repository/start.sh \
    && rm -Rf /tmp/* \
    && rm ingrid-*.jar

WORKDIR /opt/ingrid/ingrid-codelist-repository
EXPOSE 80

CMD /bin/sh start.sh start