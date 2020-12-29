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

import java.util.Arrays;
import java.util.List;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

public final class ZapMetrics implements Metrics {

  public static final String DOMAIN = "OWASP-ZAP";

  public static final String IDENTIFIED_RISK_SCORE_KEY = "identified_risk_score";

  public static final String HIGH_RISK_ALERTS_KEY = "high_risk_alerts";
  public static final String MEDIUM_RISK_ALERTS_KEY = "medium_risk_alerts";
  public static final String LOW_RISK_ALERTS_KEY = "low_risk_alerts";
  public static final String INFO_RISK_ALERTS_KEY = "info_risk_alerts";
  public static final String TOTAL_ALERTS_KEY = "total_alerts";
  public static final String HTML_REPORT_KEY = "html_report";

  public static final Metric IDENTIFIED_RISK_SCORE =
      new Metric.Builder(
              ZapMetrics.IDENTIFIED_RISK_SCORE_KEY, "Identified Risk Score", Metric.ValueType.INT)
          .setDescription("Identified Risk Score")
          .setDirection(Metric.DIRECTION_BETTER)
          .setQualitative(true)
          .setDomain(ZapMetrics.DOMAIN)
          .setBestValue(0.0)
          .create();
  public static final Metric HIGH_RISK_ALERTS =
      new Metric.Builder(HIGH_RISK_ALERTS_KEY, "High Risk Alerts", Metric.ValueType.INT)
          .setDescription("High Risk Alerts")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(ZapMetrics.DOMAIN)
          .setBestValue(0.0)
          .setHidden(false)
          .create();
  public static final Metric MEDIUM_RISK_ALERTS =
      new Metric.Builder(MEDIUM_RISK_ALERTS_KEY, "Medium Risk Alerts", Metric.ValueType.INT)
          .setDescription("Medium Risk Alerts")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(ZapMetrics.DOMAIN)
          .setBestValue(0.0)
          .setHidden(false)
          .create();
  public static final Metric LOW_RISK_ALERTS =
      new Metric.Builder(LOW_RISK_ALERTS_KEY, "Low Risk Alerts", Metric.ValueType.INT)
          .setDescription("Low Risk Alerts")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(ZapMetrics.DOMAIN)
          .setBestValue(0.0)
          .setHidden(false)
          .create();
  public static final Metric INFO_RISK_ALERTS =
      new Metric.Builder(INFO_RISK_ALERTS_KEY, "Info Risk Alerts", Metric.ValueType.INT)
          .setDescription("Info Risk Alerts")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(ZapMetrics.DOMAIN)
          .setBestValue(0.0)
          .setHidden(false)
          .create();
  public static final Metric TOTAL_ALERTS =
      new Metric.Builder(TOTAL_ALERTS_KEY, "Total Alerts", Metric.ValueType.INT)
          .setDescription("Total Alerts")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(ZapMetrics.DOMAIN)
          .setBestValue(0.0)
          .setHidden(false)
          .create();

  public static final Metric<String> HTML_REPORT =
      new Metric.Builder(HTML_REPORT_KEY, "ZAP Report", Metric.ValueType.DATA)
          .setDescription("Report HTML")
          .setQualitative(Boolean.FALSE)
          .setDomain(ZapMetrics.DOMAIN)
          .setHidden(false)
          .setDeleteHistoricalData(true)
          .create();

  public static double inheritedRiskScore(int high, int medium, int low) {
    return (high * 5.0d) + (medium * 3.0d) + (low * 1.0d);
  }

  @Override
  public List<Metric> getMetrics() {
    return Arrays.asList(
        ZapMetrics.IDENTIFIED_RISK_SCORE,
        ZapMetrics.HIGH_RISK_ALERTS,
        ZapMetrics.MEDIUM_RISK_ALERTS,
        ZapMetrics.LOW_RISK_ALERTS,
        ZapMetrics.INFO_RISK_ALERTS,
        ZapMetrics.TOTAL_ALERTS,
        ZapMetrics.HTML_REPORT);
  }
}
