package slayer.accessibility.service.flutter_accessibility_service;

import android.app.usage.UsageStatsManager;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.os.Build;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.RequiresApi;

public class AppUsageStatsHelper {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public static Map<String, Object> getAppForegroundStats(Context context, String packageName, long timeWindowInMins) {
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        Map<String, Object> results = new HashMap<>();

        long endTime = System.currentTimeMillis();
        long startTime = endTime - (long) (timeWindowInMins * 60 * 1000 * 1.1); // 10% extra time
        if (usageStatsManager == null) {
            results.put("foregroundRatio", (double) -1);
            results.put("totalTimeInForeground", -1);
            results.put("windowSizeInMins", timeWindowInMins);
            results.put("windowEndTime", endTime);
            results.put("windowStartTime", startTime);
            results.put("lastEventTime", -1);
            return results;
        }


        UsageEvents events = usageStatsManager.queryEvents(startTime, endTime);
        UsageEvents.Event event = new UsageEvents.Event();

        long totalTimeInForeground = 0;
        long lastMoveToForeground = -1;
        boolean isAppInForeground = false;
        long lastEventTime = -1;

        while (events.hasNextEvent()) {
            events.getNextEvent(event);

            if (packageName.equals(event.getPackageName())) {
                lastEventTime = event.getTimeStamp();
                if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                    lastMoveToForeground = event.getTimeStamp();
                    isAppInForeground = true;
                } else if (event.getEventType() == UsageEvents.Event.MOVE_TO_BACKGROUND) {
                    if (lastMoveToForeground != -1) {
                        // If we haven't seen MOVE_TO_FOREGROUND yet, assume implicit MOVE_TO_FOREGROUND at start
                        lastMoveToForeground = startTime;

                    }
                    totalTimeInForeground += event.getTimeStamp() - lastMoveToForeground;
                    isAppInForeground = false;
                }
            }
        }

        // If the app is not in the foreground at the end time, return false
        if (isAppInForeground) {
            totalTimeInForeground += endTime - lastMoveToForeground;
        }

        long foregroundRatio = totalTimeInForeground / (endTime - startTime);
        results.put("foregroundRatio", (double) foregroundRatio);
        results.put("totalTimeInForeground", totalTimeInForeground);
        results.put("windowSizeInMins", timeWindowInMins);
        results.put("windowEndTime", endTime);
        results.put("windowStartTime", startTime);
        results.put("lastEventTime", lastEventTime);
        return results;
    }
}