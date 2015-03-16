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

package com.arcbees.gwtpolymer.tasks;

import java.util.List;

import javax.inject.Inject;

import com.google.inject.assistedinject.Assisted;

public class SequentialTasksProcessor<T> implements Runnable {
    private final List<T> elements;
    private final Task<T>[] tasks;

    @Inject
    SequentialTasksProcessor(
            @Assisted List<T> elements,
            @Assisted Task<T>... tasks) {
        this.elements = elements;
        this.tasks = tasks;
    }

    @Override
    public void run() {
        for (Task<T> task : tasks) {
            for (T element : elements) {
                try {
                    task.process(element);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
