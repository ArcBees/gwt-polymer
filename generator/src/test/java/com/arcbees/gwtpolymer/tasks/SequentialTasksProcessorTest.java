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

import java.util.ArrayList;

import org.junit.Test;
import org.mockito.InOrder;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

public class SequentialTasksProcessorTest {

    @Test
    public void run_tasksAreRunInOrder() throws Exception {
        // given
        Task task1 = mock(Task.class);
        Task task2 = mock(Task.class);
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(new Object());
        objects.add(new Object());
        SequentialTasksProcessor<Object> processor = new SequentialTasksProcessor<>(objects, task1, task2);

        // when
        processor.run();

        // then
        InOrder inOrder = inOrder(task1, task2);
        inOrder.verify(task1, times(2)).process(any());
        inOrder.verify(task2, times(2)).process(any());
    }
}
