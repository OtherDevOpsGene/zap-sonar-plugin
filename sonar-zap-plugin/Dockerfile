ARG SONAR_VERSION
FROM sonarqube:${SONAR_VERSION}

ARG ARTIFACT
COPY target/${ARTIFACT} /opt/sonarqube/extensions/plugins/
