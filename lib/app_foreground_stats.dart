class AppForegroundStats {
  final double foregroundRatio;
  final int totalTimeInForeground;
  final int windowSizeInMins;
  final int windowStartTime;
  final int windowEndTime;
  final int lastEventTime;

  const AppForegroundStats({
    required this.totalTimeInForeground,
    required this.windowSizeInMins,
    required this.foregroundRatio,
    required this.windowStartTime,
    required this.windowEndTime,
    required this.lastEventTime,
  });

  static const nullish = AppForegroundStats(
        foregroundRatio: -1,
        totalTimeInForeground: -1,
        windowSizeInMins: -1,
        windowStartTime: -1,
        windowEndTime: -1,
        lastEventTime: -1,
      );

  factory AppForegroundStats.fromJson(Map<dynamic, dynamic> json) => AppForegroundStats(
        foregroundRatio: json['foregroundRatio'] as double,
        totalTimeInForeground: json['totalTimeInForeground'] as int,
        windowSizeInMins: json['windowSizeInMins'] as int,
        windowStartTime: json['windowStartTime'] as int,
        windowEndTime: json['windowEndTime'] as int,
        lastEventTime: json['lastEventTime'] as int,
      );

  bool get isNullish => foregroundRatio == -1;

  @override
  String toString() {
    return 'AppForegroundStats(foregroundRatio: $foregroundRatio, totalTimeInForeground: $totalTimeInForeground, windowSizeInMins: $windowSizeInMins, windowStartTime: $windowStartTime, windowEndTime: $windowEndTime, lastEventTime: $lastEventTime)';
  }
}
