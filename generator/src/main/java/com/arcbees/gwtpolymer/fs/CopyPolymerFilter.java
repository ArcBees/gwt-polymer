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
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.filefilter.AndFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import static org.apache.commons.io.IOCase.INSENSITIVE;

public class CopyPolymerFilter implements FileFilter {
    static final List<String> NAMES_TO_IGNORE;

    static {
        NAMES_TO_IGNORE = new ArrayList<>();
        NAMES_TO_IGNORE.add("elements.html");
        NAMES_TO_IGNORE.add("my-theme.html");
        NAMES_TO_IGNORE.add("Gruntfile.js");
        NAMES_TO_IGNORE.add("demo.html");
        NAMES_TO_IGNORE.add("README.md");
        NAMES_TO_IGNORE.add("index.html");
        NAMES_TO_IGNORE.add("metadata.html");
        NAMES_TO_IGNORE.add("web-animations*dev*");
        NAMES_TO_IGNORE.add("web-animations-*.html");
    }

    private final FileFilter delegate;

    public CopyPolymerFilter() {
        List<IOFileFilter> filters = new ArrayList<>();
        filters.add(new WildcardFileFilter(new String[]{"*.html", "*.css", "*.js", "*.js.map", "src", "transitions"},
                INSENSITIVE));
        filters.add(new NotFileFilter(new WildcardFileFilter(NAMES_TO_IGNORE, INSENSITIVE)));

        delegate = new AndFileFilter(filters);
    }

    @Override
    public boolean accept(File pathname) {
        return !isWebAnimationJsHtmlFile(pathname) && delegate.accept(pathname);
    }

    private boolean isWebAnimationJsHtmlFile(File file) {
        return "web-animations.html".equals(file.getName()) && file.getParent().endsWith("web-animations-js");
    }
}
