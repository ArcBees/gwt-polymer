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

import java.io.File;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.arcbees.gwtpolymer.GwtModuleGenerator;
import com.arcbees.gwtpolymer.PolymerComponent;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ComponentModuleGeneratorTest {
    private static final String A_MODULE_NAME = "A_MODULE_NAME";
    private static final String GWT_XML = ".gwt.xml";

    @Test
    public void process_callsGeneratorAndEndsWithGwtDotXml() throws Exception {
        // given
        PolymerComponent polymerComponent = mock(PolymerComponent.class);
        given(polymerComponent.getModuleName()).willReturn(A_MODULE_NAME);
        given(polymerComponent.getComponentTargetDirectory()).willReturn(new File("/").toPath());

        GwtModuleGenerator moduleGenerator = mock(GwtModuleGenerator.class);
        ComponentModuleGenerator componentModuleGenerator = new ComponentModuleGenerator(moduleGenerator);

        // when
        componentModuleGenerator.process(polymerComponent);

        // then
        ArgumentCaptor<File> argumentCaptor = ArgumentCaptor.forClass(File.class);
        verify(moduleGenerator).generate(argumentCaptor.capture(), eq(polymerComponent));
        then(argumentCaptor.getValue().getPath()).endsWith(A_MODULE_NAME + GWT_XML);
    }
}
