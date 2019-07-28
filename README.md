[![Build Status](https://travis-ci.org/Coveros/zap-sonar-plugin.svg?branch=master)](https://travis-ci.org/Coveros/zap-sonar-plugin)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/ba00fc80c7424266b2dfda21d0a62ead)](https://www.codacy.com/app/gotimer/zap-sonar-plugin?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Coveros/zap-sonar-plugin&amp;utm_campaign=Badge_Grade)
[![Maintainability](https://api.codeclimate.com/v1/badges/c978770ba79f15c2b029/maintainability)](https://codeclimate.com/github/Coveros/zap-sonar-plugin/maintainability)
[![DepShield Badge](https://depshield.sonatype.org/badges/Coveros/zap-sonar-plugin/depshield.svg)](https://depshield.github.io)

ZAP Plugin for SonarQube 7.x
=====================================

Integrates [OWASP ZAP] reports into SonarQube v7.x or higher. The target version of SonarQube is the current LTS version.


About ZAP
-------------------
OWASP Zed Attack Proxy (ZAP) is an easy to use integrated penetration testing tool for finding vulnerabilities in web applications.

It is designed to be used by people with a wide range of security experience and as such is ideal for developers and functional testers who are new to penetration testing.

ZAP provides automated scanners as well as a set of tools that allow you to find security vulnerabilities manually.


Screenshots
-------------------

![alt tag](screenshots/dashboard-widget.png)


Metrics
-------------------

The plugin keeps track of the following statistics:

* Total number of high, medium, low, and info severity findings

Additionally, the following metric is defined:

__Identified Risk Score (IRS)__

(high * 5) + (medium * 3) + (low * 1)

The IRS is simply a weighted measurement of the vulnerabilities identified during
a scan. It does not measure the actual risk posed by the findings.


Installation
-------------------
Copy the plugin (jar file) to $SONAR_INSTALL_DIR/extensions/plugins and restart SonarQube.


Plugin Configuration
-------------------
A typical SonarQube configuration will have the following parameters. This example assumes the use of a Jenkins workspace, but can easily be altered for other CI/CD systems.

```ini
sonar.zaproxy.reportPath=${WORKSPACE}/zaproxy-report.xml
# Optional - specifies additional rules outside of what's included in the core
sonar.zaproxy.rulesFilePath=${WORKSPACE}/myrules.xml
```


Compiling
-------------------

    $ mvn clean package

This will build the plugin into a jar file into `sonar-zap-plugin/target/sonar-zap-plugin-<version>.jar`.

If the `docker` property is set, a Docker image will also be created for testing. The image will be named `org.sonarsource.owasp/sonar-zap-plugin:<version>` and
will have the supported version of SonarQube pulled from Docker Hub with the newly-built zap-sonar-plugin installed.

To make sure the Docker image is always created when building locally, you can set the docker property in an active profile in your `settings.xml`:

```Maven POM
    <settings>
    ...
      <profiles>
        <profile>
          <id>docker</id>
          <properties>
            <docker>true</docker>
          </properties>
        </profile>
      </profiles>

      <activeProfiles>
        <activeProfile>docker</activeProfile>
      </activeProfiles>
    ...
    </settings>
```


Testing
-------------------
Once the Docker image is built, it can be started with

    $ docker run -d --name sonarqube -p 9000:9000 -p 9092:9092 org.sonarsource.owasp/sonar-zap-plugin:version

The SonarQube server may take a few minutes to start. You can check it with

    $ docker logs sonarqube

and look for a line that says `SonarQube is up`.

Then run an analysis using the test report:

    $ cd sonar-zap-plugin
    $ mvn sonar:sonar -Dsonar.zaproxy.reportPath=$(pwd)/src/test/resources/report/zaproxy-report.xml

The path must be an absolute path. If your shell does not support `$(pwd)`, replace it with the full path to the test report.

The results can be viewed at <http://localhost:9000/project/issues?id=org.sonarsource.owasp%3Asonar-zap-plugin&resolved=false&tags=zaproxy>.
There should be 14 issues: 1 Major, 9 Minor, 4 Info.


Releasing
-------------------

You will need a valid code signing key registered with OSSRH, which @gotimer has now.

Once the changes are pushed, all checks come back clean, and the version number is updated for release, then merge the pull request into `master`.

On the `master` branch: 

    $ mvn clean deploy -P release
    $ git tag -a sonar-zap-plugin-1.2.0 -m 'Support for SonarQube 7.9 LTS'
    $ git push --tags origin


History
-------------------

The ZAP SonarQube Plugin is derived from the [OWASP Dependency-Check SonarQube Plugin]. Version 1.0 of the Dependency-Check plugin was [forked] by [Polymont] with the intent of creating a generic OWASP SonarQube plugin to support any OWASP project. The ZAP team wanted their own SonarQube plugin independent of any other project. In addition, a number of critical defects were discovered in the initial release of the Dependency-Check SonarQube plugin that were later fixed in subsequent releases, but never addressed in the generic OWASP version. The ZAP SonarQube Plugin is based on v1.0.3 of the Dependency-Check SonarQube plugin with ZAP-specific contributions by [Polymont].


License
-------------------

Permission to modify and redistribute is granted under the terms of the [LGPLv3] license.

  [LGPLv3]: http://www.gnu.org/licenses/lgpl.txt
  [OWASP ZAP]: https://www.owasp.org/index.php/OWASP_Zed_Attack_Proxy_Project
  [OWASP Dependency-Check SonarQube Plugin]: https://github.com/stevespringett/dependency-check-sonar-plugin
  [forked]: https://github.com/polymont/dependency-check-sonar-plugin
  [Polymont]: https://github.com/polymont
