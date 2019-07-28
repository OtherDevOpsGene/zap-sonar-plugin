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
package org.sonar.zaproxy.parser;

import static org.fest.assertions.Assertions.assertThat;

import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.junit.Test;
import org.sonar.zaproxy.parser.element.AlertItem;
import org.sonar.zaproxy.parser.element.Site;
import org.sonar.zaproxy.parser.element.ZapReport;

public class ReportParserTest {

  @Test
  public void parseReport() throws Exception {
    ReportParser parser = new ReportParser();
    InputStream inputStream = getClass().getClassLoader()
        .getResourceAsStream("report/zaproxy-report.xml");
    ZapReport zapReport = parser.parse(inputStream);
    assertThat(zapReport.getGenerated()).isEqualTo("Thu, 3 May 2018 06:15:48");
    assertThat(zapReport.getVersionZAP()).isEqualTo("D-2018-03-26");

    List<Site> sites = zapReport.getSites();
    Site site = sites.get(0);
    assertThat(site.getHost()).isEqualTo("example.org");
    assertThat(site.getName()).isEqualTo("http://example.org");
    assertThat(site.getPort()).isEqualTo(80);
    assertThat(site.isSsl()).isEqualTo(false);

    Collection<AlertItem> alerts = site.getAlerts();
    assertThat(alerts.size()).isEqualTo(1);

    Iterator<AlertItem> iterator = alerts.iterator();
    AlertItem alert = iterator.next();

    assertThat(alert.getPluginid()).isEqualTo(10010);
    assertThat(alert.getAlert()).isEqualTo("Cookie No HttpOnly Flag");
    assertThat(alert.getRiskcode()).isEqualTo(1);
    assertThat(alert.getConfidence()).isEqualTo(2);
    assertThat(alert.getRiskdesc()).isEqualTo("Low (Medium)");
    assertThat(alert.getDesc()).isEqualTo(
        "<p>A cookie has been set without the HttpOnly flag, which means that the cookie can be accessed by JavaScript. If a malicious script can be run on this page then the cookie will be accessible and can be transmitted to another site. If this is a session cookie then session hijacking may be possible.</p>");
    assertThat(alert.getInstances().size()).isEqualTo(20);
    assertThat(alert.getInstances().get(1).getUri()).isEqualTo("http://example.org/support");
    assertThat(alert.getInstances().get(1).getMethod()).isEqualTo("GET");
    assertThat(alert.getInstances().get(1).getParam()).isEqualTo("_pxCaptcha");
    assertThat(alert.getInstances().get(1).getEvidence()).isEqualTo("Set-Cookie: _pxCaptcha");

//        assertThat(alert.getUri()).isEqualTo("http://localhost:8180/robots.txt");
//        assertThat(alert.getParam()).isNullOrEmpty();
    assertThat(alert.getAttack()).isNullOrEmpty();
    //assertThat(alert.getOtherinfo()).endsWith("Note that this alert is only raised if the response body could potentially contain an XSS payload (with a text-based content type, with a non-zero length).");
    assertThat(alert.getSolution())
        .isEqualTo("<p>Ensure that the HttpOnly flag is set for all cookies.</p>");
    assertThat(alert.getReference()).contains("<p>http://www.owasp.org/index.php/HttpOnly</p>");
    assertThat(alert.getCweid()).isEqualTo(16);
    assertThat(alert.getWascid()).isEqualTo(13);
  }

  @Test
  public void parseReportNoInstances() throws Exception {
    ReportParser parser = new ReportParser();
    InputStream inputStream = getClass().getClassLoader()
        .getResourceAsStream("report/zaproxy-report-no-instances.xml");
    ZapReport zapReport = parser.parse(inputStream);
    assertThat(zapReport.getGenerated()).isEqualTo("Fri, 8 Jun 2018 09:55:08");
    assertThat(zapReport.getVersionZAP()).isEqualTo("2.7.0");

    List<Site> sites = zapReport.getSites();
    Site site = sites.get(0);
    assertThat(site.getHost()).isEqualTo("example.org");
    assertThat(site.getName()).isEqualTo("http://example.org");
    assertThat(site.getPort()).isEqualTo(80);
    assertThat(site.isSsl()).isEqualTo(false);

    Collection<AlertItem> alerts = site.getAlerts();
    assertThat(alerts.size()).isEqualTo(3);

    Iterator<AlertItem> iterator = alerts.iterator();
    AlertItem alert = iterator.next();

    assertThat(alert.getPluginid()).isEqualTo(10015);
    assertThat(alert.getAlert())
        .isEqualTo("Incomplete or No Cache-control and Pragma HTTP Header Set");
    assertThat(alert.getRiskcode()).isEqualTo(1);
    assertThat(alert.getConfidence()).isEqualTo(2);
    assertThat(alert.getRiskdesc()).isEqualTo("Low (Medium)");
    assertThat(alert.getDesc()).isEqualTo(
        "<p>The cache-control and pragma HTTP header have not been set properly or are missing allowing the browser and proxies to cache content.</p>");
    assertThat(alert.getInstances()).isNull();
    assertThat(alert.getUri())
        .isEqualTo("http://example.org/sites/example.org/files/advagg_css/css.css");
    assertThat(alert.getMethod()).isEqualTo("GET");
    assertThat(alert.getParam()).isEqualTo("Cache-Control");
    assertThat(alert.getEvidence()).isEqualTo("max-age=31449600, no-transform, public");

    assertThat(alert.getAttack()).isNullOrEmpty();

    assertThat(alert.getSolution()).isEqualTo(
        "<p>Whenever possible ensure the cache-control HTTP header is set with no-cache, no-store, must-revalidate; and that the pragma HTTP header is set with no-cache.</p>");
    assertThat(alert.getReference()).contains(
        "<p>https://www.owasp.org/index.php/Session_Management_Cheat_Sheet#Web_Content_Caching</p>");
    assertThat(alert.getCweid()).isEqualTo(525);
    assertThat(alert.getWascid()).isEqualTo(13);
  }
  
  @Test
  public void parseReportMoreRules() throws Exception {
    ReportParser parser = new ReportParser();
    InputStream inputStream = getClass().getClassLoader()
        .getResourceAsStream("report/zapproxy-report-more-rules.xml");
    ZapReport zapReport = parser.parse(inputStream);
    assertThat(zapReport.getGenerated()).isEqualTo("Wed, 31 Oct 2018 19:13:06");
    assertThat(zapReport.getVersionZAP()).isEqualTo("D-2018-10-29");

    List<Site> sites = zapReport.getSites();
    Site site = sites.get(0);
    assertThat(site.getHost()).isEqualTo("192.168.1.15");
    assertThat(site.getName()).isEqualTo("https://192.168.1.15:8443");
    assertThat(site.getPort()).isEqualTo(8443);
    assertThat(site.isSsl()).isEqualTo(true);

    Collection<AlertItem> alerts = site.getAlerts();
    assertThat(alerts.size()).isEqualTo(18);
  }

}
