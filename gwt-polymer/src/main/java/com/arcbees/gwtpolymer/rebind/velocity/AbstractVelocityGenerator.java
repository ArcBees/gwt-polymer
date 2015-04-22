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

import javax.inject.Provider;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;

public abstract class AbstractVelocityGenerator {
    protected static final String SUFFIX = "Impl";

    private final TreeLogger logger;
    private final Provider<VelocityContext> velocityContextProvider;
    private final VelocityEngine velocityEngine;
    private final GeneratorUtil generatorUtil;

    protected AbstractVelocityGenerator(
            TreeLogger logger,
            Provider<VelocityContext> velocityContextProvider,
            VelocityEngine velocityEngine,
            GeneratorUtil generatorUtil) {
        this.logger = logger;
        this.velocityContextProvider = velocityContextProvider;
        this.velocityEngine = velocityEngine;
        this.generatorUtil = generatorUtil;
    }

    protected GeneratorUtil getGeneratorUtil() {
        return generatorUtil;
    }

    protected boolean mergeTemplate(String velocityTemplate, String implName)
            throws UnableToCompleteException {
        PrintWriter printWriter = getGeneratorUtil().tryCreatePrintWriter(getPackage(), implName);
        if (printWriter != null) {
            try {
                VelocityContext velocityContext = velocityContextProvider.get();
                velocityContext.put("lf", "\n");
                velocityContext.put("implName", implName);
                velocityContext.put("package", getPackage());

                populateVelocityContext(velocityContext);

                velocityEngine.mergeTemplate(velocityTemplate, "UTF-8", velocityContext, printWriter);
                generatorUtil.closeDefinition(printWriter);
            } finally {
                printWriter.close();
            }

            return true;
        }

        logger.log(TreeLogger.Type.DEBUG, implName + "already generated. Returning.");

        return false;
    }

    protected abstract String getPackage();

    protected abstract void populateVelocityContext(VelocityContext velocityContext) throws UnableToCompleteException;
}
