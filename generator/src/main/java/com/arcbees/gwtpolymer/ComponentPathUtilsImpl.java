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

import javax.inject.Inject;

import org.apache.commons.lang.WordUtils;

import com.arcbees.gwtpolymer.annotations.DefaultPackagePath;
import com.arcbees.gwtpolymer.annotations.ElementsPackage;

public class ComponentPathUtilsImpl implements ComponentPathUtils {
    static final String PUBLIC = "public";

    private final String defaultPackagePath;
    private final Path packageDirectory;

    @Inject
    ComponentPathUtilsImpl(
            @DefaultPackagePath String defaultPackagePath,
            @ElementsPackage Path packageDirectory) {
        this.defaultPackagePath = defaultPackagePath;
        this.packageDirectory = packageDirectory;
    }

    @Override
    public Path getTargetDirectory(String name) {
        return packageDirectory.resolve(name.replaceAll("-", ""));
    }

    @Override
    public File getPublicDirectory(Path componentTargetDirectory, String name) {
        return componentTargetDirectory.resolve(PUBLIC)
                .resolve(name)
                .toFile();
    }

    @Override
    public String getFullPackage(Path componentTargetDirectory) {
        return componentTargetDirectory.toString()
                .replace(defaultPackagePath + File.separator, "")
                .replace(File.separator, ".");
    }

    @Override
    public String extractModuleName(String name) {
        return WordUtils.capitalize(name, new char[]{'-'}).replace("-", "");
    }
}
