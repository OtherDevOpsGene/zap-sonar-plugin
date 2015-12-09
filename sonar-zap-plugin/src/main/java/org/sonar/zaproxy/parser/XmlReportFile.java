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
package org.sonar.zaproxy.parser;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.zaproxy.ZapSensorConfiguration;
import org.sonar.zaproxy.base.ZapConstants;

import javax.annotation.CheckForNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class XmlReportFile {
    private static final Logger LOGGER = Loggers.get(XmlReportFile.class);

    private final ZapSensorConfiguration configuration;
    private final FileSystem fileSystem;

    private File report;

    public XmlReportFile(ZapSensorConfiguration configuration, FileSystem fileSystem) {
        this.configuration = configuration;
        this.fileSystem = fileSystem;
    }

    /**
     * Report file, null if the property is not set.
     *
     * @throws org.sonar.api.utils.MessageException if the property relates to a directory or a non-existing file.
     */
    @CheckForNull
    private File getReportFromProperty() {
        String path = this.configuration.getReportPath();
        if (StringUtils.isNotBlank(path)) {
            this.report = new File(path);
            if (!report.isAbsolute()) {
                report = new File(this.fileSystem.baseDir(), path);
            }
            if (report.exists() && report.isFile()) {
                return report;
            }
            LOGGER.warn("ZAP report does not exist. SKIPPING. Please check property " +
                    ZapConstants.REPORT_PATH_PROPERTY + ": " + path);
        }
        return null;
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
            throw new FileNotFoundException("ZAP report does not exist.");
        }
        return new FileInputStream(reportFile);
    }

    public boolean exist() {
        File reportFile = getReportFromProperty();
        return reportFile != null;
    }
}
