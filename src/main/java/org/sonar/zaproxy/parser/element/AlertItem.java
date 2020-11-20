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
package org.sonar.zaproxy.parser.element;

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

import java.util.ArrayList;
import java.util.List;

public class AlertItem {

  private int pluginid;
  private String alert;
  private int riskcode;
  private int confidence;
  private String riskdesc;
  private String desc;
  private String uri;
  private String param;
  private List<Instance> instances;
  private String attack;
  private String evidence;
  private String otherinfo;
  private String solution;
  private String method;
  private String reference;
  private int cweid;
  private int wascid;

  public int getPluginid() {
    return pluginid;
  }

  public void setPluginid(int pluginid) {
    this.pluginid = pluginid;
  }

  public String getAlert() {
    return alert;
  }

  public void setAlert(String alert) {
    this.alert = alert;
  }

  public int getRiskcode() {
    return riskcode;
  }

  public void setRiskcode(int riskcode) {
    this.riskcode = riskcode;
  }

  public int getConfidence() {
    return confidence;
  }

  public void setConfidence(int confidence) {
    this.confidence = confidence;
  }

  public String getRiskdesc() {
    return riskdesc;
  }

  public void setRiskdesc(String riskdesc) {
    this.riskdesc = riskdesc;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public void addInstance(Instance instance) {
    if (this.instances == null) {
      this.instances = new ArrayList<Instance>();
    }
    this.instances.add(instance);
  }

  public List<Instance> getInstances() {
    return this.instances;
  }

  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public String getParam() {
    return param;
  }

  public void setParam(String param) {
    this.param = param;
  }

  public String getAttack() {
    return attack;
  }

  public void setAttack(String attack) {
    this.attack = attack;
  }

  public String getEvidence() {
    return evidence;
  }

  public void setEvidence(String evidence) {
    this.evidence = evidence;
  }

  public String getOtherinfo() {
    return otherinfo;
  }

  public void setOtherinfo(String otherinfo) {
    this.otherinfo = otherinfo;
  }

  public String getSolution() {
    return solution;
  }

  public void setSolution(String solution) {
    this.solution = solution;
  }

  public String getReference() {
    return reference;
  }

  public void setReference(String reference) {
    this.reference = reference;
  }

  public int getCweid() {
    return cweid;
  }

  public void setCweid(int cweid) {
    this.cweid = cweid;
  }

  public int getWascid() {
    return wascid;
  }

  public void setWascid(int wascid) {
    this.wascid = wascid;
  }

  @Override
  public String toString() {
    String instanceString = "";
    for (Instance instance : instances) {
      instanceString += instance.toString();
      if (instances.indexOf(instance) < instances.size() - 1) {
        instanceString += ", ";
      }
    }

    return "AlertItem [pluginid="
        + pluginid
        + ", alert="
        + alert
        + ", riskcode="
        + riskcode
        + ", confidence="
        + confidence
        + ", riskdesc="
        + riskdesc
        + ", desc="
        + desc
        + ", instances=["
        + instanceString
        + "], attack="
        + attack
        + ", evidence="
        + evidence
        + ", otherinfo="
        + otherinfo
        + ", solution="
        + solution
        + ", reference="
        + reference
        + ", cweid="
        + cweid
        + ", wascid="
        + wascid
        + "]\n";
  }
}
