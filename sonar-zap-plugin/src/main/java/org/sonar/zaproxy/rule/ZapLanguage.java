/*
 * ZAP Plugin for SonarQube
 * Copyright (C) 2015-2017 Steve Springett
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
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.zaproxy.rule;


import org.sonar.api.resources.AbstractLanguage;
import org.sonar.zaproxy.ZapPlugin;

/**
 * In order for a rule repository to work properly, the rules created in the repository
 * must be associated with a language.
 *
 * This class creates a new 'language' called zap
 */
public class ZapLanguage extends AbstractLanguage {

    public ZapLanguage() {
        super(ZapPlugin.LANGUAGE_KEY, ZapPlugin.LANGUAGE_NAME);
    }

    @Override
		public String[] getFileSuffixes() {
        return new String[0];
    }

}
