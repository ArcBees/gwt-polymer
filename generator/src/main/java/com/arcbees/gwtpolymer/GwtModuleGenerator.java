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
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javax.inject.Inject;
import javax.inject.Provider;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class GwtModuleGenerator {
    private static final String TEMPLATE = "com/arcbees/gwtpolymer/gwt.xml.vm";

    private final Provider<VelocityContext> velocityContextProvider;
    private final VelocityEngine velocityEngine;

    @Inject
    GwtModuleGenerator(
            Provider<VelocityContext> velocityContextProvider,
            VelocityEngine velocityEngine) {
        this.velocityContextProvider = velocityContextProvider;
        this.velocityEngine = velocityEngine;
    }

    public void generate(
            File moduleOutputFile,
            PolymerComponent component) throws FileNotFoundException {
        PrintWriter printWriter = new PrintWriter(moduleOutputFile);
        try {
            VelocityContext velocityContext = velocityContextProvider.get();
            velocityContext.put("lf", "\n");
            velocityContext.put("component", component);

            velocityEngine.mergeTemplate(TEMPLATE, "UTF-8", velocityContext, printWriter);
        } finally {
            printWriter.close();
        }
    }
}
