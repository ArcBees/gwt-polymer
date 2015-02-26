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

package com.arcbees.gwtpolymer.rebind;

import java.io.InputStream;
import java.util.Properties;

import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.velocity.app.VelocityEngine;

import com.arcbees.gwtpolymer.rebind.velocity.GeneratorUtil;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;

public class RebindModule extends AbstractModule {
    private static final String VELOCITY_PROPERTIES = "com/arcbees/gwtpolymer/rebind/velocity.properties";

    private final TreeLogger logger;
    private final GeneratorContext generatorContext;

    public RebindModule(TreeLogger logger, GeneratorContext generatorContext) {
        this.logger = logger;
        this.generatorContext = generatorContext;
    }

    @Override
    protected void configure() {
        bindConstant().annotatedWith(Names.named("VELOCITY_PROPERTIES")).to(VELOCITY_PROPERTIES);

        bind(GeneratorUtil.class).in(Singleton.class);
    }

    @Provides
    TreeLogger getLogger() {
        return logger;
    }

    @Provides
    TypeOracle getTypeOracle() {
        return generatorContext.getTypeOracle();
    }

    @Provides
    GeneratorContext getGeneratorContext() {
        return generatorContext;
    }

    @Provides
    @Singleton
    VelocityEngine getVelocityEngine(
            @Named("VELOCITY_PROPERTIES") String velocityProperties,
            TreeLogger logger)
            throws UnableToCompleteException {
        try {
            InputStream inputStream = null;
            Properties properties = new Properties();
            try {
                inputStream = this.getClass().getClassLoader().getResourceAsStream(velocityProperties);
                properties.load(inputStream);
                return new VelocityEngine(properties);
            } catch (Exception e) {
                logger.log(TreeLogger.Type.ERROR, "Cannot load velocity properties from " + velocityProperties);
                throw new UnableToCompleteException();
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        } catch (Exception e) {
            logger.log(TreeLogger.Type.ERROR, e.getMessage(), e);
        }

        return null;
    }
}
