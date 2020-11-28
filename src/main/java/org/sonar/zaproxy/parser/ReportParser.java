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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.staxmate.SMInputFactory;
import org.codehaus.staxmate.in.SMHierarchicCursor;
import org.codehaus.staxmate.in.SMInputCursor;
import org.sonar.zaproxy.base.ZapUtils;
import org.sonar.zaproxy.parser.element.AlertItem;
import org.sonar.zaproxy.parser.element.Instance;
import org.sonar.zaproxy.parser.element.Site;
import org.sonar.zaproxy.parser.element.ZapReport;

public class ReportParser {

  public ZapReport parse(InputStream inputStream) {
    SMInputFactory inputFactory = ZapUtils.newStaxParser();
    try {
      SMHierarchicCursor rootC = inputFactory.rootElementCursor(inputStream);

      SMInputCursor owaspZapReportCursor = rootC.advance(); // <OWASPZAPReport>

      String generated = owaspZapReportCursor.getAttrValue("generated");
      String versionZAP = owaspZapReportCursor.getAttrValue("version");

      SMInputCursor childCursor =
          rootC.childCursor(); // Child of <OWASPZAPReport>, here only <site>

      List<Site> sites = new ArrayList<>();

      while (childCursor.getNext() != null) {
        String nodeName = childCursor.getLocalName();
        if ("site".equals(nodeName)) {
          sites.add(processSite(childCursor));
        }
      }
      return new ZapReport(generated, versionZAP, sites);
    } catch (XMLStreamException e) {
      throw new IllegalStateException("XML is not valid", e);
    }
  }

  private Site processSite(SMInputCursor siteCursor) throws XMLStreamException {
    Site site = new Site();

    site.setHost(siteCursor.getAttrValue("host"));
    site.setName(siteCursor.getAttrValue("name"));
    site.setPort(Integer.parseInt(siteCursor.getAttrValue("port")));
    site.setSsl(Boolean.parseBoolean(siteCursor.getAttrValue("ssl")));

    SMInputCursor childCursor = siteCursor.childCursor(); // Child of <site>, here only <alerts>

    while (childCursor.getNext() != null) {
      String nodeName = childCursor.getLocalName();
      if ("alerts".equals(nodeName)) {
        site.setAlerts(processAlerts(childCursor));
      }
    }
    return site;
  }

  private Collection<AlertItem> processAlerts(SMInputCursor alertsCursor)
      throws XMLStreamException {
    Collection<AlertItem> alertItemCollection = new ArrayList<>();
    SMInputCursor alertItemCursor = alertsCursor.childElementCursor("alertitem");

    while (alertItemCursor.getNext() != null) {
      alertItemCollection.add(processAlertItem(alertItemCursor));
    }
    return alertItemCollection;
  }

  private AlertItem processAlertItem(SMInputCursor alertItemCursor) throws XMLStreamException {
    AlertItem alertItem = new AlertItem();
    SMInputCursor childCursor = alertItemCursor.childCursor();
    while (childCursor.getNext() != null) {
      String nodeName = childCursor.getLocalName();
      switch (nodeName == null ? "" : nodeName) {
        case ("pluginid"):
          alertItem.setPluginid(childCursor.getElemIntValue());
          break;
        case "alert":
          alertItem.setAlert(StringUtils.trim(childCursor.collectDescendantText(false)));
          break;
        case "riskcode":
          alertItem.setRiskcode(childCursor.getElemIntValue());
          break;
        case "confidence":
          alertItem.setConfidence(childCursor.getElemIntValue());
          break;
        case "riskdesc":
          alertItem.setRiskdesc(StringUtils.trim(childCursor.collectDescendantText(false)));
          break;
        case "desc":
          alertItem.setDesc(StringUtils.trim(childCursor.collectDescendantText(false)));
          break;
        case "instances":
          SMInputCursor instanceCursor = childCursor.childElementCursor("instance");
          while (instanceCursor.getNext() != null) {
            SMInputCursor childInstanceCursor = instanceCursor.childCursor();
            Instance instance = processInstance(childInstanceCursor);
            alertItem.addInstance(instance);
          }
          break;
        case "uri":
          alertItem.setUri(StringUtils.trim(childCursor.collectDescendantText(false)));
          break;
        case "param":
          alertItem.setParam(StringUtils.trim(childCursor.collectDescendantText(false)));
          break;
        case "method":
          alertItem.setMethod(StringUtils.trim(childCursor.collectDescendantText(false)));
          break;
        case "evidence":
          alertItem.setEvidence(StringUtils.trim(childCursor.collectDescendantText(false)));
          break;
        case "attack":
          alertItem.setAttack(StringUtils.trim(childCursor.collectDescendantText(false)));
          break;
        case "otherinfo":
          alertItem.setOtherinfo(StringUtils.trim(childCursor.collectDescendantText(false)));
          break;
        case "solution":
          alertItem.setSolution(StringUtils.trim(childCursor.collectDescendantText(false)));
          break;
        case "reference":
          alertItem.setReference(StringUtils.trim(childCursor.collectDescendantText(false)));
          break;
        case "cweid":
          alertItem.setCweid(childCursor.getElemIntValue());
          break;
        case "wascid":
          alertItem.setWascid(childCursor.getElemIntValue());
          break;
        default:
          break;
      }
    }
    return alertItem;
  }

  private Instance processInstance(SMInputCursor childInstanceCursor) throws XMLStreamException {
    Instance instance = new Instance();
    while (childInstanceCursor.getNext() != null) {
      String instanceNodeName = childInstanceCursor.getLocalName();
      if (instanceNodeName != null) {
        String value = StringUtils.trim(childInstanceCursor.collectDescendantText(false));
        switch (instanceNodeName) {
          case "uri":
            instance.setUri(value);
            break;
          case "param":
            instance.setParam(value);
            break;
          case "method":
            instance.setMethod(value);
            break;
          case "evidence":
            instance.setEvidence(value);
            break;
          case "attack":
            instance.setAttack(value);
            break;
          default:
            break;
        }
      }
    }
    return instance;
  }
}
