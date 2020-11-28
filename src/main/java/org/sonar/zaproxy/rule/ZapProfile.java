package org.sonar.zaproxy.rule;

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
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;
import org.sonar.zaproxy.ZapPlugin;
import org.sonar.zaproxy.base.ZapUtils;

public class ZapProfile implements BuiltInQualityProfilesDefinition {

  /** Return the key of the rule pointed by ruleCursor. */
  private String processKey(SMInputCursor ruleCursor) throws XMLStreamException {
    SMInputCursor childCursor = ruleCursor.childElementCursor("key");
    childCursor.getNext();
    return StringUtils.trim(childCursor.collectDescendantText(false));
  }

  /** Process all rules to get all rules' key. */
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
  public void define(Context context) {
    NewBuiltInQualityProfile profile =
        context.createBuiltInQualityProfile(ZapPlugin.LANGUAGE_NAME, ZapPlugin.LANGUAGE_KEY);
    profile.setDefault(true);

    InputStream inputStream = getClass().getResourceAsStream(ZapPlugin.RULES_FILE);
    List<String> ruleKeysList = new ArrayList<>(getAllRuleKeysFromFile(inputStream));

    for (String ruleKey : ruleKeysList) {
      profile.activateRule(ZapPlugin.REPOSITORY_KEY, ruleKey);
    }

    profile.done();
  }
}
