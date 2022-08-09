package slayer.accessibility.service.flutter_accessibility_service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AccessibilityListener extends AccessibilityService {

    public static String ACCESSIBILITY_INTENT = "accessibility_event";
    public static String ACCESSIBILITY_NAME = "packageName";
//    public static String ACCESSIBILITY_EVENT_TYPE = "eventType";
    public static String ACCESSIBILITY_TEXT = "capturedText";
//    public static String ACCESSIBILITY_ACTION = "action";
//    public static String ACCESSIBILITY_EVENT_TIME = "eventTime";
//    public static String ACCESSIBILITY_CHANGES_TYPES = "contentChangeTypes";
//    public static String ACCESSIBILITY_MOVEMENT = "movementGranularity";
//    public static String ACCESSIBILITY_IS_ACTIVE = "isActive";
//    public static String ACCESSIBILITY_IS_FOCUSED = "isFocused";
//    public static String ACCESSIBILITY_IS_PIP = "isInPictureInPictureMode";
//    public static String ACCESSIBILITY_WINDOW_TYPE = "windowType";
//    public static String ACCESSIBILITY_SCREEN_BOUNDS = "screenBounds";
//    public static String ACCESSIBILITY_NODES_TEXT = "nodesText";

    private static Map<String, String> getSupportedBrowsers() {
        Map<String, String> browsers = new HashMap<>();
        browsers.put("com.android.chrome", "com.android.chrome:id/url_bar");
        return browsers;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        AccessibilityNodeInfo parentNodeInfo = accessibilityEvent.getSource();
        if (parentNodeInfo == null || parentNodeInfo.getChildCount() != 0) {
            return;
        }

        String packageName = parentNodeInfo.getPackageName().toString();
        Map<String, String> supportedBrowsers = getSupportedBrowsers();

        if(!supportedBrowsers.containsKey(packageName)) {
            return;
        }

        String capturedUrl = captureUrl(parentNodeInfo, supportedBrowsers.get(packageName));
        if (capturedUrl == null) {
            return;
        }

        Intent intent = new Intent(ACCESSIBILITY_INTENT);
        intent.putExtra(ACCESSIBILITY_NAME, packageName);
        intent.putExtra(ACCESSIBILITY_TEXT, capturedUrl);
        Log.d("unprn", "Accessibility event: " + packageName + ", " + capturedUrl + ", nodes: " + parentNodeInfo.getChildCount());

        sendBroadcast(intent);
    }

    // @Override
    // public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
    //     final int eventType = accessibilityEvent.getEventType();
    //     AccessibilityNodeInfo parentNodeInfo = accessibilityEvent.getSource();
    //     AccessibilityWindowInfo windowInfo = null;
    //     List<String> nextTexts = new ArrayList<>();


    //     if (parentNodeInfo == null) {
    //         return;
    //     }

    //     parentNodeInfo = parentNodeInfo.getParent();
    //     if (parentNodeInfo == null) {
    //         parentNodeInfo = accessibilityEvent.getSource();
    //     }

    //     String packageName = parentNodeInfo.getPackageName().toString();

    //     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
    //         windowInfo = parentNodeInfo.getWindow();
    //     }

    //     Intent intent = new Intent(ACCESSIBILITY_INTENT);
    //     //Gets the package name of the source
    //     intent.putExtra(ACCESSIBILITY_NAME, packageName);
    //     //Gets the event type
    //     intent.putExtra(ACCESSIBILITY_EVENT_TYPE, eventType);
    //     //Gets the performed action that triggered this event.
    //     intent.putExtra(ACCESSIBILITY_ACTION, accessibilityEvent.getAction());
    //     //Gets The event time.
    //     intent.putExtra(ACCESSIBILITY_EVENT_TIME, accessibilityEvent.getEventTime());
    //     //Gets the movement granularity that was traversed.
    //     intent.putExtra(ACCESSIBILITY_MOVEMENT, accessibilityEvent.getMovementGranularity());

    //     // Gets the node bounds in screen coordinates.
    //     Rect rect = new Rect();
    //     parentNodeInfo.getBoundsInScreen(rect);
    //     intent.putExtra(ACCESSIBILITY_SCREEN_BOUNDS, getBoundingPoints(rect));

    //     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
    //         //Gets the bit mask of change types signaled by a TYPE_WINDOW_CONTENT_CHANGED event or TYPE_WINDOW_STATE_CHANGED. A single event may represent multiple change types.
    //         intent.putExtra(ACCESSIBILITY_CHANGES_TYPES, accessibilityEvent.getContentChangeTypes());
    //     }

    //     //Gets the text of this node.
    //     // String captureUrl = captureUrl(parentNodeInfo);
    //     // if (captureUrl != null) {
    //     //     intent.putExtra(ACCESSIBILITY_TEXT, captureUrl);
    //     // } else {
    //     //     if (parentNodeInfo.getText() != null) {        
    //     //         intent.putExtra(ACCESSIBILITY_TEXT, parentNodeInfo.getText().toString());
    //     //     }
    //     // }
    //     if (parentNodeInfo.getText() != null) {        
    //         intent.putExtra(ACCESSIBILITY_TEXT, parentNodeInfo.getText().toString());
    //     }
        
    //     getNextTexts(parentNodeInfo, nextTexts);

    //     //Gets the text of sub nodes.
    //     intent.putStringArrayListExtra(ACCESSIBILITY_NODES_TEXT, (ArrayList<String>) nextTexts);
        

    //     if (windowInfo != null) {
    //         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
    //             // Gets if this window is active.
    //             intent.putExtra(ACCESSIBILITY_IS_ACTIVE, windowInfo.isActive());
    //             // Gets if this window has input focus.
    //             intent.putExtra(ACCESSIBILITY_IS_FOCUSED, windowInfo.isFocused());
    //             // Gets the type of the window.
    //             intent.putExtra(ACCESSIBILITY_WINDOW_TYPE, windowInfo.getType());
    //             // Check if the window is in picture-in-picture mode.
    //             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    //                 intent.putExtra(ACCESSIBILITY_IS_PIP, windowInfo.isInPictureInPictureMode());
    //             }

    //         }
    //     }
    //     sendBroadcast(intent);
    // }


//    void getNextTexts(AccessibilityNodeInfo node, List<String> arr) {
//        if (node.getText() != null && node.getText().length() > 0) {
//            arr.add(node.getText().toString());
//            if(node.getViewIdResourceName() != null) {
//                arr.add(node.getViewIdResourceName());
//            }
//        }
//        for (int i = 0; i < node.getChildCount(); i++) {
//            AccessibilityNodeInfo child = node.getChild(i);
//            if (child == null)
//                continue;
//            getNextTexts(child, arr);
//        }
//
//    }

    private String captureUrl(AccessibilityNodeInfo info, String urlViewId) {
        List<AccessibilityNodeInfo> nodes = info.findAccessibilityNodeInfosByViewId(urlViewId);
        if (nodes == null || nodes.size() <= 0) {
            return null;
        }

        AccessibilityNodeInfo addressBarNodeInfo = nodes.get(0);
        if (addressBarNodeInfo.getText() == null) {
           return null;
        }

        return addressBarNodeInfo.getText().toString();
    }

//    private HashMap<String, Integer> getBoundingPoints(Rect rect) {
//        HashMap<String, Integer> frame = new HashMap<>();
//        frame.put("left", rect.left);
//        frame.put("right", rect.right);
//        frame.put("top", rect.top);
//        frame.put("bottom", rect.bottom);
//        frame.put("width", rect.width());
//        frame.put("height", rect.height());
//        return frame;
//    }

    @Override
    public void onInterrupt() {

    }
}
