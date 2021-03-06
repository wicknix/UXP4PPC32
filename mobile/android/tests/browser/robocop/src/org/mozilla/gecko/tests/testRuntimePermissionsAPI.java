/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.gecko.tests;

import org.mozilla.gecko.EventDispatcher;
import org.mozilla.gecko.util.EventCallback;
import org.mozilla.gecko.util.NativeEventListener;
import org.mozilla.gecko.util.NativeJSObject;

import static org.mozilla.gecko.tests.helpers.AssertionHelper.fFail;

public class testRuntimePermissionsAPI extends JavascriptTest implements NativeEventListener {
    public testRuntimePermissionsAPI() {
        super("testRuntimePermissionsAPI.js");
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        EventDispatcher.getInstance().registerGeckoThreadListener(this, "RuntimePermissions:Prompt");
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();

        EventDispatcher.getInstance().unregisterGeckoThreadListener(this, "RuntimePermissions:Prompt");
    }

    @Override
    public void handleMessage(String event, NativeJSObject message, EventCallback callback) {
        mAsserter.is(event, "RuntimePermissions:Prompt", "Received RuntimePermissions:Prompt event");

        try {
            String[] permissions = message.getStringArray("permissions");
            mAsserter.is(3, permissions.length, "Received three permissions");

            mAsserter.is("android.permission.CAMERA", permissions[0], "Received CAMERA permission");
            mAsserter.is("android.permission.WRITE_EXTERNAL_STORAGE", permissions[1], "Received WRITE_EXTERNAL_STORAGE permission");
            mAsserter.is("android.permission.RECORD_AUDIO", permissions[2], "Received RECORD_AUDIO permission");
        } catch (Exception e) {
            fFail("Event does not contain expected data: " + e.getMessage());
        }
    }
}
