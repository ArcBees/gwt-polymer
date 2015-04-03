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

package com.arcbees.gwtpolymer.base;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.ScriptInjector;

public class GwtPolymer {

	private static boolean isWebCompScriptInjected = false;

	public interface LoadCallback {
		void onInjectDone();
	}

	public static void init() {
		init(null);
	}

	public static void init(LoadCallback callbacks) {
		injectWebComponentsScript(callbacks);
	}

	private static void injectWebComponentsScript(final LoadCallback callback) {
		if (isWebCompScriptInjected) {

			doCallback(callback);

			return;

		}
		ScriptInjector
				.fromUrl(
						GWT.getModuleBaseURL()
								+ "webcomponentsjs/webcomponents.min.js")
				.setWindow(ScriptInjector.TOP_WINDOW)
				.setCallback(new Callback<Void, Exception>() {
					@Override
					public void onFailure(Exception reason) {
						GWT.log(reason.getMessage());
					}

					@Override
					public void onSuccess(Void result) {
						isWebCompScriptInjected = true;
						Imports imports = GWT.create(Imports.class);
						imports.injectImports();
						doCallback(callback);
					}
				}).inject();
	}

	private static void doCallback(final LoadCallback callback) {
		if (callback != null) {
			Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
				@Override
				public void execute() {
					callback.onInjectDone();
				}
			});
		}
	}
}
