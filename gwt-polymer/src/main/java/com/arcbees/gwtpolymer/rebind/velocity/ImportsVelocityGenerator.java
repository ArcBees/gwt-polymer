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

import java.util.HashSet;

import javax.inject.Inject;
import javax.inject.Provider;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.arcbees.gwtpolymer.base.Imports;
import com.google.gwt.core.ext.ConfigurationProperty;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;

public class ImportsVelocityGenerator extends AbstractVelocityGenerator {
    public static final String GWTPOLYMER_ELEMENTS = "gwtpolymer.elements";

    private static final String TEMPLATE = "com/arcbees/gwtpolymer/rebind/imports.vm";

    @Inject
    ImportsVelocityGenerator(
            TreeLogger logger,
            Provider<VelocityContext> velocityContextProvider,
            VelocityEngine velocityEngine,
            GeneratorUtil generatorUtil) {
        super(logger, velocityContextProvider, velocityEngine, generatorUtil);
    }

    public String generate() throws UnableToCompleteException {
        String implName = Imports.class.getSimpleName() + SUFFIX;

        mergeTemplate(TEMPLATE, implName);

        return Imports.class.getName() + SUFFIX;
    }

    @Override
    protected String getPackage() {
        return Imports.class.getPackage().getName();
    }

    @Override
    protected void populateVelocityContext(VelocityContext velocityContext) throws UnableToCompleteException {
        ConfigurationProperty property = getGeneratorUtil().getConfigurationProperty(GWTPOLYMER_ELEMENTS);

        velocityContext.put("elements", new HashSet<>(property.getValues()));
    }
}
