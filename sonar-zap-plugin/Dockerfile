ARG SONAR_VERSION
FROM sonarqube:${SONAR_VERSION}
LABEL maintainer="gene.gotimer@coveros.com"

ARG ARTIFACT
COPY target/${ARTIFACT} /opt/sonarqube/extensions/plugins/
