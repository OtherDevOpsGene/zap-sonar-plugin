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
package org.sonar.zaproxy;

import static org.mockito.Mockito.mock;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.rule.Rules;
import org.sonar.api.scan.filesystem.PathResolver;
import org.sonar.zaproxy.parser.element.AlertItem;
import org.sonar.zaproxy.parser.element.Instance;

public class ZapSensorTest {

  private ZapSensor zapSensor;

  @Before
  public void setUp() {
    final ZapSensorConfiguration configuration = mock(ZapSensorConfiguration.class);
    final FileSystem fileSystem = mock(FileSystem.class);
    final PathResolver pathResolver = mock(PathResolver.class);
    final Rules rules = mock(Rules.class);

    this.zapSensor = new ZapSensor(configuration, fileSystem, pathResolver, rules);
  }
  
  @Test
  public void formatDescriptionNoInstances() {
    final AlertItem alertItem = createTestAlertItem(1);

    final String desc = zapSensor.formatDescription(alertItem);

    assertEquals("URI: http://example.org/alertItem1 | Param: param1 | Attack: attack1 | "
        + "Evidence: evidence1Method: method1 | Confidence: 1 | Description: desc1 | ", desc);
  }

  @Test
  public void formatDescriptionWithInstances() {
    final Instance instance4 = createTestInstance(4);
    final Instance instance5 = createTestInstance(5);

    final AlertItem alertItem = createTestAlertItem(3);
    alertItem.addInstance(instance4);
    alertItem.addInstance(instance5);

    final String desc = zapSensor.formatDescription(alertItem);

    assertEquals("URI: http://example.org/instance4 | Method: method4 | Param: param4 | "
        + "Attack: method4 | Evidence: evidence4 | "
        + "URI: http://example.org/instance5 | Method: method5 | Param: param5 | "
        + "Attack: method5 | Evidence: evidence5 | "
        + "Confidence: 3 | Description: desc3 | ", desc);
  }

  private AlertItem createTestAlertItem(final Integer id) {
    final AlertItem alertItem = new AlertItem();
    alertItem.setUri("http://example.org/alertItem" + id);
    alertItem.setParam("param" + id);
    alertItem.setAttack("attack" + id);
    alertItem.setEvidence("evidence" + id);
    alertItem.setMethod("method" + id);
    alertItem.setConfidence(id);
    alertItem.setDesc("desc" + id);
    return alertItem;
  }

  private Instance createTestInstance(final Integer id) {
    final Instance instance = new Instance();
    instance.setUri("http://example.org/instance" + id);
    instance.setMethod("method" + id);
    instance.setParam("param" + id);
    instance.setAttack("attack" + id);
    instance.setEvidence("evidence" + id);
    return instance;
  }
}
