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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.io.Charsets;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.sonar.api.config.Configuration;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinitionXmlLoader;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.zaproxy.ZapPlugin;
import org.sonar.zaproxy.base.ZapConstants;

public class ZapRuleDefinition implements RulesDefinition {

  private static final Logger LOGGER = Loggers.get(ZapRuleDefinition.class);

  private final RulesDefinitionXmlLoader xmlLoader;
  private final Configuration configuration;

  public ZapRuleDefinition(RulesDefinitionXmlLoader xmlLoader, Configuration configuration) {
    this.xmlLoader = xmlLoader;
    this.configuration = configuration;
  }

  private String getRulesFilePath() {
    Optional<String> rulesFilePath = this.configuration.get(ZapConstants.RULES_FILE_PATH_PROPERTY);
    if (!rulesFilePath.isPresent() || StringUtils.isBlank(rulesFilePath.get())) {
      return null;
    }
    LOGGER.info("Path to rules.xml = [" + rulesFilePath + "]");
    return rulesFilePath.get();
  }

  private void loadDefaultZAProxyRules(NewRepository repository) {
    xmlLoader.load(repository, getClass().getResourceAsStream(ZapPlugin.RULES_FILE), Charsets.UTF_8);
    
    for (NewRule newRule : repository.rules()) {
    	
		try {
			final Set<String> tags = (Set<String>) FieldUtils.readField(newRule, "tags", true);
			for (String tag : tags) {
				
				if (tag.contains("cweid-")) {
					newRule.addCwe(Integer.parseInt(tag.replace("cweid-", "")));					
				}
		    	
				if (tag.contains("owasp-")) {
					newRule.addOwaspTop10(OwaspTop10.valueOf(tag.replace("owasp-", "").toUpperCase()));				
				}
			}
		} catch (IllegalAccessException e) {
			LOGGER.warn("Problem parsing security tags",e);
		}    	
	}
  }

  @Override
  public void define(Context context) {
    NewRepository repository = context
        .createRepository(ZapPlugin.REPOSITORY_KEY, ZapPlugin.LANGUAGE_KEY);
    repository.setName(ZapPlugin.REPOSITORY_KEY);

    String rulesFilePath = getRulesFilePath();

    if (rulesFilePath == null) { // rules.xml by default
      loadDefaultZAProxyRules(repository);
    } else { // custom rules.xml
      File f = null;
      try {
        f = new File(rulesFilePath);
        xmlLoader.load(repository, new FileInputStream(f), "UTF-8");
      } catch (FileNotFoundException e) {
        LOGGER.warn("The file " + f.getAbsolutePath() + " does not exist", e);

        // Load default ZAProxy rules if custom rules.xml does not exist.
        loadDefaultZAProxyRules(repository);
      }
    }
    repository.done();
  }

}