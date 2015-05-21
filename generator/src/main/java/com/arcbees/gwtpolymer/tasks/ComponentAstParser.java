/**
 * Copyright 2015 ArcBees Inc.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.arcbees.gwtpolymer.tasks;

import javax.inject.Inject;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.arcbees.gwtpolymer.JsDocParser;
import com.arcbees.gwtpolymer.PolymerComponent;
import com.google.inject.assistedinject.Assisted;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.parsing.Config;
import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SimpleErrorReporter;
import com.google.javascript.rhino.Token;

public class ComponentAstParser implements Task<PolymerComponent> {
    private static final String POLYMER_ELEMENT_TAG = "polymer-element";
    private static final String NAME = "name";
    private static final String SCRIPT = "script";

    private final JsDocParser jsDocParser;
    private final Document document;

    @Inject
    ComponentAstParser(
            JsDocParser jsDocParser,
            @Assisted Document document) {
        this.jsDocParser = jsDocParser;
        this.document = document;
    }

    @Override
    public void process(PolymerComponent element) throws Exception {
        if (element.isAstParsed()) {
            return;
        }

        Elements polymerElements = document.body().getElementsByTag(POLYMER_ELEMENT_TAG);
        if (polymerElements.size() != 1) {
            return;
        }

        Element polymerElement = polymerElements.iterator().next();

        String elementName = polymerElement.attr(NAME);
        if (elementName.equals("core-ajax")) {
            Elements scripts = polymerElement.getElementsByTag(SCRIPT);
            for (Element script : scripts) {
                String content = script.html();
//                AstRoot root = parser.parse(content, "", 1);
                Config config = ParserRunner.createConfig(true, Config.LanguageMode.ECMASCRIPT6, true, null);
//                Parser.Config config = new Parser.Config(Parser.Config.Mode.ES6);
                SourceFile sourceFile = SourceFile.fromCode(elementName, content);
                ParserRunner.ParseResult result =
                        ParserRunner.parse(sourceFile, content, config, new SimpleErrorReporter());
//                result.ast.children().forEach((n) -> parseNode(n));
                result.ast.children().forEach((n) -> parseNode2(n));
//                root.getStatements().forEach((n) -> parseNode(parser, n));
            }
        }

        element.setAstParsed(true);
//        Path path = element.getComponentPublicDirectory().toPath().getParent();
//        for (String elementName : element.getElementNames()) {
//            Parser parser = parserProvider.get();
//            File file = path.resolve(elementName).toFile();
//            try (FileReader fileReader = new FileReader(file)) {
//                AstRoot root = parser.parse(fileReader, file.getPath(), 1);
//                System.out.println(file.getPath());
//                print(root, root.debugPrint());
////                root.getStatements().forEach(this::parseNode);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

    private void parseNode2(Node node) {
//        System.out.println("====");
//        System.out.println("*** " + Token.name(node.getType()));

        if (node.isFunction()) {
//            System.out.println(node.toStringTree());
            ParsedMethod parsedMethod = parseMethod(node);
        } else {
            for (int i = 0; i < node.getChildCount(); i++) {
                parseNode2(node.getChildAtIndex(i));
            }

            if (node.getNext() != null) {
                parseNode2(node.getNext());
            }
        }
    }

    private ParsedMethod parseMethod(Node node) {
        ParsedMethod method = new ParsedMethod();

        System.out.println(node.getParent().toStringTree());
//        node.children().forEach((n) -> parseMethod(method, n));

        return method;
    }

    private void parseMethod(ParsedMethod method, Node node) {
        System.out.println(node.toStringTree());
//        System.out.println(node.toString());
        if (node.isName()) {
//            System.out.println("Function name : " + node.toString());
        }
    }

    private void parseNode(Node node) {
        JSDocInfo docInfo = node.getJSDocInfo();
        if (docInfo != null) {
            String description = docInfo.getBlockDescription();
        }

        if (node.isVar()) {
        }
        System.out.println("====");
        System.out.println("*** " + Token.name(node.getType()));

        for (int i = 0; i < node.getChildCount(); i++) {
            parseNode(node.getChildAtIndex(i));
        }
    }

//    private void parseNode(AstNode node) {
//        Comment jsDocNode = node.getJsDocNode();
//        if (jsDocNode != null) {
//            jsDocParser.parse(jsDocNode.getValue());
//        }
//        if (node instanceof ExpressionStatement) {
//            parseNode(parser, ((ExpressionStatement) node).getExpression());
//        } else if (node instanceof Name && node.getParent() instanceof ObjectProperty) {
//            print(node, ((Name) node).getIdentifier());
//        } else if (node instanceof Name && node.getParent() instanceof FunctionCall) {
//            print(node, ((Name) node).getIdentifier());
//        } else if (node instanceof FunctionCall) {
//            FunctionCall functionCall = (FunctionCall) node;
//            parseNode(parser, functionCall.getTarget());
//            functionCall.getArguments().forEach((n) -> parseNode(parser, n));
//        } else if (node instanceof PropertyGet) {
//        } else if (node instanceof StringLiteral) {
//            print(node, ((StringLiteral) node).getValue());
//        } else if (node instanceof ObjectLiteral) {
//            ((ObjectLiteral) node).getElements().forEach((n) -> parseNode(parser, n));
//        } else if (node instanceof NumberLiteral) {
//        } else if (node instanceof FunctionNode) {
//        } else if (node instanceof KeywordLiteral) {
//            print(node, ((KeywordLiteral) node).isBooleanLiteral());
//            print(node, Token.typeToName(node.getType()));
//        } else if (node instanceof ObjectProperty) {
//            parseNode(parser, ((ObjectProperty) node).getLeft());
//            parseNode(parser, ((ObjectProperty) node).getRight());
//        } else if (node instanceof Comment) {
//        } else if (node instanceof LabeledStatement) {
//            print(node, node.getClass().getName());
//        } else {
//            System.out.println(node.getClass());
//        }
//    }

//    private void print(AstNode node, Object value) {
//        if (node != null) {
//            System.out.println(node.getClass().getSimpleName() + " : " + value);
//        }
//    }
}
