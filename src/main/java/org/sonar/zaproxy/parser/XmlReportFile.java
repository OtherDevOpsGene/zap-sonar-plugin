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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javax.annotation.CheckForNull;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.scan.filesystem.PathResolver;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.zaproxy.ZapSensorConfiguration;
import org.sonar.zaproxy.base.ZapConstants;

public class XmlReportFile {

  private static final Logger LOGGER = Loggers.get(XmlReportFile.class);

  private final ZapSensorConfiguration configuration;
  private final FileSystem fileSystem;
  private final PathResolver pathResolver;

  private File report;

  public XmlReportFile(
      ZapSensorConfiguration configuration, FileSystem fileSystem, PathResolver pathResolver) {
    this.configuration = configuration;
    this.fileSystem = fileSystem;
    this.pathResolver = pathResolver;
  }

  /**
   * Report file, null if the property is not set.
   *
   * @throws org.sonar.api.utils.MessageException if the property relates to a directory or a
   *     non-existing file.
   */
  @CheckForNull
  private File getReportFromProperty() {
    String path = configuration.getReportPath();
    if (path == null) {
      return null;
    }

    this.report = pathResolver.relativeFile(fileSystem.baseDir(), path);

    if (report != null && !report.isFile()) {
      LOGGER.warn(
          "ZAP report does not exist. SKIPPING. Please check property "
              + ZapConstants.REPORT_PATH_PROPERTY
              + ": "
              + path);
      return null;
    }
    return report;
  }

  public File getFile() {
    if (report == null) {
      report = getReportFromProperty();
    }
    return report;
  }

  public InputStream getInputStream() throws FileNotFoundException {
    File reportFile = getFile();
    if (reportFile == null) {
      LOGGER.warn("ZAP report does not exist.");
      return null;
    }
    return new FileInputStream(reportFile);
  }

  public boolean exist() {
    File reportFile = getReportFromProperty();
    return reportFile != null;
  }
}
