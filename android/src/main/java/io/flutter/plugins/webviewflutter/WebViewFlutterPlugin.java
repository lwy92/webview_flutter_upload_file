// Copyright 2018 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package io.flutter.plugins.webviewflutter;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.PluginRegistry;

/**
 * Java platform implementation of the webview_flutter plugin.
 *
 * <p>Register this in an add to app scenario to gracefully handle activity and context changes.
 *
 * <p>Call {@link #registerWith(Registrar)} to use the stable {@code io.flutter.plugin.common}
 * package instead.
 */
public class WebViewFlutterPlugin implements FlutterPlugin, ActivityAware, PluginRegistry.ActivityResultListener {

    private static WebViewFactory webViewFactory1;
    private FlutterCookieManager flutterCookieManager;

    /**
     * Add an instance of this to {@link io.flutter.embedding.engine.plugins.PluginRegistry} to
     * register it.
     *
     * <p>THIS PLUGIN CODE PATH DEPENDS ON A NEWER VERSION OF FLUTTER THAN THE ONE DEFINED IN THE
     * PUBSPEC.YAML. Text input will fail on some Android devices unless this is used with at least
     * flutter/flutter@1d4d63ace1f801a022ea9ec737bf8c15395588b9. Use the V1 embedding with {@link
     * #registerWith(Registrar)} to use this plugin with older Flutter versions.
     *
     * <p>Registration should eventually be handled automatically by v2 of the
     * GeneratedPluginRegistrant. https://github.com/flutter/flutter/issues/42694
     */
    public WebViewFlutterPlugin() {
    }

    WebViewFactory webViewFactory;

    /**
     * Registers a plugin implementation that uses the stable {@code io.flutter.plugin.common}
     * package.
     *
     * <p>Calling this automatically initializes the plugin. However plugins initialized this way
     * won't react to changes in activity or context, unlike {@link CameraPlugin}.
     */
    @SuppressWarnings("deprecation")
    public static void registerWith(io.flutter.plugin.common.PluginRegistry.Registrar registrar) {
        webViewFactory1 = new WebViewFactory(registrar.messenger(), registrar.view());
        registrar
                .platformViewRegistry()
                .registerViewFactory(
                        "plugins.flutter.io/webview",
                        webViewFactory1);
        webViewFactory1.setActivity(registrar.activity());
        final WebViewFlutterPlugin instance = new WebViewFlutterPlugin();
        registrar.addActivityResultListener(instance);
        new FlutterCookieManager(registrar.messenger());
    }

    @Override
    public void onAttachedToEngine(FlutterPluginBinding binding) {
        BinaryMessenger messenger = binding.getBinaryMessenger();
        webViewFactory = new WebViewFactory(messenger, /*containerView=*/ null);
        binding
                .getPlatformViewRegistry()
                .registerViewFactory(
                        "plugins.flutter.io/webview", webViewFactory);
        flutterCookieManager = new FlutterCookieManager(messenger);
    }

    @Override
    public void onDetachedFromEngine(FlutterPluginBinding binding) {
        if (flutterCookieManager == null) {
            return;
        }

        flutterCookieManager.dispose();
        flutterCookieManager = null;
    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        Activity activity = binding.getActivity();
        webViewFactory.setActivity(activity);
        binding.addActivityResultListener(this);

    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        System.out.println();
    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
        onAttachedToActivity(binding);
    }

    @Override
    public void onDetachedFromActivity() {
        System.out.println();
    }


    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (webViewFactory == null) {
            webViewFactory = webViewFactory1;
        }
        if (webViewFactory.getFlutterWebView() != null) {
            return webViewFactory.getFlutterWebView().resultHandler.handleResult(requestCode, resultCode, data);
        }
        return true;
    }
}
