package slayer.accessibility.service.flutter_accessibility_service;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ApplicationInfo;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AccessibilityListener extends AccessibilityService {
    public static boolean NODE_LOGGING = false;

    public static String ACCESSIBILITY_INTENT = "accessibility_event";
    public static String ACCESSIBILITY_NAME = "packageName";
    public static String ACCESSIBILITY_EVENT_TYPE = "eventType";
    public static String ACCESSIBILITY_TEXT = "capturedText";
    public static String ACCESSIBILITY_IS_SUPPORTED_BROWSER = "isSupportedBrowser";
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

    private static Map<String, String> browsers;

    private static String ignoredPackageName = "";

    private static PackageManager packageManager;

    private static Map<String, String> getSupportedBrowsers() {
        if (browsers == null) {
            browsers = new HashMap<>();
            browsers.put("org.chromium.webview_shell", "org.chromium.webview_shell:id/url_field");
            browsers.put("com.android.chrome", "com.android.chrome:id/url_bar");
            browsers.put("org.mozilla.firefox", "org.mozilla.firefox:id/mozac_browser_toolbar_url_view");
            browsers.put("com.opera.browser", "com.opera.browser:id/url_field");
            browsers.put("com.brave.browser", "com.brave.browser:id/url_bar");
            browsers.put("com.duckduckgo.mobile.android", "com.duckduckgo.mobile.android:id/omnibarTextInput");
            browsers.put("com.sec.android.app.sbrowser", "com.sec.android.app.sbrowser:id/location_bar_edit_text");
            browsers.put("com.microsoft.emmx", "com.microsoft.emmx:id/url_bar");
        }
        return browsers;
    }

    public static void setIgnoredPackageName(String packageName) {
        ignoredPackageName = packageName;
    }

    public static void setPackageManager(PackageManager pm) {
        packageManager = pm;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        AccessibilityNodeInfo parentNodeInfo = accessibilityEvent.getSource();
        if (parentNodeInfo == null) {
            return;
        }

        String packageName = parentNodeInfo.getPackageName().toString();
        if (packageName.equals(ignoredPackageName)) {
            return;
        }

        Map<String, String> supportedBrowsers = getSupportedBrowsers();
        final boolean isSupportedBrowser = supportedBrowsers.containsKey(packageName);
        final int eventType = accessibilityEvent.getEventType();
        String capturedUrl = "N/A";

        if (isSupportedBrowser) {
            capturedUrl = captureUrl(parentNodeInfo, supportedBrowsers.get(packageName));

            if (NODE_LOGGING) {
                Log.i("onAccessibilityEvent", "----------------vvvv----------------");
                Log.i("onAccessibilityEvent", "parentNodeInfo: " + parentNodeInfo.toString());
                Log.i("onAccessibilityEvent", "capturedUrl: " + capturedUrl);

                ArrayList<String> nodeTexts = getNodeTexts(parentNodeInfo);
                for (String x :
                        nodeTexts) {
                    Log.i("onAccessibilityEvent", "NodeText: " + x);
                }
                Log.i("onAccessibilityEvent", "----------------^^^^----------------");
            }

            if (capturedUrl == null) {
                return;
            }
        }

        Intent intent = new Intent(ACCESSIBILITY_INTENT);
        intent.putExtra(ACCESSIBILITY_NAME, packageName);
        intent.putExtra(ACCESSIBILITY_EVENT_TYPE, eventType);
        intent.putExtra(ACCESSIBILITY_TEXT, capturedUrl);
        intent.putExtra(ACCESSIBILITY_IS_SUPPORTED_BROWSER, isSupportedBrowser);
//        intent.putExtra(ACCESSIBILITY_NODES_TEXT, nodeTexts);
        Log.i("onAccssbltyEvnt.intent", "PackageName: " + packageName);
        Log.i("onAccssbltyEvnt.intent", "EventType: " + String.valueOf(eventType));
        Log.i("onAccssbltyEvnt.intent", "CapturedUrl: " + capturedUrl);
        Log.i("onAccssbltyEvnt.intent", "SupportedBrowser: " + String.valueOf(isSupportedBrowser));
        sendBroadcast(intent);
    }

    private ArrayList<String> getNodeTexts(AccessibilityNodeInfo parentNodeInfo) {
        ArrayList<String> nextTexts = new ArrayList<>();
        getNextTexts(parentNodeInfo, nextTexts);
        return nextTexts;
    }

    // @Override
    // public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
    //     final int eventType = accessibilityEvent.getEventType();
//         AccessibilityNodeInfo parentNodeInfo = accessibilityEvent.getSource();
    //     AccessibilityWindowInfo windowInfo = null;
//         List<String> nextTexts = new ArrayList<>();


    //     if (parentNodeInfo == null) {
    //         return;
    //     }

//         parentNodeInfo = parentNodeInfo.getParent();
//         if (parentNodeInfo == null) {
//             parentNodeInfo = accessibilityEvent.getSource();
//         }

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

    //Gets the text of this node.
    // String captureUrl = captureUrl(parentNodeInfo);
    // if (captureUrl != null) {
    //     intent.putExtra(ACCESSIBILITY_TEXT, captureUrl);
    // } else {
    //     if (parentNodeInfo.getText() != null) {
    //         intent.putExtra(ACCESSIBILITY_TEXT, parentNodeInfo.getText().toString());
    //     }
    // }
//         if (parentNodeInfo.getText() != null) {
//             intent.putExtra(ACCESSIBILITY_TEXT, parentNodeInfo.getText().toString());
//         }
//
//         getNextTexts(parentNodeInfo, nextTexts);

    //     //Gets the text of sub nodes.
//         intent.putStringArrayListExtra(ACCESSIBILITY_NODES_TEXT, (ArrayList<String>) nextTexts);


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


    void getNextTexts(AccessibilityNodeInfo node, List<String> arr) {
        Log.i("Node Text", "" + node.getText());
        if (node.getText() != null && node.getText().length() > 0) {
            arr.add(node.getText().toString());
            if (node.getViewIdResourceName() != null) {
                arr.add(node.getViewIdResourceName());
            }
        }
        Log.i("Node ChildCount", "" + node.getChildCount());
        for (int i = 0; i < node.getChildCount(); i++) {
            AccessibilityNodeInfo child = node.getChild(i);
            if (child == null)
                continue;
            getNextTexts(child, arr);
        }
    }

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
