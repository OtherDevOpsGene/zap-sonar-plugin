ZAP Plugin for SonarQube
=====================================

Integrates [OWASP ZAP] reports into SonarQube v5.1 or higher.

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


Compiling
-------------------

> $ mvn clean package

Installation
-------------------
Copy the plugin (jar file) to $SONAR_INSTALL_DIR/extensions/plugins and restart SonarQube.

Plugin Configuration
-------------------
A typical SonarQube configuraiton will have the following parameters. This example assumes the use of a Jenkins workspace, but can easily be altered for other CI/CD systems.

```ini
sonar.sources=${WORKSPACE}
sonar.zaproxy.reportPath=${WORKSPACE}/zaproxy-report.xml
# Optional - specifies additional rules outside of what's included in the core
sonar.zaproxy.rulesFilePath=${WORKSPACE}/myrules.xml
```

This example may be less than ideal as the source directory generally will not be the root of the project workspace, but rather a dedicated 'src' directory. It is important to note that from SonarQubes perspective, zaproxy-report.xml is a source file and will need to be included in sonar.sources. Multiple source directories can be specified by this properly.

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
