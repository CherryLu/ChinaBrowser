/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.anyradio.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class HomeReceiver extends BroadcastReceiver {

	private static final String AnyRadioPackage = "com.joyradio.fm";
	private static final String LenovoAction = "lenovo.intent.action.TASK_REMOVED_FROM_RECENT";

	@Override
	public void onReceive(Context context, Intent intent) {

		if (LenovoAction.equals(intent.getAction())) {
			if (AnyRadioPackage.equals(intent.getStringExtra("package"))) {
				PlayManager.getInstance(context).stop();
			}
			return;
		}
	}
}
