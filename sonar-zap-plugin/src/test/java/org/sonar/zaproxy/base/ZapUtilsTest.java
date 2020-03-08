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
package org.sonar.zaproxy.base;

import static org.fest.assertions.Assertions.assertThat;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.sonar.api.batch.rule.Severity;

public class ZapUtilsTest {

  public static Stream<Map.Entry<Integer, Severity>> severities() {
    return Stream.of(
        new SimpleImmutableEntry<>(3, Severity.CRITICAL),
        new SimpleImmutableEntry<>(2, Severity.MAJOR),
        new SimpleImmutableEntry<>(1, Severity.MINOR),
        new SimpleImmutableEntry<>(0, Severity.INFO)
    );
  }

  @ParameterizedTest
  @MethodSource("severities")
  public void testRiskCodeToSonarQubeSeverity(Map.Entry<Integer, Severity> pair) {
    assertThat(ZapUtils.riskCodeToSonarQubeSeverity(pair.getKey()))
        .isEqualTo(pair.getValue());
  }

}