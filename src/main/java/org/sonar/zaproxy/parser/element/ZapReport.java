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

public class ZapReport {

  private String generated;
  private String versionZAP;
  private List<Site> sites;

  public ZapReport(String generated, String versionZAP, List<Site> sites) {
    this.generated = generated;
    this.versionZAP = versionZAP;
    this.sites = sites;
  }

  public String getGenerated() {
    return generated;
  }

  public String getVersionZAP() {
    return versionZAP;
  }

  public List<Site> getSites() {
    return sites;
  }

  public void addSite(Site site) {
    if (sites == null) {
      sites = new ArrayList<Site>();
    }
    sites.add(site);
  }

  public int getIssueCount() {
    int count = 0;
    for (Site site : sites) {
      count += site.getAlerts().size();
    }
    return count;
  }

  @Override
  public String toString() {
    String s = "";
    s += "generated : [" + generated + "]\n";
    s += "versionZAP : [" + versionZAP + "]\n";
    s += "sites : [" + sites.toString() + "]\n";
    return s;
  }
}
