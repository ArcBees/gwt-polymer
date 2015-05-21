/**
 * Copyright 2015 ArcBees Inc.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.arcbees.gwtpolymer.tasks;

import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;

import com.arcbees.gwtpolymer.PolymerComponent;

public interface TasksFactory {
    ComponentFilesCollector createFilesCollector(Map<String, PolymerComponent> componentNames);

    ComponentFilesCopier createFilesCopier();

    OrderedTasksProcessor<PolymerComponent> createOrderedTasksProcessor(
            List<PolymerComponent> components,
            Task<PolymerComponent>... tasks);

    SequentialTasksProcessor<PolymerComponent> createSequentialTasksProcessor(
            List<PolymerComponent> components,
            Task<PolymerComponent>... tasks);

    ComponentModuleGenerator createModuleGenerator();

    ComponentDependenciesCollector createDependenciesCollector(
            Map<String, PolymerComponent> componentNames);

    ComponentAstParser createAstParser(Document componentNames);
}
