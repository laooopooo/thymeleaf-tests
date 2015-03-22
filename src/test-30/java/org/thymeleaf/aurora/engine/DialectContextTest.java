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
package org.thymeleaf.aurora.engine;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.junit.Assert;
import org.junit.Test;
import org.thymeleaf.aurora.DialectConfiguration;
import org.thymeleaf.aurora.context.DialectContext;
import org.thymeleaf.aurora.dialect.AbstractProcessorDialect;
import org.thymeleaf.aurora.dialect.IProcessorDialect;
import org.thymeleaf.aurora.processor.IProcessor;
import org.thymeleaf.aurora.processor.cdatasection.AbstractCDATASectionProcessor;
import org.thymeleaf.aurora.processor.comment.AbstractCommentProcessor;
import org.thymeleaf.aurora.processor.doctype.AbstractDocTypeProcessor;
import org.thymeleaf.aurora.processor.element.AbstractElementProcessor;
import org.thymeleaf.aurora.processor.node.AbstractNodeProcessor;
import org.thymeleaf.aurora.processor.node.INodeProcessor;
import org.thymeleaf.aurora.processor.processinginstruction.AbstractProcessingInstructionProcessor;
import org.thymeleaf.aurora.processor.text.AbstractTextProcessor;
import org.thymeleaf.aurora.processor.xmldeclaration.AbstractXMLDeclarationProcessor;
import org.thymeleaf.aurora.templatemode.TemplateMode;


public final class DialectContextTest {





    @Test
    public void testProcessorComputation01() {

        final IProcessorDialect dialect =
                buildDialect("standard", "th",
                        "CD-10-cdataone,CD-5-cdatatwo,C-20-comone,E-20-null-src,N-ELEMENT-10-test-null",
                        "CD-5-cdataxml");

        final DialectConfiguration dialectConfiguration = new DialectConfiguration("wo",dialect);
        final DialectContext dialectContext = DialectContext.build(Collections.singleton(dialectConfiguration));

        Assert.assertEquals("[standard,th,[CD-10-cdataone, CD-5-cdatatwo, C-20-comone, E-20-null-{wo:src,data-wo-src}, N-ELEMENT-10-{wo:test,wo-test}-null, CD-5-cdataxml]]", dialect.toString());
        Assert.assertEquals("[CD-5-cdatatwo, CD-10-cdataone]", dialectContext.getCDATASectionProcessors(TemplateMode.HTML).toString());
        Assert.assertEquals("[CD-5-cdataxml]", dialectContext.getCDATASectionProcessors(TemplateMode.XML).toString());

    }


    @Test
    public void testProcessorComputation02() {

        final IProcessorDialect dialect =
                buildHTMLDialect("standard", "TH",
                        "E-20-null-src, E-10-null-src,E-20-null-href,E-20-null-text,E-10-null-text,E-10-*div-text,E-15-*div-src,E-1-form-*action,E-20-form-null,E-10-null-*action,E-50-null-null");

        final DialectConfiguration dialectConfiguration = new DialectConfiguration(dialect);
        final DialectContext dialectContext = DialectContext.build(Collections.singleton(dialectConfiguration));
        final AttributeDefinitions attributeDefinitions = dialectContext.getAttributeDefinitions();
        final ElementDefinitions elementDefinitions = dialectContext.getElementDefinitions();

        Assert.assertEquals("TH", dialect.getPrefix());

        Assert.assertEquals("[E-10-null-{th:src,data-th-src}, E-15-{div}-{th:src,data-th-src}, E-20-null-{th:src,data-th-src}]",attributeDefinitions.forHTMLName("th:src").getAssociatedProcessors().toString());
        Assert.assertEquals("[E-1-{th:form,th-form}-{action}, E-10-null-{action}]",attributeDefinitions.forHTMLName("action").getAssociatedProcessors().toString());
        Assert.assertEquals("[E-20-{th:form,th-form}-null, E-50-null-null]",elementDefinitions.forHTMLName("th","form").getAssociatedProcessors().toString());
        Assert.assertEquals("[]",attributeDefinitions.forHTMLName("th","utext").getAssociatedProcessors().toString());
        Assert.assertEquals("[E-50-null-null]",elementDefinitions.forHTMLName("p").getAssociatedProcessors().toString());

    }


    @Test
    public void testProcessorComputation03() {

        final IProcessorDialect dialect =
                buildDialect("standard", "TH",
                        "E-20-null-src, E-10-null-src,E-20-null-href,E-20-null-text,E-10-null-text,E-10-*div-text,E-15-*div-src,E-1-form-*action,E-20-form-null,E-10-null-*action,E-50-null-null",
                        "E-200-null-src, E-100-null-src,E-200-null-href,E-200-null-text,E-100-null-text,E-100-*div-text,E-150-*div-src,E-10-form-*action,E-200-form-null,E-100-null-*action,E-500-null-null");

        final DialectConfiguration dialectConfiguration = new DialectConfiguration(dialect);
        final DialectContext dialectContext = DialectContext.build(Collections.singleton(dialectConfiguration));
        final AttributeDefinitions attributeDefinitions = dialectContext.getAttributeDefinitions();
        final ElementDefinitions elementDefinitions = dialectContext.getElementDefinitions();

        Assert.assertEquals("TH", dialect.getPrefix());

        Assert.assertEquals("[E-10-null-{th:src,data-th-src}, E-15-{div}-{th:src,data-th-src}, E-20-null-{th:src,data-th-src}]",attributeDefinitions.forHTMLName("th:src").getAssociatedProcessors().toString());
        Assert.assertEquals("[E-1-{th:form,th-form}-{action}, E-10-null-{action}]",attributeDefinitions.forHTMLName("action").getAssociatedProcessors().toString());
        Assert.assertEquals("[E-20-{th:form,th-form}-null, E-50-null-null]",elementDefinitions.forHTMLName("th","form").getAssociatedProcessors().toString());
        Assert.assertEquals("[]",attributeDefinitions.forHTMLName("th","utext").getAssociatedProcessors().toString());
        Assert.assertEquals("[E-50-null-null]",elementDefinitions.forHTMLName("p").getAssociatedProcessors().toString());

        Assert.assertEquals("[]",attributeDefinitions.forXMLName("th:src").getAssociatedProcessors().toString());
        Assert.assertEquals("[E-100-null-{TH:src}, E-150-{div}-{TH:src}, E-200-null-{TH:src}]",attributeDefinitions.forXMLName("TH:src").getAssociatedProcessors().toString());
        Assert.assertEquals("[E-10-{TH:form}-{action}, E-100-null-{action}]",attributeDefinitions.forXMLName("action").getAssociatedProcessors().toString());
        Assert.assertEquals("[E-200-{TH:form}-null, E-500-null-null]",elementDefinitions.forXMLName("TH", "form").getAssociatedProcessors().toString());
        Assert.assertEquals("[]",attributeDefinitions.forXMLName("th", "utext").getAssociatedProcessors().toString());
        Assert.assertEquals("[]",attributeDefinitions.forXMLName("TH", "utext").getAssociatedProcessors().toString());
        Assert.assertEquals("[E-500-null-null]",elementDefinitions.forXMLName("p").getAssociatedProcessors().toString());

    }


    @Test
    public void testProcessorComputation04() {

        final IProcessorDialect dialect =
                buildDialect("standard", "TH",
                        "N-ELEMENT-20-null-src, N-ELEMENT-10-null-src,N-ELEMENT-20-null-href,N-ELEMENT-20-null-text,N-ELEMENT-10-null-text,N-ELEMENT-10-*div-text,N-ELEMENT-15-*div-src,N-ELEMENT-1-form-*action,N-ELEMENT-20-form-null,N-ELEMENT-10-null-*action,N-ELEMENT-50-null-null",
                        "N-ELEMENT-200-null-src, N-ELEMENT-100-null-src,N-ELEMENT-200-null-href,N-ELEMENT-200-null-text,N-ELEMENT-100-null-text,N-ELEMENT-100-*div-text,N-ELEMENT-150-*div-src,N-ELEMENT-10-form-*action,N-ELEMENT-200-form-null,N-ELEMENT-100-null-*action,N-ELEMENT-500-null-null");

        final DialectConfiguration dialectConfiguration = new DialectConfiguration(dialect);
        final DialectContext dialectContext = DialectContext.build(Collections.singleton(dialectConfiguration));
        final AttributeDefinitions attributeDefinitions = dialectContext.getAttributeDefinitions();
        final ElementDefinitions elementDefinitions = dialectContext.getElementDefinitions();

        Assert.assertEquals("TH", dialect.getPrefix());

        Assert.assertEquals("[N-ELEMENT-10-null-{th:src,data-th-src}, N-ELEMENT-15-{div}-{th:src,data-th-src}, N-ELEMENT-20-null-{th:src,data-th-src}]",attributeDefinitions.forHTMLName("th:src").getAssociatedProcessors().toString());
        Assert.assertEquals("[N-ELEMENT-1-{th:form,th-form}-{action}, N-ELEMENT-10-null-{action}]",attributeDefinitions.forHTMLName("action").getAssociatedProcessors().toString());
        Assert.assertEquals("[N-ELEMENT-20-{th:form,th-form}-null, N-ELEMENT-50-null-null]",elementDefinitions.forHTMLName("th","form").getAssociatedProcessors().toString());
        Assert.assertEquals("[]",attributeDefinitions.forHTMLName("th","utext").getAssociatedProcessors().toString());
        Assert.assertEquals("[N-ELEMENT-50-null-null]",elementDefinitions.forHTMLName("p").getAssociatedProcessors().toString());

        Assert.assertEquals("[]",attributeDefinitions.forXMLName("th:src").getAssociatedProcessors().toString());
        Assert.assertEquals("[N-ELEMENT-100-null-{TH:src}, N-ELEMENT-150-{div}-{TH:src}, N-ELEMENT-200-null-{TH:src}]",attributeDefinitions.forXMLName("TH:src").getAssociatedProcessors().toString());
        Assert.assertEquals("[N-ELEMENT-10-{TH:form}-{action}, N-ELEMENT-100-null-{action}]",attributeDefinitions.forXMLName("action").getAssociatedProcessors().toString());
        Assert.assertEquals("[N-ELEMENT-200-{TH:form}-null, N-ELEMENT-500-null-null]",elementDefinitions.forXMLName("TH", "form").getAssociatedProcessors().toString());
        Assert.assertEquals("[]",attributeDefinitions.forXMLName("th", "utext").getAssociatedProcessors().toString());
        Assert.assertEquals("[]",attributeDefinitions.forXMLName("TH", "utext").getAssociatedProcessors().toString());
        Assert.assertEquals("[N-ELEMENT-500-null-null]",elementDefinitions.forXMLName("p").getAssociatedProcessors().toString());

    }


    @Test
    public void testProcessorComputation05() {

        final IProcessorDialect dialect =
                buildDialect("standard", "TH",
                        "N-ELEMENT-20-null-src, N-CDATA_SECTION-10-some,N-DOC_TYPE-20-other,E-20-null-text,E-10-null-text,N-TEXT-10-whoa!,E-15-*div-src,E-1-form-*action,N-ELEMENT-20-form-null,E-10-null-*action,E-50-null-null,N-CDATA_SECTION-5-someother,T-25-uye,T-10-eo",
                        "N-TEXT-20-whoaX!,T-10-eoX");

        final DialectConfiguration dialectConfiguration = new DialectConfiguration(dialect);
        final DialectContext dialectContext = DialectContext.build(Collections.singleton(dialectConfiguration));
        final AttributeDefinitions attributeDefinitions = dialectContext.getAttributeDefinitions();
        final ElementDefinitions elementDefinitions = dialectContext.getElementDefinitions();

        Assert.assertEquals("TH", dialect.getPrefix());

        Assert.assertEquals("[E-15-{div}-{th:src,data-th-src}, N-ELEMENT-20-null-{th:src,data-th-src}]",attributeDefinitions.forHTMLName("th:src").getAssociatedProcessors().toString());
        Assert.assertEquals("[E-1-{th:form,th-form}-{action}, E-10-null-{action}]",attributeDefinitions.forHTMLName("action").getAssociatedProcessors().toString());
        Assert.assertEquals("[N-ELEMENT-20-{th:form,th-form}-null, E-50-null-null]",elementDefinitions.forHTMLName("th", "form").getAssociatedProcessors().toString());
        Assert.assertEquals("[]",attributeDefinitions.forHTMLName("th", "utext").getAssociatedProcessors().toString());
        Assert.assertEquals("[E-50-null-null]",elementDefinitions.forHTMLName("p").getAssociatedProcessors().toString());
        Assert.assertEquals("[N-CDATA_SECTION-5-someother, N-CDATA_SECTION-10-some]",dialectContext.getCDATASectionProcessors(TemplateMode.HTML).toString());
        Assert.assertEquals("[N-TEXT-10-whoa!, T-10-eo, T-25-uye]",dialectContext.getTextProcessors(TemplateMode.HTML).toString());
        Assert.assertEquals("[T-10-eoX, N-TEXT-20-whoaX!]",dialectContext.getTextProcessors(TemplateMode.XML).toString());
        Assert.assertEquals("[N-DOC_TYPE-20-other]",dialectContext.getDocTypeProcessors(TemplateMode.HTML).toString());

        Assert.assertEquals("[]",dialectContext.getCommentProcessors(TemplateMode.HTML).toString());
        Assert.assertEquals("[]",dialectContext.getProcessingInstructionProcessors(TemplateMode.HTML).toString());
        Assert.assertEquals("[]",dialectContext.getXMLDeclarationProcessors(TemplateMode.HTML).toString());

        Assert.assertEquals("[]",dialectContext.getCDATASectionProcessors(TemplateMode.XML).toString());
        Assert.assertEquals("[]",dialectContext.getCommentProcessors(TemplateMode.XML).toString());
        Assert.assertEquals("[]",dialectContext.getDocTypeProcessors(TemplateMode.XML).toString());
        Assert.assertEquals("[]",dialectContext.getProcessingInstructionProcessors(TemplateMode.XML).toString());
        Assert.assertEquals("[]",dialectContext.getXMLDeclarationProcessors(TemplateMode.XML).toString());

    }




    @Test
    public void testProcessorComputation06() {

        final IProcessorDialect dialect =
                buildHTMLDialect("standard", "TH",
                        "N-CDATA_SECTION-10-some,CD-4-other,N-COMMENT-10-some,C-4-other,N-DOC_TYPE-10-some,DT-4-other,N-PROCESSING_INSTRUCTION-10-some,PI-4-other,N-TEXT-10-some,T-4-other,N-XML_DECLARATION-10-some,XD-4-other");

        final DialectConfiguration dialectConfiguration = new DialectConfiguration(dialect);
        final DialectContext dialectContext = DialectContext.build(Collections.singleton(dialectConfiguration));

        Assert.assertEquals("TH", dialect.getPrefix());

        Assert.assertEquals("[CD-4-other, N-CDATA_SECTION-10-some]",dialectContext.getCDATASectionProcessors(TemplateMode.HTML).toString());
        Assert.assertEquals("[C-4-other, N-COMMENT-10-some]",dialectContext.getCommentProcessors(TemplateMode.HTML).toString());
        Assert.assertEquals("[DT-4-other, N-DOC_TYPE-10-some]",dialectContext.getDocTypeProcessors(TemplateMode.HTML).toString());
        Assert.assertEquals("[PI-4-other, N-PROCESSING_INSTRUCTION-10-some]",dialectContext.getProcessingInstructionProcessors(TemplateMode.HTML).toString());
        Assert.assertEquals("[T-4-other, N-TEXT-10-some]",dialectContext.getTextProcessors(TemplateMode.HTML).toString());
        Assert.assertEquals("[XD-4-other, N-XML_DECLARATION-10-some]",dialectContext.getXMLDeclarationProcessors(TemplateMode.HTML).toString());

        Assert.assertEquals("[]",dialectContext.getCDATASectionProcessors(TemplateMode.XML).toString());
        Assert.assertEquals("[]",dialectContext.getCommentProcessors(TemplateMode.XML).toString());
        Assert.assertEquals("[]",dialectContext.getDocTypeProcessors(TemplateMode.XML).toString());
        Assert.assertEquals("[]",dialectContext.getProcessingInstructionProcessors(TemplateMode.XML).toString());
        Assert.assertEquals("[]",dialectContext.getTextProcessors(TemplateMode.XML).toString());
        Assert.assertEquals("[]",dialectContext.getXMLDeclarationProcessors(TemplateMode.XML).toString());

    }




    @Test
    public void testProcessorComputation07() {

        final IProcessorDialect dialect =
                buildXMLDialect("standard", "TH",
                        "N-CDATA_SECTION-10-some,CD-4-other,N-COMMENT-10-some,C-4-other,N-DOC_TYPE-10-some,DT-4-other,N-PROCESSING_INSTRUCTION-10-some,PI-4-other,N-TEXT-10-some,T-4-other,N-XML_DECLARATION-10-some,XD-4-other");

        final DialectConfiguration dialectConfiguration = new DialectConfiguration(dialect);
        final DialectContext dialectContext = DialectContext.build(Collections.singleton(dialectConfiguration));

        Assert.assertEquals("TH", dialect.getPrefix());

        Assert.assertEquals("[CD-4-other, N-CDATA_SECTION-10-some]",dialectContext.getCDATASectionProcessors(TemplateMode.XML).toString());
        Assert.assertEquals("[C-4-other, N-COMMENT-10-some]",dialectContext.getCommentProcessors(TemplateMode.XML).toString());
        Assert.assertEquals("[DT-4-other, N-DOC_TYPE-10-some]",dialectContext.getDocTypeProcessors(TemplateMode.XML).toString());
        Assert.assertEquals("[PI-4-other, N-PROCESSING_INSTRUCTION-10-some]",dialectContext.getProcessingInstructionProcessors(TemplateMode.XML).toString());
        Assert.assertEquals("[T-4-other, N-TEXT-10-some]",dialectContext.getTextProcessors(TemplateMode.XML).toString());
        Assert.assertEquals("[XD-4-other, N-XML_DECLARATION-10-some]",dialectContext.getXMLDeclarationProcessors(TemplateMode.XML).toString());

        Assert.assertEquals("[]",dialectContext.getCDATASectionProcessors(TemplateMode.HTML).toString());
        Assert.assertEquals("[]",dialectContext.getCommentProcessors(TemplateMode.HTML).toString());
        Assert.assertEquals("[]",dialectContext.getDocTypeProcessors(TemplateMode.HTML).toString());
        Assert.assertEquals("[]",dialectContext.getProcessingInstructionProcessors(TemplateMode.HTML).toString());
        Assert.assertEquals("[]",dialectContext.getTextProcessors(TemplateMode.HTML).toString());
        Assert.assertEquals("[]",dialectContext.getXMLDeclarationProcessors(TemplateMode.HTML).toString());

    }




    @Test
    public void testProcessorComputation08() {

        final IProcessorDialect dialect =
                buildDialect("standard", "TH",
                        "N-CDATA_SECTION-10-some,CD-4-other,N-COMMENT-10-some,C-4-other,N-DOC_TYPE-10-some,DT-4-other,N-PROCESSING_INSTRUCTION-10-some,PI-4-other,N-TEXT-10-some,T-4-other,N-XML_DECLARATION-10-some,XD-4-other",
                        "N-CDATA_SECTION-100-some,CD-40-other,N-COMMENT-100-some,C-40-other,N-DOC_TYPE-100-some,DT-40-other,N-PROCESSING_INSTRUCTION-100-some,PI-40-other,N-TEXT-100-some,T-40-other,N-XML_DECLARATION-100-some,XD-40-other");

        final DialectConfiguration dialectConfiguration = new DialectConfiguration(dialect);
        final DialectContext dialectContext = DialectContext.build(Collections.singleton(dialectConfiguration));

        Assert.assertEquals("TH", dialect.getPrefix());

        Assert.assertEquals("[CD-4-other, N-CDATA_SECTION-10-some]",dialectContext.getCDATASectionProcessors(TemplateMode.HTML).toString());
        Assert.assertEquals("[C-4-other, N-COMMENT-10-some]",dialectContext.getCommentProcessors(TemplateMode.HTML).toString());
        Assert.assertEquals("[DT-4-other, N-DOC_TYPE-10-some]",dialectContext.getDocTypeProcessors(TemplateMode.HTML).toString());
        Assert.assertEquals("[PI-4-other, N-PROCESSING_INSTRUCTION-10-some]",dialectContext.getProcessingInstructionProcessors(TemplateMode.HTML).toString());
        Assert.assertEquals("[T-4-other, N-TEXT-10-some]",dialectContext.getTextProcessors(TemplateMode.HTML).toString());
        Assert.assertEquals("[XD-4-other, N-XML_DECLARATION-10-some]",dialectContext.getXMLDeclarationProcessors(TemplateMode.HTML).toString());

        Assert.assertEquals("[CD-40-other, N-CDATA_SECTION-100-some]",dialectContext.getCDATASectionProcessors(TemplateMode.XML).toString());
        Assert.assertEquals("[C-40-other, N-COMMENT-100-some]",dialectContext.getCommentProcessors(TemplateMode.XML).toString());
        Assert.assertEquals("[DT-40-other, N-DOC_TYPE-100-some]",dialectContext.getDocTypeProcessors(TemplateMode.XML).toString());
        Assert.assertEquals("[PI-40-other, N-PROCESSING_INSTRUCTION-100-some]",dialectContext.getProcessingInstructionProcessors(TemplateMode.XML).toString());
        Assert.assertEquals("[T-40-other, N-TEXT-100-some]",dialectContext.getTextProcessors(TemplateMode.XML).toString());
        Assert.assertEquals("[XD-40-other, N-XML_DECLARATION-100-some]",dialectContext.getXMLDeclarationProcessors(TemplateMode.XML).toString());

    }







    private static ProcessorAggregationTestDialect buildDialect(
            final String name, final String prefix, final String htmlProcSpecification, final String xmlProcSpecification) {
        return ProcessorAggregationTestDialect.build(name, prefix, htmlProcSpecification, xmlProcSpecification);
    }

    private static ProcessorAggregationTestDialect buildHTMLDialect(
            final String name, final String prefix, final String htmlProcSpecification) {
        return buildDialect(name, prefix, htmlProcSpecification, "");
    }

    private static ProcessorAggregationTestDialect buildXMLDialect(
            final String name, final String prefix, final String xmlProcSpecification) {
        return buildDialect(name, prefix, "", xmlProcSpecification);
    }


    private static class ProcessorAggregationTestDialect extends AbstractProcessorDialect {


        static ProcessorAggregationTestDialect build(final String name, final String prefix,
                                              final String htmlProcSpecification, final String xmlProcSpecification) {


            final Set<IProcessor> processors = new LinkedHashSet<IProcessor>();
            processors.addAll(buildProcessors(TemplateMode.HTML, htmlProcSpecification));
            processors.addAll(buildProcessors(TemplateMode.XML, xmlProcSpecification));
            return new ProcessorAggregationTestDialect(name, prefix, processors);

        }


        static Set<IProcessor> buildProcessors(final TemplateMode templateMode, final String specification) {

            final Set<IProcessor> processors = new LinkedHashSet<IProcessor>();
            final StringTokenizer specTok = new StringTokenizer(specification,", ");

            while (specTok.hasMoreTokens()) {

                final String procSpec = specTok.nextToken();

                final StringTokenizer procSpecTok = new StringTokenizer(procSpec,"-");

                final String type = procSpecTok.nextToken();
                if (type.equals("CD")) {
                    final int precedence = Integer.valueOf(procSpecTok.nextToken());
                    processors.add(
                            new CDATASectionProcessorAggregationTestProcessor(
                                    procSpecTok.nextToken(), templateMode, precedence));
                } else if (type.equals("C")) {
                    final int precedence = Integer.valueOf(procSpecTok.nextToken());
                    processors.add(
                            new CommentProcessorAggregationTestProcessor(
                                    procSpecTok.nextToken(), templateMode, precedence));
                } else if (type.equals("DT")) {
                    final int precedence = Integer.valueOf(procSpecTok.nextToken());
                    processors.add(
                            new DocTypeProcessorAggregationTestProcessor(
                                    procSpecTok.nextToken(), templateMode, precedence));
                } else if (type.equals("PI")) {
                    final int precedence = Integer.valueOf(procSpecTok.nextToken());
                    processors.add(
                            new ProcessingInstructionProcessorAggregationTestProcessor(
                                    procSpecTok.nextToken(), templateMode, precedence));
                } else if (type.equals("T")) {
                    final int precedence = Integer.valueOf(procSpecTok.nextToken());
                    processors.add(
                            new TextProcessorAggregationTestProcessor(
                                    procSpecTok.nextToken(), templateMode, precedence));
                } else if (type.equals("XD")) {
                    final int precedence = Integer.valueOf(procSpecTok.nextToken());
                    processors.add(
                            new XMLDeclarationProcessorAggregationTestProcessor(
                                    procSpecTok.nextToken(), templateMode, precedence));
                } else if (type.equals("E")) {
                    final int precedence = Integer.valueOf(procSpecTok.nextToken());
                    String elementName = procSpecTok.nextToken();
                    boolean prefixElementName = true;
                    if (elementName.startsWith("*")) {
                        prefixElementName = false;
                        elementName = elementName.substring(1);
                    }
                    String attributeName = procSpecTok.nextToken();
                    boolean prefixAttributeName = true;
                    if (attributeName.startsWith("*")) {
                        prefixAttributeName = false;
                        attributeName = attributeName.substring(1);
                    }
                    processors.add(
                            new ElementProcessorAggregationTestProcessor(
                                    (elementName.equals("null")? null : elementName), prefixElementName,
                                    (attributeName.equals("null")? null : attributeName), prefixAttributeName,
                                    templateMode, precedence));
                } else if (type.equals("N")) {
                    final INodeProcessor.MatchingNodeType matchingNodeType = INodeProcessor.MatchingNodeType.valueOf(procSpecTok.nextToken());
                    final int precedence = Integer.valueOf(procSpecTok.nextToken());
                    switch(matchingNodeType) {

                        case CDATA_SECTION:
                            processors.add(
                                    new NodeProcessorAggregationTestProcessor(
                                            matchingNodeType, procSpecTok.nextToken(), templateMode, precedence));
                            break;
                        case COMMENT:
                            processors.add(
                                    new NodeProcessorAggregationTestProcessor(
                                            matchingNodeType, procSpecTok.nextToken(), templateMode, precedence));
                            break;
                        case DOC_TYPE:
                            processors.add(
                                    new NodeProcessorAggregationTestProcessor(
                                            matchingNodeType, procSpecTok.nextToken(), templateMode, precedence));
                            break;
                        case ELEMENT:
                            String elementName = procSpecTok.nextToken();
                            boolean prefixElementName = true;
                            if (elementName.startsWith("*")) {
                                prefixElementName = false;
                                elementName = elementName.substring(1);
                            }
                            String attributeName = procSpecTok.nextToken();
                            boolean prefixAttributeName = true;
                            if (attributeName.startsWith("*")) {
                                prefixAttributeName = false;
                                attributeName = attributeName.substring(1);
                            }
                            processors.add(
                                    new NodeProcessorAggregationTestProcessor(
                                            matchingNodeType,
                                            (elementName.equals("null")? null : elementName), prefixElementName,
                                            (attributeName.equals("null")? null : attributeName), prefixAttributeName,
                                            templateMode, precedence));
                            break;
                        case PROCESSING_INSTRUCTION:
                            processors.add(
                                    new NodeProcessorAggregationTestProcessor(
                                            matchingNodeType, procSpecTok.nextToken(), templateMode, precedence));
                            break;
                        case TEXT:
                            processors.add(
                                    new NodeProcessorAggregationTestProcessor(
                                            matchingNodeType, procSpecTok.nextToken(), templateMode, precedence));
                            break;
                        case XML_DECLARATION:
                            processors.add(
                                    new NodeProcessorAggregationTestProcessor(
                                            matchingNodeType, procSpecTok.nextToken(), templateMode, precedence));
                            break;

                    }
                } else {
                    throw new IllegalArgumentException("Unrecognized: " + type);
                }

            }

            return processors;

        }


        protected ProcessorAggregationTestDialect(final String name, final String prefix, final Set<IProcessor> processors) {
            super(name, prefix, processors);
        }


        public String toString() {
            return "[" + getName() + "," + getPrefix() + "," + getProcessors() + "]";
        }



        static interface NamedTestProcessor {
            String getName();
        }


        private static class CDATASectionProcessorAggregationTestProcessor extends AbstractCDATASectionProcessor implements NamedTestProcessor {

            private final String name;

            CDATASectionProcessorAggregationTestProcessor(final String name, final TemplateMode templateMode, final int precedence) {
                super(templateMode, precedence);
                this.name = name;
            }

            public String getName() {
                return "CD-" + getPrecedence() + "-" + this.name;
            }

            public String toString() {
                return getName();
            }

        }


        private static class CommentProcessorAggregationTestProcessor extends AbstractCommentProcessor implements NamedTestProcessor {

            private final String name;

            CommentProcessorAggregationTestProcessor(final String name, final TemplateMode templateMode, final int precedence) {
                super(templateMode, precedence);
                this.name = name;
            }

            public String getName() {
                return "C-" + getPrecedence() + "-" + this.name;
            }

            public String toString() {
                return getName();
            }

        }


        private static class DocTypeProcessorAggregationTestProcessor extends AbstractDocTypeProcessor implements NamedTestProcessor {

            private final String name;

            DocTypeProcessorAggregationTestProcessor(final String name, final TemplateMode templateMode, final int precedence) {
                super(templateMode, precedence);
                this.name = name;
            }

            public String getName() {
                return "DT-" + getPrecedence() + "-" + this.name;
            }

            public String toString() {
                return getName();
            }

        }


        private static class ProcessingInstructionProcessorAggregationTestProcessor extends AbstractProcessingInstructionProcessor implements NamedTestProcessor {

            private final String name;

            ProcessingInstructionProcessorAggregationTestProcessor(final String name, final TemplateMode templateMode, final int precedence) {
                super(templateMode, precedence);
                this.name = name;
            }

            public String getName() {
                return "PI-" + getPrecedence() + "-" + this.name;
            }

            public String toString() {
                return getName();
            }

        }


        private static class TextProcessorAggregationTestProcessor extends AbstractTextProcessor implements NamedTestProcessor {

            private final String name;

            TextProcessorAggregationTestProcessor(final String name, final TemplateMode templateMode, final int precedence) {
                super(templateMode, precedence);
                this.name = name;
            }

            public String getName() {
                return "T-" + getPrecedence() + "-" + this.name;
            }

            public String toString() {
                return getName();
            }

        }


        private static class XMLDeclarationProcessorAggregationTestProcessor extends AbstractXMLDeclarationProcessor implements NamedTestProcessor {

            private final String name;

            XMLDeclarationProcessorAggregationTestProcessor(final String name, final TemplateMode templateMode, final int precedence) {
                super(templateMode, precedence);
                this.name = name;
            }

            public String getName() {
                return "XD-" + getPrecedence() + "-" + this.name;
            }

            public String toString() {
                return getName();
            }

        }


        private static class ElementProcessorAggregationTestProcessor extends AbstractElementProcessor implements NamedTestProcessor {

            ElementProcessorAggregationTestProcessor(
                    final String elementName, final boolean prefixElementName,
                    final String attributeName, final boolean prefixAttributeName,
                    final TemplateMode templateMode, final int precedence) {
                super(templateMode, elementName, prefixElementName, attributeName, prefixAttributeName, precedence);
            }

            public String getName() {
                return "E-" + getPrecedence() + "-" + this.getMatchingElementName() + "-" + this.getMatchingAttributeName();
            }

            public String toString() {
                return getName();
            }

        }


        private static class NodeProcessorAggregationTestProcessor extends AbstractNodeProcessor implements NamedTestProcessor {

            private final String name;

            NodeProcessorAggregationTestProcessor(
                    final MatchingNodeType matchingNodeType,
                    final String elementName, final boolean prefixElementName,
                    final String attributeName, final boolean prefixAttributeName,
                    final TemplateMode templateMode, final int precedence) {
                super(matchingNodeType, templateMode, elementName, prefixElementName, attributeName, prefixAttributeName, precedence);
                this.name = null;
            }

            NodeProcessorAggregationTestProcessor(
                    final MatchingNodeType matchingNodeType, final String name, final TemplateMode templateMode, final int precedence) {
                super(matchingNodeType, templateMode, null, false, null, false, precedence);
                this.name = name;
            }

            public String getName() {
                if (getMatchingNodeType() == MatchingNodeType.ELEMENT) {
                    return "N-" + this.getMatchingNodeType() + "-" + getPrecedence() + "-" + this.getMatchingElementName() + "-" + this.getMatchingAttributeName();
                }
                return "N-" + this.getMatchingNodeType() + "-" + getPrecedence() + "-" + this.name;
            }

            public String toString() {
                return getName();
            }

        }


    }



}