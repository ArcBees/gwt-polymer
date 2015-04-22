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

package com.arcbees.gwtpolymer;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import com.google.inject.assistedinject.Assisted;

public class PolymerComponent {
    private final File componentDirectory;
    private final Path componentTargetDirectory;
    private final File componentPublicDirectory;
    private final String moduleName;
    private final String fullPackage;

    private List<String> elementNames;
    private Set<PolymerComponent> dependencies;

    @Inject
    PolymerComponent(
            ComponentPathUtils componentPathUtils,
            @Assisted File componentDirectory) {
        this.componentDirectory = componentDirectory;
        String name = componentDirectory.getName();
        componentTargetDirectory = componentPathUtils.getTargetDirectory(name);
        componentPublicDirectory = componentPathUtils.getPublicDirectory(componentTargetDirectory, name);
        moduleName = componentPathUtils.extractModuleName(name);
        fullPackage = componentPathUtils.getFullPackage(componentTargetDirectory);
    }

    public File getComponentDirectory() {
        return componentDirectory;
    }

    public Path getComponentTargetDirectory() {
        return componentTargetDirectory;
    }

    public File getComponentPublicDirectory() {
        return componentPublicDirectory;
    }

    public void setElementNames(List<String> elementNames) {
        this.elementNames = elementNames;
    }

    public List<String> getElementNames() {
        return elementNames;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getFullPackage() {
        return fullPackage;
    }

    public Set<PolymerComponent> getDependencies() {
        return dependencies;
    }

    public void setDependencies(Set<PolymerComponent> dependencies) {
        this.dependencies = dependencies;
    }
}
