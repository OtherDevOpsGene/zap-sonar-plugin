# ZAP Plugin for SonarQube

[![Build Status](https://travis-ci.org/OtherDevOpsGene/zap-sonar-plugin.svg?branch=main)](https://travis-ci.org/OtherDevOpsGene/zap-sonar-plugin)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/6f379a37e7ef4994b5443723ec4bcb9f)](https://www.codacy.com/gh/OtherDevOpsGene/zap-sonar-plugin/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=OtherDevOpsGene/zap-sonar-plugin&amp;utm_campaign=Badge_Grade)
[![Maintainability](https://api.codeclimate.com/v1/badges/c6ac1a0ad4a4bba3b9f0/maintainability)](https://codeclimate.com/github/OtherDevOpsGene/zap-sonar-plugin/maintainability)
[![DepShield Badge](https://depshield.sonatype.org/badges/OtherDevOpsGene/zap-sonar-plugin/depshield.svg)](https://depshield.github.io)
[![Known Vulnerabilities](https://snyk.io/test/github/OtherDevOpsGene/zap-sonar-plugin/badge.svg)](https://snyk.io/test/github/OtherDevOpsGene/zap-sonar-plugin)
[![deepcode](https://www.deepcode.ai/api/gh/badge?key=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJwbGF0Zm9ybTEiOiJnaCIsIm93bmVyMSI6Ik90aGVyRGV2T3BzR2VuZSIsInJlcG8xIjoiemFwLXNvbmFyLXBsdWdpbiIsImluY2x1ZGVMaW50IjpmYWxzZSwiYXV0aG9ySWQiOjI1MDYzLCJpYXQiOjE2MDU4ODgyMzB9.5bi9hovkUE-DkAOLt0IUZy_CJTeJH1LFiRrqK_REoW0)](https://www.deepcode.ai/app/gh/OtherDevOpsGene/zap-sonar-plugin/_/dashboard?utm_content=gh%2FOtherDevOpsGene%2Fzap-sonar-plugin)

Integrates [OWASP ZAP](https://www.owasp.org/index.php/OWASP_Zed_Attack_Proxy_Project)
reports into SonarQube 7.9.6 LTS or higher.
The current LTS version of SonarQube is the target.

## About ZAP

OWASP Zed Attack Proxy (ZAP) is an easy to use integrated penetration testing
tool for finding vulnerabilities in web applications.

People with a wide range of security experience can use ZAP and making it ideal
for developers and functional testers new to penetration testing.

ZAP provides automated scanners as well as a set of tools that allow you to find
security vulnerabilities manually.

## Installation

Copy the plugin (jar file) to `$SONAR_INSTALL_DIR/extensions/plugins` and
restart SonarQube.

## Plugin Configuration

A typical SonarQube configuration will have the following parameters. This
example assumes the use of a Jenkins workspace, but can easily be altered for
other CI/CD systems.

```ini
sonar.zaproxy.reportPath=${WORKSPACE}/zaproxy-report.xml
sonar.zaproxy.htmlReportPath=${WORKSPACE}/zaproxy-htmlReport.html
# Optional - specifies additional rules outside of what's included in the core
sonar.zaproxy.rulesFilePath=${WORKSPACE}/myrules.xml
```

## Example of automation with a CI toolchain

```sh
cd example

docker-compose up -d sonarqube
sleep 120
# wait 2 minute for sonarqbue to start

export PLUGIN_VERSION=2.2.0
wget https://github.com/Coveros/zap-sonar-plugin/releases/download/sonar-zap-plugin-${PLUGIN_VERSION}/sonar-zap-plugin-${PLUGIN_VERSION}.jar -O ./plugin/sonar-zap-plugin-${PLUGIN_VERSION}.jar

export APP_URL_UNDER_TEST='your-url-under-test'
docker-compose up owasp-zap
docker-compose up sonar-scanner
```

> If you wish to run the zap tool within the CI pipeline:
> -   you may refet to the [example](example)
> -   You need to have docker and docker-compose installed
> -   You may refer to [.gitlab-ci.yml](example/.gitlab-ci.yml) if you wish to run on Gitlab CI

## History

The ZAP SonarQube Plugin is derived from the
[OWASP Dependency-Check SonarQube Plugin](https://github.com/stevespringett/dependency-check-sonar-plugin).
Version 1.0 of the Dependency-Check plugin was [forked](https://github.com/polymont/dependency-check-sonar-plugin)
by @polymont with the intent of creating a generic OWASP SonarQube plugin to
support any OWASP project. The ZAP team wanted their own SonarQube plugin
independent of any other project. In addition, a number of critical defects
were discovered in the initial release of the Dependency-Check SonarQube plugin
that were later fixed in subsequent releases, but never addressed in the generic
OWASP version. The ZAP SonarQube Plugin is based on v1.0.3 of
the Dependency-Check SonarQube plugin with ZAP-specific contributions by @polymont.

## License

Permission to modify and redistribute is granted under the terms of the
[LGPLv3](http://www.gnu.org/licenses/lgpl.txt) license.
