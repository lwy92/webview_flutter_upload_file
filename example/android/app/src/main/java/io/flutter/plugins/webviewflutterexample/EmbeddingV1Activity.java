// Copyright 2019 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package io.flutter.plugins.webviewflutterexample;

import android.os.Bundle;
import io.flutter.plugins.webviewflutter.WebViewFlutterPlugin;

@SuppressWarnings("deprecation")
public class EmbeddingV1Activity extends io.flutter.app.FlutterActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    WebViewFlutterPlugin.registerWith(
        registrarFor("io.flutter.plugins.webviewflutter.WebViewFlutterPlugin"));
  }
}
