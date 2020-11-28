# Contributing

## Compiling

```bash
mvn clean install
```

This will build the plugin into a jar file into `sonar-zap-plugin/target/sonar-zap-plugin-<version>.jar`.

If you set the `docker` property, a Docker image will also be created for testing.
The image will be named `org.sonarsource.owasp/sonar-zap-plugin:<version>` and
will have the `lts` version of SonarQube pulled from Docker Hub with the
newly-built `zap-sonar-plugin` installed.

To always create the Docker image when building locally, you can set the docker
property in an active profile in your `settings.xml`:

```xml
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

## Testing

Once you build the Docker image, you can start it with

```bash
docker run -d --name sonarqube -p 9000:9000 -p 9092:9092 org.sonarsource.owasp/sonar-zap-plugin:version
```

The SonarQube server may take a few minutes to start. You can check the start
process by watching the logs:

```bash
docker logs sonarqube
```

Look for a line that says `SonarQube is up`.

Then run an analysis using the test report:

```bash
mvn sonar:sonar
```

The ZAP results can be viewed at
<http://localhost:9000/project/issues?id=org.sonarsource.owasp%3Asonar-zap-plugin&resolved=false&tags=zaproxy>.
There should be 14 issues: 1 Major, 9 Minor, 4 Info.

## Releasing

You will need a valid code signing key registered with OSSRH.

Once you push the changes, all checks come back clean, and you update the
version number for release, then merge the pull request into `main`.

```bash
git checkout main
mvn versions:set -DnewVersion='2.0.3'
git add pom.xml
git commit -m 'Releasing to Central Repository'
mvn clean deploy -P release
git tag -a sonar-zap-plugin-2.0.3 -m 'Support for SonarQube 7.9 LTS'
mvn versions:set -DnewVersion=2.0.4-SNAPSHOT
git add pom.xml
git commit -m 'Preparing for next development version'
git push origin
git push --tags origin
```
