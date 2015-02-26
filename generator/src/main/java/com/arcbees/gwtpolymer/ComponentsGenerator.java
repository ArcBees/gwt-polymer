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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.arcbees.gwtpolymer.fs.FileProvider;
import com.arcbees.gwtpolymer.fs.FileTraverser;
import com.arcbees.gwtpolymer.tasks.ComponentDependenciesCollector;
import com.arcbees.gwtpolymer.tasks.ComponentFilesCollector;
import com.arcbees.gwtpolymer.tasks.ComponentFilesCopier;
import com.arcbees.gwtpolymer.tasks.ComponentModuleGenerator;
import com.arcbees.gwtpolymer.tasks.OrderedTasksProcessor;
import com.arcbees.gwtpolymer.tasks.TasksFactory;

public class ComponentsGenerator {
    private final FileProvider fileProvider;
    private final FileTraverser fileTraverser;
    private final TasksFactory tasksFactory;

    @Inject
    ComponentsGenerator(
            FileProvider fileProvider,
            FileTraverser fileTraverser,
            TasksFactory tasksFactory) {
        this.fileProvider = fileProvider;
        this.fileTraverser = fileTraverser;
        this.tasksFactory = tasksFactory;
    }

    public void generate() throws IOException {
        File bowerDirectory = fileProvider.getFile("bower_components");
        List<PolymerComponent> components = fileTraverser.traverse(bowerDirectory);

        Map<String, PolymerComponent> componentNames = new HashMap<>();
        OrderedTasksProcessor<PolymerComponent> preparationSteps = createPreparationSteps(components, componentNames);

        ComponentDependenciesCollector dependenciesCollector = tasksFactory.createDependenciesCollector(componentNames);
        ComponentModuleGenerator moduleGenerator = tasksFactory.createModuleGenerator();

        tasksFactory
                .createSequentialTasksProcessor(components, preparationSteps, dependenciesCollector, moduleGenerator)
                .run();
    }

    private OrderedTasksProcessor<PolymerComponent> createPreparationSteps(List<PolymerComponent> components,
            Map<String, PolymerComponent> componentNames) {
        ComponentFilesCollector componentFilesCollector = tasksFactory.createFilesCollector(componentNames);
        ComponentFilesCopier componentFilesCopier = tasksFactory.createFilesCopier();

        return tasksFactory.createOrderedTasksProcessor(components, componentFilesCopier, componentFilesCollector);
    }
}
