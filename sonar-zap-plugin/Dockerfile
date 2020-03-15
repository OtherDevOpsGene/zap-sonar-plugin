ARG SONAR_VERSION
FROM sonarqube:${sonar.version}-community
LABEL maintainer="gene.gotimer@coveros.com"

ARG ARTIFACT
COPY target/${project.build.finalName}.jar /opt/sonarqube/extensions/plugins/
