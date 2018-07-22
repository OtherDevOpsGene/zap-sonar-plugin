/*
 * ZAP Plugin for SonarQube
 * Copyright (C) 2015-2017 Gene Gotimer
 * gene.gotimer@coveros.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.zaproxy.parser;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.rule.Rules;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.batch.sensor.issue.internal.DefaultIssueLocation;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.issue.Issuable.IssueBuilder;
import org.sonar.api.issue.Issue;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.scan.filesystem.PathResolver;
import org.sonar.zaproxy.ZapSensor;
import org.sonar.zaproxy.ZapSensorConfiguration;

public class ZapSensorTest {

  private ZapSensorConfiguration configuration;
  private ResourcePerspectives resourcePerspectives;
  private FileSystem fileSystem;
  private PathResolver pathResolver;
  private Rules rules;
  private ZapSensor sensor;

  @Before
  public void init() {
    this.configuration = mock(ZapSensorConfiguration.class);
    this.resourcePerspectives = mock(ResourcePerspectives.class);
    this.fileSystem = mock(FileSystem.class);
    this.pathResolver = mock(PathResolver.class);
    this.rules = mock(Rules.class);
    this.sensor = new ZapSensor(
        this.configuration,
        this.fileSystem,
        this.pathResolver,
        this.rules);
  }


  @Test
  public void toStringTest() {
    assertThat(this.sensor.toString()).isEqualTo("OWASP Zed Attack Proxy");
  }

  @Test
  public void shouldAnalyse() throws URISyntaxException {
    // todo: Once the Sensor is capable of working properly, populate this unit test.
  }

  private class MockIssueBuilder implements IssueBuilder {

    private RuleKey ruleKey;
    private Integer line;
    private String message;
    private String severity;

    private List<NewIssueLocation> issueLocation = new ArrayList<>();

    @Override
    public IssueBuilder ruleKey(RuleKey ruleKey) {
      this.ruleKey = ruleKey;
      return this;
    }

    @Override
    public IssueBuilder line(Integer line) {
      this.line = line;
      return this;
    }

    @Override
    public IssueBuilder message(String message) {
      this.message = message;
      return this;
    }

    @Override
    public IssueBuilder severity(String severity) {
      this.severity = severity;
      return this;
    }

    @Override
    public IssueBuilder reporter(String reporter) {
      return this;
    }

    @Override
    public IssueBuilder effortToFix(Double d) {
      return this;
    }

    @Override
    public IssueBuilder attribute(String key, String value) {
      return this;
    }

    @Override
    public Issue build() {
      return null;
    }

    @Override
    public NewIssueLocation newLocation() {
      return new DefaultIssueLocation();
    }

    @Override
    public IssueBuilder at(NewIssueLocation newIssueLocation) {
      issueLocation.add(newIssueLocation);
      return this;
    }

    @Override
    public IssueBuilder addLocation(NewIssueLocation newIssueLocation) {
      issueLocation.add(newIssueLocation);
      return this;
    }

    @Override
    public IssueBuilder addFlow(Iterable<NewIssueLocation> iterable) {
      for (NewIssueLocation location : iterable) {
        issueLocation.add(location);
      }
      return this;
    }

  }
}
