import 'dart:async';
import 'dart:developer';
import 'dart:io';

import 'package:flutter/services.dart';
import 'package:flutter_accessibility_service/accessibility_event.dart';

class FlutterAccessibilityService {
  FlutterAccessibilityService._();

  static const MethodChannel _methodeChannel =
      MethodChannel('x-slayer/accessibility_channel');
  static const EventChannel _eventChannel =
      EventChannel('x-slayer/accessibility_event');
  static Stream<AccessibilityEvent>? _stream;

  /// stream the incoming Accessibility events
  static Stream<AccessibilityEvent> get accessStream {
    if (Platform.isAndroid) {
      _stream ??=
          _eventChannel.receiveBroadcastStream().map<AccessibilityEvent>(
                (event) => AccessibilityEvent.fromMap(event),
              );
      return _stream!;
    }
    throw Exception("Accessibility API exclusively available on Android!");
  }

  static void requestNodeLogging() async {
    await _methodeChannel.invokeMethod('requestNodeLogging');
  }

  /// request accessibility permission
  /// it will open the accessibility settings page and return `true` once the permission granted.
  static Future<bool> requestAccessibilityPermission() async {
    try {
      return await _methodeChannel
          .invokeMethod('requestAccessibilityPermission');
    } on PlatformException catch (error) {
      log("$error");
      return Future.value(false);
    }
  }

  /// check if accessibility permession is enebaled
  static Future<bool> isAccessibilityPermissionEnabled() async {
    try {
      return await _methodeChannel
          .invokeMethod('isAccessibilityPermissionEnabled');
    } on PlatformException catch (error) {
      log("$error");
      return false;
    }
  }
}
