import 'package:feedbook_ui/models/alarm_model.dart';

class AlarmList {
  num totalSize;
  num pageNumber;
  List<Alarm> feedAlarms;

  AlarmList({
    required this.totalSize,
    required this.pageNumber,
    required this.feedAlarms,
  });

  factory AlarmList.fromJson(Map<String, dynamic> json) {
    return AlarmList(
      totalSize: json["totalSize"],
      pageNumber: json["pageNumber"],
      feedAlarms: [for (var alarm in json["feedAlarms"]) Alarm.fromJson(alarm)],
    );
  }
}
