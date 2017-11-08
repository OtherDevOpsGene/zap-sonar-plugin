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
package org.sonar.zaproxy.base;

import java.util.Arrays;
import java.util.List;

import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;
import org.sonar.api.measures.SumChildValuesFormula;

public final class ZapMetrics implements Metrics {

    public static final String DOMAIN = "OWASP-ZAP";

    public static final String IDENTIFIED_RISK_SCORE_KEY = "identified_risk_score";

    public static final String HIGH_RISK_ALERTS_KEY = "high_risk_alerts";
    public static final String MEDIUM_RISK_ALERTS_KEY = "medium_risk_alerts";
    public static final String LOW_RISK_ALERTS_KEY = "low_risk_alerts";
    public static final String INFO_RISK_ALERTS_KEY = "info_risk_alerts";
    public static final String TOTAL_ALERTS_KEY = "total_alerts";

    public static double inheritedRiskScore(int high, int medium, int low) {
        return (high * 5) + (medium * 3) + (low * 1);
    }

    public static final Metric IDENTIFIED_RISK_SCORE = new Metric.Builder(ZapMetrics.IDENTIFIED_RISK_SCORE_KEY, "Identified Risk Score", Metric.ValueType.INT)
            .setDescription("Identified Risk Score")
            .setDirection(Metric.DIRECTION_BETTER)
            .setQualitative(true)
            .setDomain(ZapMetrics.DOMAIN)
            .setFormula(new SumChildValuesFormula(false))
            .setBestValue(0.0)
            .create();

    public static final Metric HIGH_RISK_ALERTS = new Metric.Builder(HIGH_RISK_ALERTS_KEY, "High Risk Alerts", Metric.ValueType.INT)
            .setDescription("High Risk Alerts")
            .setDirection(Metric.DIRECTION_WORST)
            .setQualitative(false)
            .setDomain(ZapMetrics.DOMAIN)
            .setBestValue(0.0)
            .setHidden(false)
            .create();

    public static final Metric MEDIUM_RISK_ALERTS = new Metric.Builder(MEDIUM_RISK_ALERTS_KEY, "Medium Risk Alerts", Metric.ValueType.INT)
            .setDescription("Medium Risk Alerts")
            .setDirection(Metric.DIRECTION_WORST)
            .setQualitative(false)
            .setDomain(ZapMetrics.DOMAIN)
            .setBestValue(0.0)
            .setHidden(false)
            .create();

    public static final Metric LOW_RISK_ALERTS = new Metric.Builder(LOW_RISK_ALERTS_KEY, "Low Risk Alerts", Metric.ValueType.INT)
            .setDescription("Low Risk Alerts")
            .setDirection(Metric.DIRECTION_WORST)
            .setQualitative(false)
            .setDomain(ZapMetrics.DOMAIN)
            .setBestValue(0.0)
            .setHidden(false)
            .create();

    public static final Metric INFO_RISK_ALERTS = new Metric.Builder(INFO_RISK_ALERTS_KEY, "Info Risk Alerts", Metric.ValueType.INT)
            .setDescription("Info Risk Alerts")
            .setDirection(Metric.DIRECTION_WORST)
            .setQualitative(false)
            .setDomain(ZapMetrics.DOMAIN)
            .setBestValue(0.0)
            .setHidden(false)
            .create();

    public static final Metric TOTAL_ALERTS = new Metric.Builder(TOTAL_ALERTS_KEY, "Total Alerts", Metric.ValueType.INT)
            .setDescription("Total Alerts")
            .setDirection(Metric.DIRECTION_WORST)
            .setQualitative(false)
            .setDomain(ZapMetrics.DOMAIN)
            .setBestValue(0.0)
            .setHidden(false)
            .create();

    @Override
    public List<Metric> getMetrics() {
        return Arrays.asList(
                ZapMetrics.IDENTIFIED_RISK_SCORE,
                ZapMetrics.HIGH_RISK_ALERTS,
                ZapMetrics.MEDIUM_RISK_ALERTS,
                ZapMetrics.LOW_RISK_ALERTS,
                ZapMetrics.INFO_RISK_ALERTS,
                ZapMetrics.TOTAL_ALERTS
        );
    }
}