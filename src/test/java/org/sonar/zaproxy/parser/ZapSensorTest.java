package org.sonar.zaproxy.parser;

/*-
 * #%L
 * ZAP Plugin for SonarQube
 * %%
 * Copyright (C) 2015 - 2020 Gene Gotimer <eugene.gotimer@steampunk.com>
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.rule.Rules;
import org.sonar.api.scan.filesystem.PathResolver;
import org.sonar.zaproxy.ZapSensor;
import org.sonar.zaproxy.ZapSensorConfiguration;

public class ZapSensorTest {

  private ZapSensor sensor;

  public ZapSensorTest() {
    final ZapSensorConfiguration configuration = mock(ZapSensorConfiguration.class);
    final FileSystem fileSystem = mock(FileSystem.class);
    final PathResolver pathResolver = mock(PathResolver.class);
    final Rules rules = mock(Rules.class);
    this.sensor = new ZapSensor(configuration, fileSystem, pathResolver, rules);
  }

  @Test
  public void toStringTest() {
    assertThat(this.sensor.toString()).isEqualTo("OWASP Zed Attack Proxy");
  }
}
