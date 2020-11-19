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
package org.sonar.zaproxy.rule;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import org.apache.commons.lang.StringUtils;
import org.codehaus.staxmate.SMInputFactory;
import org.codehaus.staxmate.in.SMHierarchicCursor;
import org.codehaus.staxmate.in.SMInputCursor;
import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.profiles.XMLProfileParser;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.utils.ValidationMessages;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.zaproxy.ZapPlugin;
import org.sonar.zaproxy.base.ZapUtils;

public class ZapProfile extends ProfileDefinition {

  private static final Logger LOGGER = Loggers.get(ZapProfile.class);
  private final XMLProfileParser xmlParser;
  private final RuleFinder ruleFinder;

  public ZapProfile(RuleFinder ruleFinder, XMLProfileParser xmlParser) {
    this.xmlParser = xmlParser;
    this.ruleFinder = ruleFinder;
  }

  /**
   * Return the key of the rule pointed by ruleCursor.
   */
  private String processKey(SMInputCursor ruleCursor) throws XMLStreamException {
    SMInputCursor childCursor = ruleCursor.childElementCursor("key");
    childCursor.getNext();
    return StringUtils.trim(childCursor.collectDescendantText(false));
  }

  /**
   * Process all rules to get all rules' key.
   */
  private Collection<String> processRules(SMInputCursor rulesCursor) throws XMLStreamException {
    Collection<String> ruleKeysCollection = new ArrayList<>();
    SMInputCursor ruleCursor = rulesCursor.childElementCursor("rule");
    while (ruleCursor.getNext() != null) {
      ruleKeysCollection.add(processKey(ruleCursor));
    }

    return ruleKeysCollection;
  }

  private Collection<String> getAllRuleKeysFromFile(InputStream inputStream) {
    SMInputFactory inputFactory = ZapUtils.newStaxParser();
    try {
      SMHierarchicCursor rootC = inputFactory.rootElementCursor(inputStream);
      SMInputCursor rulesCursor = rootC.advance();
      return processRules(rulesCursor);
    } catch (XMLStreamException e) {
      throw new IllegalStateException("XML is not valid", e);
    }
  }

  @Override
  public RulesProfile createProfile(ValidationMessages validation) {
    RulesProfile profile = RulesProfile.create(ZapPlugin.LANGUAGE_NAME, ZapPlugin.LANGUAGE_KEY);
    InputStream inputStream = getClass().getResourceAsStream(ZapPlugin.RULES_FILE);
    List<String> ruleKeysList = new ArrayList<>(getAllRuleKeysFromFile(inputStream));

    for (String ruleKey : ruleKeysList) {
      Rule rule = ruleFinder.findByKey(ZapPlugin.REPOSITORY_KEY, ruleKey);
      profile.activateRule(rule, null);
    }

    profile.setDefaultProfile(true);
    return profile;
  }
}