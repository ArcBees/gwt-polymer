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

package com.arcbees.gwtpolymer.rebind.velocity;

import java.io.PrintWriter;

import javax.inject.Inject;

import com.google.gwt.core.ext.BadPropertyValueException;
import com.google.gwt.core.ext.ConfigurationProperty;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.PropertyOracle;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;

public class GeneratorUtil {
    private final TreeLogger logger;
    private final GeneratorContext generatorContext;

    @Inject
    GeneratorUtil(
            TreeLogger logger,
            GeneratorContext generatorContext) {
        this.logger = logger;
        this.generatorContext = generatorContext;
    }

    public void closeDefinition(PrintWriter printWriter) {
        generatorContext.commit(logger, printWriter);
    }

    public PrintWriter tryCreatePrintWriter(String packageName, String className)
            throws UnableToCompleteException {
        return generatorContext.tryCreate(logger, packageName, className);
    }

    public ConfigurationProperty getConfigurationProperty(String name) throws UnableToCompleteException {
        try {
            PropertyOracle propertyOracle = generatorContext.getPropertyOracle();

            ConfigurationProperty configurationProperty = propertyOracle.getConfigurationProperty(name);
            return configurationProperty;
        } catch (BadPropertyValueException e) {
            logger.log(TreeLogger.Type.ERROR, e.getMessage());
            throw new UnableToCompleteException();
        }
    }
}
