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

import org.sonar.api.config.Configuration;
import org.sonar.api.scanner.ScannerSide;
import org.sonar.zaproxy.base.ZapConstants;

@ScannerSide
public class ZapSensorConfiguration {

  private final Configuration config;

  public ZapSensorConfiguration(final Configuration config) {
    this.config = config;
  }

  public String getReportPath() {
    return this.config.get(ZapConstants.REPORT_PATH_PROPERTY).orElse("missing/report/path");
  }

  public String getHtmlReportPath() {
    return this.config
        .get(ZapConstants.HTML_REPORT_PATH_PROPERTY)
        .orElse("missing/html/report/path");
  }
}
