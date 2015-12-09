/*
 * ZAP Plugin for SonarQube
 * Copyright (C) 2015 Steve Springett
 * steve.springett@owasp.org
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
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.zaproxy;

import org.sonar.api.SonarPlugin;
import org.sonar.zaproxy.rule.ZapLanguage;
import org.sonar.zaproxy.rule.ZapProfile;
import org.sonar.zaproxy.rule.ZapRuleDefinition;
import org.sonar.zaproxy.ui.ZapWidget;
import org.sonar.zaproxy.base.ZapMetrics;

import java.util.Arrays;
import java.util.List;

public final class ZapPlugin extends SonarPlugin {

    public static final String REPOSITORY_KEY = "ZAProxy";
    public static final String LANGUAGE_KEY = "zap";
    public static final String LANGUAGE_NAME = "ZAP";
    public static final String RULES_FILE = "/org/sonar/zaproxy/rules.xml";

    @Override
    public List getExtensions() {
        return Arrays.asList(
                ZapSensor.class,
                ZapSensorConfiguration.class,
                ZapMetrics.class,
                ZapProfile.class,
                ZapLanguage.class,
                ZapRuleDefinition.class,
                ZapWidget.class);
    }
}
