/*
 * ZAP Plugin for SonarQube
 * Copyright (C) 2015-2020 Gene Gotimer
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
package org.sonar.zaproxy.ui;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class ZapWidgetTest {

  @Test
  public void test_rails_template() throws Exception {
    ZapWidget widget = new ZapWidget();
    assertThat(widget.getClass().getResource(widget.getTemplatePath()))
        .as("Template not found: " + widget.getTemplatePath())
        .isNotNull();
  }

  @Test
  public void test_metadata() throws Exception {
    ZapWidget widget = new ZapWidget();
    assertThat(widget.getId()).containsIgnoringCase("zap");
    assertThat(widget.getTitle()).contains("Vulnerabilities identified by ZAP");
  }

}