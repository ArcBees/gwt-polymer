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

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

public class OrderedTasksProcessorTest {

    @Test
    public void process_inOrder() throws Exception {
        // given
        Task task1 = mock(Task.class);
        Task task2 = mock(Task.class);
        ArrayList<Object> objects = new ArrayList<>();
        Object object = new Object();
        OrderedTasksProcessor<Object> processor = new OrderedTasksProcessor<>(objects, task2, task1);

        // when
        processor.process(object);

        // then
        InOrder inOrder = inOrder(task1, task2);
        inOrder.verify(task2).process(eq(object));
        inOrder.verify(task1).process(eq(object));
    }

    @Test
    public void run_inOrder() throws Exception {
        // given
        Task task1 = mock(Task.class);
        Task task2 = mock(Task.class);

        ArrayList<Object> objects = new ArrayList<>();
        Object object1 = new Object();
        Object object2 = new Object();
        objects.add(object1);
        objects.add(object2);

        OrderedTasksProcessor<Object> processor = new OrderedTasksProcessor<>(objects, task1, task2);

        // when
        processor.run();

        // then
        InOrder inOrder = inOrder(task1, task2);
        inOrder.verify(task1).process(eq(object1));
        inOrder.verify(task2).process(eq(object1));
        inOrder.verify(task1).process(eq(object2));
        inOrder.verify(task2).process(eq(object2));
    }
}
