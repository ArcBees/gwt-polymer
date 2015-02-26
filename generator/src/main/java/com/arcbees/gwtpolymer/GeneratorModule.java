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
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Properties;

import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.velocity.app.VelocityEngine;

import com.arcbees.gwtpolymer.annotations.DefaultPackagePath;
import com.arcbees.gwtpolymer.annotations.ElementsPackage;
import com.arcbees.gwtpolymer.annotations.PathPrefix;
import com.arcbees.gwtpolymer.fs.FileTraverser;
import com.arcbees.gwtpolymer.fs.PolymerComponentFileTraverser;
import com.arcbees.gwtpolymer.tasks.TasksFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;

public class GeneratorModule extends AbstractModule {
    private static final String VELOCITY_PROPERTIES = "com/arcbees/gwtpolymer/velocity.properties";

    private final String pathPrefix;

    public GeneratorModule(
            String pathPrefix) {
        this.pathPrefix = pathPrefix;
    }

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().build(TasksFactory.class));
        install(new FactoryModuleBuilder().build(PolymerComponentFactory.class));

        bind(FileTraverser.class).to(PolymerComponentFileTraverser.class).in(Singleton.class);
        bind(ComponentPathUtils.class).to(ComponentPathUtilsImpl.class).in(Singleton.class);

        File baseFolder = new File(elementsPackagePath());
        bind(Path.class).annotatedWith(ElementsPackage.class).toInstance(
                baseFolder.toPath().resolve("elements"));
        bind(String.class).annotatedWith(DefaultPackagePath.class).toInstance(defaultPackagePath());
        bind(String.class).annotatedWith(PathPrefix.class).toInstance(pathPrefix);

        bindConstant().annotatedWith(Names.named("VELOCITY_PROPERTIES")).to(VELOCITY_PROPERTIES);
    }

    @Provides
    @Singleton
    VelocityEngine getVelocityEngine(@Named("VELOCITY_PROPERTIES") String velocityProperties) throws IOException {
        InputStream inputStream = null;
        Properties properties = new Properties();
        try {
            inputStream = this.getClass().getClassLoader().getResourceAsStream(velocityProperties);
            properties.load(inputStream);
            return new VelocityEngine(properties);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    private String elementsPackagePath() {
        return defaultPackagePath() + "/com/arcbees/gwtpolymer/";
    }

    private String defaultPackagePath() {
        return new File(pathPrefix + "gwt-polymer/src/main/java/").getPath();
    }
}
