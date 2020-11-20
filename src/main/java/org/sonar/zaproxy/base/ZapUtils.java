package org.sonar.zaproxy.base;

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

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import org.codehaus.staxmate.SMInputFactory;
import org.sonar.api.batch.rule.Severity;

public final class ZapUtils {

  private ZapUtils() {}

  public static SMInputFactory newStaxParser() throws FactoryConfigurationError {
    XMLInputFactory xmlFactory = XMLInputFactory.newInstance();
    xmlFactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.TRUE);
    xmlFactory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, Boolean.FALSE);
    xmlFactory.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
    xmlFactory.setProperty(XMLInputFactory.IS_VALIDATING, Boolean.FALSE);
    return new SMInputFactory(xmlFactory);
  }

  public static Severity riskCodeToSonarQubeSeverity(int riskcode) {
    if (riskcode == 3) {
      return Severity.CRITICAL;
    } else if (riskcode == 2) {
      return Severity.MAJOR;
    } else if (riskcode == 1) {
      return Severity.MINOR;
    } else {
      return Severity.INFO;
    }
  }
}
