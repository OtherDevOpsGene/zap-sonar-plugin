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

import org.apache.commons.lang.StringUtils;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.rule.Rules;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.issue.Issuable;
import org.sonar.api.issue.Issue;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.rule.Severity;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.api.utils.log.Profiler;
import org.sonar.zaproxy.base.ZapUtils;
import org.sonar.zaproxy.base.ZapMetrics;
import org.sonar.zaproxy.parser.ReportParser;
import org.sonar.zaproxy.parser.XmlReportFile;
import org.sonar.zaproxy.parser.element.AlertItem;
import org.sonar.zaproxy.parser.element.ZapReport;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

public class ZapSensor implements Sensor {

    private static final Logger LOGGER = Loggers.get(ZapSensor.class);

    private final ZapSensorConfiguration configuration;
    private final ResourcePerspectives resourcePerspectives;
    private final FileSystem fileSystem;
    private final Rules rules;
    private final XmlReportFile report;

    private int totalAlerts;
    private int criticalIssuesCount;
    private int majorIssuesCount;
    private int minorIssuesCount;
    private int infoIssuesCount;


    public ZapSensor(
            ZapSensorConfiguration configuration,
            ResourcePerspectives resourcePerspectives,
            FileSystem fileSystem,
            Rules rules) {
        this.configuration = configuration;
        this.resourcePerspectives = resourcePerspectives;
        this.fileSystem = fileSystem;
        this.rules = rules;
        this.report = new XmlReportFile(configuration, fileSystem);
    }

    @Override
    public boolean shouldExecuteOnProject(Project project) {
        return this.report.exist();
    }

    private void addIssue(Project project, AlertItem alert) {
        Issuable issuable = this.resourcePerspectives.as(Issuable.class, (Resource)project);
        if (issuable != null) {
            String severity = ZapUtils.riskCodeToSonarQubeSeverity(alert.getRiskcode());
            String pluginid = String.valueOf(alert.getPluginid());
            // Check if the rule with the pluginid exists
            if(rules.find(RuleKey.of(ZapPlugin.REPOSITORY_KEY, pluginid)) != null) {
                Issue issue = issuable.newIssueBuilder()
                        .ruleKey(RuleKey.of(ZapPlugin.REPOSITORY_KEY, pluginid))
                        .message(formatDescription(alert))
                        .severity(severity)
                        .build();
                if (issuable.addIssue(issue)) {
                    incrementCount(severity);
                }

            } else {
                LOGGER.warn("The rule " + ZapPlugin.REPOSITORY_KEY + ":" + pluginid + " doesn't exist.");
            }
        }
    }

    /**
     *      todo: Add Markdown formatting if and when Sonar supports it
     *      https://jira.codehaus.org/browse/SONAR-4161
     */
    private String formatDescription(AlertItem alert) {
        StringBuilder sb = new StringBuilder();

        sb.append(addValueToDescription("URI", alert.getUri(), false));
        sb.append(addValueToDescription("Confidence", String.valueOf(alert.getConfidence()), false));
        sb.append(addValueToDescription("Description", alert.getDesc(), false));
        sb.append(addValueToDescription("Param", alert.getParam(), false));
        sb.append(addValueToDescription("Attack", alert.getAttack(), false));
        sb.append(addValueToDescription("Evidence", alert.getEvidence(), true));

        return sb.toString();
    }

    private String addValueToDescription(String name, String value, boolean isEnd) {
        StringBuilder sb = new StringBuilder();
        if( !StringUtils.isBlank(value) ) {
            sb.append(name).append(": ").append(value);
            if(!isEnd) {
                sb.append(" | ");
            }
        }
        return sb.toString();
    }

    private void incrementCount(String severity) {
        switch (severity) {
            case Severity.CRITICAL:
                this.criticalIssuesCount++;
                break;
            case Severity.MAJOR:
                this.majorIssuesCount++;
                break;
            case Severity.MINOR:
                this.minorIssuesCount++;
                break;
            case Severity.INFO:
                this.infoIssuesCount++;
                break;
        }
    }

    private void addIssues(Project project, ZapReport zapReport) {
        if (zapReport.getSite().getAlerts() == null) {
            return;
        }
        for (AlertItem alert : zapReport.getSite().getAlerts()) {
            addIssue(project, alert);
        }
    }

    private ZapReport parseZapReport() throws IOException, ParserConfigurationException, SAXException {
        try (InputStream stream = this.report.getInputStream()) {
            return new ReportParser().parse(stream);
        }
    }

    public void analyse(Project project, SensorContext context) {
        Profiler profiler = Profiler.create(LOGGER);
        profiler.startInfo("Process ZAP report");
        try {
            ZapReport zapReport = parseZapReport();
            totalAlerts = zapReport.getSite().getAlerts().size();
            addIssues(project, zapReport);
        } catch (Exception e) {
            throw new RuntimeException("Can not process ZAP report. Ensure the report are located within the project workspace and that sonar.sources is set to reflect these paths (or set sonar.sources=.)", e);
        } finally {
            profiler.stopInfo();
        }
        saveMeasures(context);
    }

    private void saveMeasures(SensorContext context) {
        context.saveMeasure(ZapMetrics.HIGH_RISK_ALERTS, (double) criticalIssuesCount);
        context.saveMeasure(ZapMetrics.MEDIUM_RISK_ALERTS, (double) majorIssuesCount);
        context.saveMeasure(ZapMetrics.LOW_RISK_ALERTS, (double) minorIssuesCount);
        context.saveMeasure(ZapMetrics.INFO_RISK_ALERTS, (double) infoIssuesCount);
        context.saveMeasure(ZapMetrics.TOTAL_ALERTS, (double) totalAlerts);

        context.saveMeasure(ZapMetrics.IDENTIFIED_RISK_SCORE, ZapMetrics.inheritedRiskScore(criticalIssuesCount, majorIssuesCount, minorIssuesCount));
    }

    @Override
    public String toString() {
        return "OWASP Zed Attack Proxy";
    }
}
