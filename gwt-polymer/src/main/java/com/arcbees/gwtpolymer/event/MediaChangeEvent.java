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

package com.arcbees.gwtpolymer.event;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.shared.EventHandler;

public class MediaChangeEvent extends DomEvent<MediaChangeEvent.MediaChangeHandler> {
    public interface MediaChangeHandler extends EventHandler {
        void onMediaChange(MediaChangeEvent event);
    }

    private static final Type<MediaChangeHandler> TYPE =
            new Type<>(PolymerEvents.MEDIA_CHANGE, new MediaChangeEvent());

    public static Type<MediaChangeHandler> getType() {
        return TYPE;
    }

    public boolean matches() {
        return matches(getNativeEvent());
    }

    @Override
    public Type<MediaChangeHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(MediaChangeHandler handler) {
        handler.onMediaChange(this);
    }

    private native boolean matches(NativeEvent nativeEvent) /*-{
        return nativeEvent.detail.matches;
    }-*/;
}
