/**
 * Copyright 2015 ArcBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.arcbees.gwtpolymer.tasks;

import java.io.File;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.arcbees.gwtpolymer.PolymerComponent;
import com.google.inject.assistedinject.Assisted;

public class ComponentDependenciesCollector implements Task<PolymerComponent> {
    private final Map<String, PolymerComponent> componentNames;

    @Inject
    ComponentDependenciesCollector(
            @Assisted Map<String, PolymerComponent> componentNames) {
        this.componentNames = componentNames;
    }

    @Override
    public void process(PolymerComponent element) throws Exception {
        Set<PolymerComponent> dependencies = new LinkedHashSet<>();

        Path path = element.getComponentPublicDirectory().toPath().getParent();
        for (String elementName : element.getElementNames()) {
            File file = path.resolve(elementName).toFile();
            Document document = Jsoup.parse(file, "UTF-8");

            addLinkDependencies(dependencies, document);
            addScriptDependencies(dependencies, document);
        }

        element.setDependencies(dependencies);
    }

    private void addLinkDependencies(Set<PolymerComponent> dependencies, Document document) {
        Elements links = document.getElementsByTag("link");
        for (Element link : links) {
            String href = link.attr("href");
            String rel = link.attr("rel");
            if ("import".equals(rel)) {
                addDependency(dependencies, href);
            }
        }
    }

    private void addScriptDependencies(Set<PolymerComponent> dependencies, Document document) {
        Elements scripts = document.getElementsByTag("script");
        for (Element script : scripts) {
            String href = script.attr("src");
            addDependency(dependencies, href);
        }
    }

    private void addDependency(Set<PolymerComponent> dependencies, String path) {
        PolymerComponent dependency = componentNames.get(path.replaceFirst("\\.\\./", ""));
        if (dependency != null) {
            dependencies.add(dependency);
        }
    }
}
