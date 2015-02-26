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

import org.junit.Before;
import org.junit.Test;

import static java.io.File.separator;

import static org.assertj.core.api.BDDAssertions.then;

import static com.arcbees.gwtpolymer.ComponentPathUtilsImpl.PUBLIC;

public class ComponentPathUtilsImplTest {
    private static final String DEFAULT_PACKAGE_PATH = "some/path/to/code";
    private static final String APP_PACKAGE = "/com/package/app";
    private static final String APP_PACKAGE_DOT = "com.package.app.";
    private static final String COMPONENT_NAME = "abc-def-ghi";
    private static final String EXPECTED_PACKAGE_NAME = "abcdefghi";
    private static final String EXPECTED_MODULE_NAME = "AbcDefGhi";

    private ComponentPathUtilsImpl utils;

    @Before
    public void setUp() {
        File defaultPackagePath = new File(DEFAULT_PACKAGE_PATH);
        Path parentDirectory = new File(DEFAULT_PACKAGE_PATH + APP_PACKAGE).toPath();
        utils = new ComponentPathUtilsImpl(defaultPackagePath.getPath(), parentDirectory);
    }

    @Test
    public void publicDirectory() {
        // given
        Path targetDirectory = utils.getTargetDirectory(COMPONENT_NAME);

        // when
        File publicDirectory = utils.getPublicDirectory(targetDirectory, COMPONENT_NAME);

        // then
        String expected = DEFAULT_PACKAGE_PATH + APP_PACKAGE + separator + EXPECTED_PACKAGE_NAME
                + separator + PUBLIC + separator + COMPONENT_NAME;
        then(publicDirectory.getPath()).isEqualTo(new File(expected).getPath());
    }

    @Test
    public void fullPackage() {
        // given
        Path targetDirectory = utils.getTargetDirectory(COMPONENT_NAME);

        // when
        String fullPackage = utils.getFullPackage(targetDirectory);

        // then
        then(fullPackage).isEqualTo(APP_PACKAGE_DOT + EXPECTED_PACKAGE_NAME);
    }

    @Test
    public void extractModuleName() {
        // when
        String moduleName = utils.extractModuleName(COMPONENT_NAME);

        // then
        then(moduleName).isEqualTo(EXPECTED_MODULE_NAME);
    }
}
