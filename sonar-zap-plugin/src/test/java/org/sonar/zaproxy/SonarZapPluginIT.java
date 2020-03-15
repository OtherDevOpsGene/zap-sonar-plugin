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
package org.sonar.zaproxy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.seljup.SeleniumExtension;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

@ExtendWith(SeleniumExtension.class)
public class SonarZapPluginIT {

  /**
   * Poll periodically for page load to complete.
   */
  private static final long SLEEP_MILLIS = 200L;

  /**
   * Timeout if page is not loaded in this time.
   */
  private static final long TIMEOUT_MILLIS = 60L * 1000L;

  /**
   * Number of vulnerabilities to expect.
   */
  public static final String NUM_VULNERABILITIES = "14";

  /**
   * Number of major vulnerabilities to expect.
   */
  public static final String NUM_MAJORS = "1";

  /**
   * Number of minor vulnerabilities to expect.
   */
  public static final String NUM_MINORS = "9";

  /**
   * Number of info vulnerabilities to expect.
   */
  public static final String NUM_INFOS = "4";

  /**
   * Base URL for the tests.
   */
  private final String baseUrl = "http://localhost:9000";

  @Test
  void dashboard(final ChromeDriver driver) {
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    driver.get(baseUrl + "/dashboard?id=org.sonarsource.owasp%3Asonar-zap-plugin");

    waitForFooter(driver);

    // http://localhost:9000/project/issues?id=org.sonarsource.owasp%3Asonar-zap-plugin&resolved=false&types=VULNERABILITY
    final WebElement vulnerabilities = driver.findElement(By.cssSelector("a[href*='types=VULNERABILITY']"));

    assertEquals(NUM_VULNERABILITIES, vulnerabilities.getText());
  }

  @Test
  void issues(final ChromeDriver driver) {
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    driver.get(baseUrl + "/project/issues?id=org.sonarsource.owasp%3Asonar-zap-plugin&resolved=false&types=VULNERABILITY");

    waitForFooter(driver);

    final WebElement majorsLink = driver.findElement(By.cssSelector("a[data-facet='MAJOR']"));
    final WebElement majors = majorsLink.findElement(By.cssSelector("span.facet-stat"));
    assertEquals(NUM_MAJORS, majors.getText(), "Number of Majors");

    final WebElement minorsLink = driver.findElement(By.cssSelector("a[data-facet='MINOR']"));
    final WebElement minors = minorsLink.findElement(By.cssSelector("span.facet-stat"));
    assertEquals(NUM_MINORS, minors.getText(),"Number of Minors");

    final WebElement infosLink = driver.findElement(By.cssSelector("a[data-facet='INFO']"));
    final WebElement info = infosLink.findElement(By.cssSelector("span.facet-stat"));
    assertEquals(NUM_INFOS, info.getText(), "Number of Infos");
  }

  private void waitForFooter(final RemoteWebDriver driver) {
    PAGE:
    for (long second = 0; ; second += SLEEP_MILLIS) {
      if (second >= TIMEOUT_MILLIS) {
        fail("Page failed to load in " + TIMEOUT_MILLIS + "ms.");
      }
      final List<WebElement> footers = driver.findElements(By.cssSelector("div#footer.page-footer.page-container > div"));
      for (final WebElement footer: footers) {
        final String footerText = footer.getText();
        if (footerText.startsWith("SonarQube™ technology is powered by SonarSource SA")) {
          break PAGE;
        }
      }
      try {
        Thread.sleep(SLEEP_MILLIS);
      } catch (final InterruptedException ie) {
        Thread.currentThread().interrupt();
        throw new RuntimeException(ie);
      }
    }
  }
}
