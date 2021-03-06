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

package com.arcbees.gwtpolymer.fs;

import java.io.File;

import org.assertj.core.util.Iterables;
import org.jukito.All;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.BDDAssertions.then;

import static com.arcbees.gwtpolymer.fs.CopyPolymerFilter.NAMES_TO_IGNORE;

@RunWith(JukitoRunner.class)
public class CopyPolymerFilterTest {
    public static class Module extends JukitoModule {
        @Override
        protected void configureTest() {
            bindManyNamedInstances(String.class, "NamesToIgnore", Iterables.toArray(NAMES_TO_IGNORE));
            bindManyNamedInstances(String.class, "Accepted", "src", "some-component.js", "some-component.min.js",
                    "another-component.js.map", "yet-another-component.html");
        }
    }

    @Test
    public void accept_excludedPattern_areExcluded(@All("NamesToIgnore") String pattern) throws Exception {
        // given
        CopyPolymerFilter filter = new CopyPolymerFilter();
        String filename = pattern.replace("*", "abcd");

        // when
        boolean accept = filter.accept(new File(filename));

        // then
        then(accept).describedAs("accept(%s) should return false", filename).isFalse();
    }

    @Test
    public void accept_src_included(@All("Accepted") String filename) throws Exception {
        // given
        CopyPolymerFilter filter = new CopyPolymerFilter();

        // when
        boolean accept = filter.accept(new File(filename));

        // then
        then(accept).describedAs("accept(%s) should return true", filename).isTrue();
    }
}
