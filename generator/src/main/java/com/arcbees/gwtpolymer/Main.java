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

import org.apache.commons.io.FileUtils;

import com.arcbees.gwtpolymer.annotations.ElementsPackage;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;

public class Main {
    public static void main(String[] args) throws Exception {
        String currentDirectory = new File("").getAbsolutePath();
        String pathPrefix = "";
        if (currentDirectory.endsWith("generator")) {
            pathPrefix = "../";
        }

        Config config = new Config();
        Injector injector = Guice.createInjector(new GeneratorModule(config, pathPrefix));
        ComponentsGenerator packageGenerator = injector.getInstance(ComponentsGenerator.class);

        deleteOutputFolder(injector);

        packageGenerator.generate();
    }

    private static void deleteOutputFolder(Injector injector) {
        Path elementsPath = injector.getInstance(Key.get(Path.class, ElementsPackage.class));

        delete(elementsPath);
    }

    private static void delete(Path path) {
        File file = path.toFile();
        if (file.exists()) {
            FileUtils.deleteQuietly(file);
        }
    }
}
