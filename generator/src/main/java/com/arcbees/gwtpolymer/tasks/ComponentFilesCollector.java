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

import java.io.File;
import java.io.FileFilter;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.io.filefilter.WildcardFileFilter;

import com.arcbees.gwtpolymer.PolymerComponent;
import com.google.common.collect.FluentIterable;
import com.google.inject.assistedinject.Assisted;

import static org.apache.commons.io.IOCase.INSENSITIVE;

public class ComponentFilesCollector implements Task<PolymerComponent> {
    private final Map<String, PolymerComponent> componentNames;

    @Inject
    ComponentFilesCollector(
            @Assisted Map<String, PolymerComponent> componentNames) {
        this.componentNames = componentNames;
    }

    @Override
    public void process(PolymerComponent element) throws Exception {
        File componentPublicDirectory = element.getComponentPublicDirectory();
        File[] htmlFiles = getHtmlFiles(componentPublicDirectory);

        List<String> elementNames = extractElementNames(htmlFiles, element.getComponentDirectory());
        element.setElementNames(elementNames);

        File[] jsFiles = getJsFiles(componentPublicDirectory);
        List<String> jsElementNames = extractElementNames(jsFiles, componentPublicDirectory);

        addAllElements(element, elementNames);
        addAllElements(element, jsElementNames);
    }

    private void addAllElements(PolymerComponent element, List<String> jsElementNames) {
        for (String jsElementName : jsElementNames) {
            componentNames.put(jsElementName, element);
        }
    }

    private List<String> extractElementNames(
            File[] files,
            final File moduleDirectory) {

        return FluentIterable.of(files)
                .transform(input -> moduleDirectory.getName() + "/" + input.getName()).toList();
    }

    private File[] getHtmlFiles(File componentPublicDirectory) {
        return componentPublicDirectory.listFiles((FileFilter) new WildcardFileFilter("*.html", INSENSITIVE));
    }

    private File[] getJsFiles(File componentPublicDirectory) {
        return componentPublicDirectory.listFiles((FileFilter) new WildcardFileFilter("*.js", INSENSITIVE));
    }
}
