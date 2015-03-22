/*
 * =============================================================================
 * 
 *   Copyright (c) 2011-2014, The THYMELEAF team (http://www.thymeleaf.org)
 * 
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 * 
 * =============================================================================
 */
package org.thymeleaf.aurora.context;

import java.util.Collections;

import org.thymeleaf.aurora.DialectConfiguration;
import org.thymeleaf.aurora.standard.StandardDialect;
import org.thymeleaf.aurora.text.TextRepositories;


public final class TestTemplateEngineContextBuilder {



    public static ITemplateEngineContext build() {
        final StandardDialect standardDialect = new StandardDialect();
        final DialectConfiguration standardDialectConfiguration = new DialectConfiguration(standardDialect);
        return new TemplateEngineContext(Collections.singleton(standardDialectConfiguration), TextRepositories.createLimitedSizeCacheRepository());
    }


    private TestTemplateEngineContextBuilder() {
        super();
    }

}