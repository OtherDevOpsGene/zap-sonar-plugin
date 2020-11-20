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

import org.sonar.api.Plugin;
import org.sonar.zaproxy.base.ZapMetrics;
import org.sonar.zaproxy.rule.ZapLanguage;
import org.sonar.zaproxy.rule.ZapProfile;
import org.sonar.zaproxy.rule.ZapRuleDefinition;
import org.sonar.zaproxy.ui.ZapWidget;

public final class ZapPlugin implements Plugin {

  public static final String REPOSITORY_KEY = "ZAProxy";
  public static final String LANGUAGE_KEY = "zap";
  public static final String LANGUAGE_NAME = "ZAP";
  public static final String RULES_FILE = "/org/sonar/zaproxy/rules.xml";

  @Override
  public void define(Context context) {
    context.addExtensions(
        ZapSensor.class,
        ZapSensorConfiguration.class,
        ZapMetrics.class,
        ZapProfile.class,
        ZapLanguage.class,
        ZapRuleDefinition.class,
        ZapWidget.class);
  }
}
