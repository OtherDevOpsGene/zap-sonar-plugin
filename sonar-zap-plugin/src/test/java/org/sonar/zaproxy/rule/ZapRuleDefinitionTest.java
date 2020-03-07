/*
 * ZAP Plugin for SonarQube
 * Copyright (C) 2015-2020 Gene Gotimer
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
package org.sonar.zaproxy.rule;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.sonar.api.config.Configuration;
import org.sonar.api.server.rule.RulesDefinition.Context;
import org.sonar.api.server.rule.RulesDefinition.Rule;
import org.sonar.api.server.rule.RulesDefinitionXmlLoader;

public class ZapRuleDefinitionTest {

  @Test
  public void defineContextTest() {
    RulesDefinitionXmlLoader xmlLoader = new RulesDefinitionXmlLoader();
    Configuration configuration = mock(Configuration.class);

    ZapRuleDefinition ZapRuleDefinition = new ZapRuleDefinition(xmlLoader, configuration);

    Context context = new Context();
    ZapRuleDefinition.define(context);
    for (Rule rule : context.repositories().get(0).rules()) {
      assertFalse(rule.tags().isEmpty());
    }
  }
}
