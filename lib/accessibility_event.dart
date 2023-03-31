import 'package:flutter_accessibility_service/utils.dart';

import 'constants.dart';

class AccessibilityEvent {
  /// the performed action that triggered this event
  /// https://developer.android.com/reference/android/view/accessibility/AccessibilityEvent#getAction()
  // int? actionType;

  /// the time in which this event was sent.
  /// https://developer.android.com/reference/android/view/accessibility/AccessibilityEvent#TYPE_WINDOW_CONTENT_CHANGED
  // DateTime? eventTime;

  /// the package name of the source
  /// https://developer.android.com/reference/android/view/accessibility/AccessibilityEvent#getPackageName()
  final String packageName;

  /// the event type.
  /// https://developer.android.com/reference/android/view/accessibility/AccessibilityEvent#getEventTime()
  final EventType? eventType;

  /// Gets the text of this node.
  /// https://developer.android.com/reference/android/view/accessibility/AccessibilityNodeInfo#getText()
  final String capturedUrl;

  final bool isSupportedBrowser;

  /// the bit mask of change types signaled by a `TYPE_WINDOW_CONTENT_CHANGED` event or `TYPE_WINDOW_STATE_CHANGED`. A single event may represent multiple change types
  /// https://developer.android.com/reference/android/view/accessibility/AccessibilityEvent#getContentChangeTypes()
  // ContentChangeTypes? contentChangeTypes;

  /// the movement granularity that was traversed
  /// https://developer.android.com/reference/android/view/accessibility/AccessibilityEvent#getMovementGranularity()
  // int? movementGranularity;

  /// the type of the window
  /// https://developer.android.com/reference/android/view/accessibility/AccessibilityWindowInfo#getType()
  // WindowType? windowType;

  /// check if this window is active. An active window is the one the user is currently touching or the window has input focus and the user is not touching any window.
  /// https://developer.android.com/reference/android/view/accessibility/AccessibilityWindowInfo#getType()
  // bool? isActive;

  /// check if this window has input focus.
  /// https://developer.android.com/reference/android/view/accessibility/AccessibilityWindowInfo#isFocused()
  // bool? isFocused;

  /// Check if the window is in picture-in-picture mode.
  /// https://developer.android.com/reference/android/view/accessibility/AccessibilityWindowInfo#isInPictureInPictureMode()
  // bool? isPip;

  /// Gets the node bounds in screen coordinates.
  /// https://developer.android.com/reference/android/view/accessibility/AccessibilityNodeInfo#getBoundsInScreen(android.graphics.Rect)
  // ScreenBounds? screenBounds;

  /// Get the node childrens and sub childrens text
  /// https://developer.android.com/reference/android/view/accessibility/AccessibilityNodeInfo#getChild(int)
  // List<String>? nodesText;

  AccessibilityEvent({
    // this.actionType,
    // this.eventTime,
    required this.packageName,
    required this.eventType,
    required this.capturedUrl,
    required this.isSupportedBrowser
    // this.contentChangeTypes,
    // this.movementGranularity,
    // this.windowType,
    // this.isActive,
    // this.isFocused,
    // this.isPip,
    // this.screenBounds,
    // this.nodesText,
  });

  AccessibilityEvent.fromMap(Map<dynamic, dynamic> map)
      : packageName = map['packageName'],
        capturedUrl = map['capturedUrl'],
        eventType =
          map['eventType'] == null ? null : Utils.eventType[map['eventType']],
        isSupportedBrowser = map['isSupportedBrowser'];
  // actionType = map['actionType'];
  // eventTime = DateTime.now();
  // packageName = ;
  // eventType =
  //     map['eventType'] == null ? null : Utils.eventType[map['eventType']];
  // capturedUrl = map['capturedUrl'] ?? '';
  // contentChangeTypes = map['contentChangeTypes'] == null
  //     ? null
  //     : (Utils.changeType[map['contentChangeTypes']] ??
  //         ContentChangeTypes.others);
  // movementGranularity = map['movementGranularity'];
  // windowType =
  //     map['windowType'] == null ? null : Utils.windowType[map['windowType']];
  // isActive = map['isActive'];
  // isFocused = map['isFocused'];
  // isPip = map['isPip'];
  // screenBounds = map['screenBounds'] != null
  //     ? ScreenBounds.fromMap(map['screenBounds'])
  //     : null;
  //       nodesText = map['nodesText'] == null
  //           ? []
  //           : [
  //         ...{...map['nodesText']}
  //       ];
  // }

  @override
  String toString() {
    return '''AccessibilityEvent: (
       Event Type: $eventType
       Package Name: $packageName 
       Captured Url: $capturedUrl
       Supported Browser: $isSupportedBrowser 
       )''';
  }

//   @override
//   String toString() {
//     return '''AccessibilityEvent: (
//        Package Name: $packageName
//        Event Type: $eventType
//        Captured Text: $capturedText
//        content Change Types: $contentChangeTypes
//        Movement Granularity: $movementGranularity
//        Is Active: $isActive
//        is focused: $isFocused
//        in Pip: $isPip
//        window Type: $windowType
//        Screen bounds: $screenBounds
//        Nodes Text: $nodesText
//        )''';
//   }
}

// class ScreenBounds {
//   int? right;
//   int? top;
//   int? left;
//   int? bottom;
//   int? width;
//   int? height;

//   ScreenBounds({
//     this.right,
//     this.top,
//     this.left,
//     this.bottom,
//     this.width,
//     this.height,
//   });

//   ScreenBounds.fromMap(Map<dynamic, dynamic> json) {
//     right = json['right'];
//     top = json['top'];
//     left = json['left'];
//     bottom = json['bottom'];
//     width = json['width'];
//     height = json['height'];
//   }

//   @override
//   String toString() {
//     return "left: $left - right: $right - top: $top - bottom: $bottom - width: $width - height: $height";
//   }
// }
