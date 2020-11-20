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

import java.util.Collection;
import java.util.Collections;

public class Site {

  private String host;
  private String name;
  private int port;
  private boolean ssl;
  private Collection<AlertItem> alerts = Collections.emptyList();

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public boolean isSsl() {
    return ssl;
  }

  public void setSsl(boolean ssl) {
    this.ssl = ssl;
  }

  public Collection<AlertItem> getAlerts() {
    return alerts;
  }

  public void setAlerts(Collection<AlertItem> alerts) {
    this.alerts = alerts;
  }

  @Override
  public String toString() {
    String s = "";
    s += "host : [" + host + "]\n";
    s += "name : [" + name + "]\n";
    s += "port : [" + port + "]\n";
    s += "ssl : [" + ssl + "]\n";
    s += "alerts : [" + alerts + "]\n";
    return s;
  }
}
