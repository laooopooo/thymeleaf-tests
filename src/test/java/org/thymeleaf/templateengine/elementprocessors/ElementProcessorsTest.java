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
package org.thymeleaf.templateengine.elementprocessors;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.templateengine.elementprocessors.dialect.AggDialect;
import org.thymeleaf.testing.templateengine.engine.TestExecutor;


public class ElementProcessorsTest {


    public ElementProcessorsTest() {
        super();
    }






    @Test
    public void testBlock() throws Exception {

        final TestExecutor executor = new TestExecutor();
        executor.execute("classpath:templateengine/elementprocessors/block");

        Assert.assertTrue(executor.isAllOK());

    }



    @Test
    public void testElementMarkupProcessors() throws Exception {

        final TestExecutor executor = new TestExecutor();
        executor.setDialects(Arrays.asList(new IDialect[]{new StandardDialect(), new AggDialect()}));
        executor.execute("classpath:templateengine/elementprocessors/markup");

        Assert.assertTrue(executor.isAllOK());

    }

}
