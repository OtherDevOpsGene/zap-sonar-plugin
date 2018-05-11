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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.StringUtils;
import org.codehaus.staxmate.SMInputFactory;
import org.codehaus.staxmate.in.SMHierarchicCursor;
import org.codehaus.staxmate.in.SMInputCursor;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.zaproxy.base.ZapUtils;
import org.sonar.zaproxy.parser.element.AlertItem;
import org.sonar.zaproxy.parser.element.Instance;
import org.sonar.zaproxy.parser.element.Site;
import org.sonar.zaproxy.parser.element.ZapReport;

public class ReportParser {

    private static final Logger LOGGER = Loggers.get(ReportParser.class);

    public ZapReport parse(InputStream inputStream) {
        SMInputFactory inputFactory = ZapUtils.newStaxParser();
        try {
            SMHierarchicCursor rootC = inputFactory.rootElementCursor(inputStream);

            SMInputCursor owaspZapReportCursor = rootC.advance(); // <OWASPZAPReport>

            String gererated = owaspZapReportCursor.getAttrValue("generated");
            String versionZAP = owaspZapReportCursor.getAttrValue("version");

            SMInputCursor childCursor = rootC.childCursor(); // Child of <OWASPZAPReport>, here only <site>

            List<Site> sites = new ArrayList<Site>();

            while (childCursor.getNext() != null) {
                String nodeName = childCursor.getLocalName();
                if ("site".equals(nodeName)) {
                    sites.add(processSite(childCursor));

                }
            }
            return new ZapReport(gererated, versionZAP, sites);
        } catch (XMLStreamException e) {
            throw new IllegalStateException("XML is not valid", e);
        }
    }

    private Site processSite(SMInputCursor siteCursor) throws XMLStreamException {
        Site site = new Site();

        site.setHost(siteCursor.getAttrValue("host"));
        site.setName(siteCursor.getAttrValue("name"));
        site.setPort(Integer.valueOf(siteCursor.getAttrValue("port")));
        site.setSsl(Boolean.valueOf(siteCursor.getAttrValue("ssl")));

        SMInputCursor childCursor = siteCursor.childCursor(); // Child of <site>, here only <alerts>

        while (childCursor.getNext() != null) {
            String nodeName = childCursor.getLocalName();
            if ("alerts".equals(nodeName)) {
                site.setAlerts(processAlerts(childCursor));
            }
        }
        return site;
    }

    private Collection<AlertItem> processAlerts(SMInputCursor alertsCursor) throws XMLStreamException {
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
            if ("pluginid".equals(nodeName)) {
                alertItem.setPluginid(childCursor.getElemIntValue());
            } else if ("alert".equals(nodeName)) {
                alertItem.setAlert(StringUtils.trim(childCursor.collectDescendantText(false)));
            } else if ("riskcode".equals(nodeName)) {
                alertItem.setRiskcode(childCursor.getElemIntValue());
            } else if ("confidence".equals(nodeName)) {
                alertItem.setConfidence(childCursor.getElemIntValue());
            } else if ("riskdesc".equals(nodeName)) {
                alertItem.setRiskdesc(StringUtils.trim(childCursor.collectDescendantText(false)));
            } else if ("desc".equals(nodeName)) {
                alertItem.setDesc(StringUtils.trim(childCursor.collectDescendantText(false)));
            } else if ("instances".equals(nodeName)) {
                SMInputCursor instanceCursor = childCursor.childElementCursor("instance");
                while (instanceCursor.getNext() != null) {
                    SMInputCursor childInstanceCursor = instanceCursor.childCursor();
                    Instance instance = new Instance();
                    while (childInstanceCursor.getNext() != null) {
                        String instanceNodeName = childInstanceCursor.getLocalName();
                        if (instanceNodeName != null) {
                            String value = StringUtils.trim(childInstanceCursor.collectDescendantText(false));
                            if ("uri".equals(instanceNodeName)) {
                                instance.setUri(value);
                            } else if ("param".equals(instanceNodeName)) {
                                instance.setParam(value);
                            } else if ("method".equals(instanceNodeName)) {
                                instance.setMethod(value);
                            } else if ("evidence".equals(instanceNodeName)) {
                                instance.setEvidence(value);
                            } else if ("attack".equals(instanceNodeName)) {
                                instance.setAttack(value);
                            }
                        }
                    }
                    alertItem.addInstance(instance);
                }
            } else if ("otherinfo".equals(nodeName)) {
                alertItem.setOtherinfo(StringUtils.trim(childCursor.collectDescendantText(false)));
            } else if ("solution".equals(nodeName)) {
                alertItem.setSolution(StringUtils.trim(childCursor.collectDescendantText(false)));
            } else if ("reference".equals(nodeName)) {
                alertItem.setReference(StringUtils.trim(childCursor.collectDescendantText(false)));
            } else if ("cweid".equals(nodeName)) {
                alertItem.setCweid(childCursor.getElemIntValue());
            } else if ("wascid".equals(nodeName)) {
                alertItem.setWascid(childCursor.getElemIntValue());
            }
        }
        return alertItem;
    }

}