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

package com.arcbees.gwtpolymer.fs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.arcbees.gwtpolymer.PolymerComponent;
import com.arcbees.gwtpolymer.PolymerComponentFactory;

public class PolymerComponentFileTraverser implements FileTraverser {
    static final List<String> PATHS_TO_IGNORE;

    static {
        PATHS_TO_IGNORE = new ArrayList<>();
        PATHS_TO_IGNORE.add("google-code-prettify");
        PATHS_TO_IGNORE.add("polymer-test-tools");
        PATHS_TO_IGNORE.add("core-tests");
    }

    private final PolymerComponentFactory componentFactory;

    @Inject
    PolymerComponentFileTraverser(
            PolymerComponentFactory componentFactory) {
        this.componentFactory = componentFactory;
    }

    @Override
    public List<PolymerComponent> traverse(File directory) {
        List<PolymerComponent> components = new ArrayList<>();
        for (File file : directory.listFiles()) {
            if (file.isDirectory() && !isIgnoredFolder(file)) {
                components.add(componentFactory.create(file));
            }
        }

        return components;
    }

    private boolean isIgnoredFolder(File file) {
        for (String path : PATHS_TO_IGNORE) {
            if (file.getPath().contains(path)) {
                return true;
            }
        }

        return false;
    }
}
