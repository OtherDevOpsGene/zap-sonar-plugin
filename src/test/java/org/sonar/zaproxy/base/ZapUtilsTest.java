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

import static org.fest.assertions.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.sonar.api.batch.rule.Severity;

@RunWith(Parameterized.class)
public class ZapUtilsTest {

  private final int riskCodeSeverity;
  private final Severity expectedSeverity;

  public ZapUtilsTest(int riskCodeSeverity, Severity expectedSeverity) {
    this.riskCodeSeverity = riskCodeSeverity;
    this.expectedSeverity = expectedSeverity;
  }

  @Parameterized.Parameters
  public static Collection<Object[]> severities() {
    return Arrays.asList(
        new Object[][] {
          {3, Severity.CRITICAL},
          {2, Severity.MAJOR},
          {1, Severity.MINOR},
          {0, Severity.INFO}
        });
  }

  @Test
  public void testRiskCodeToSonarQubeSeverity() {
    assertThat(ZapUtils.riskCodeToSonarQubeSeverity(this.riskCodeSeverity))
        .isEqualTo(this.expectedSeverity);
  }
}
