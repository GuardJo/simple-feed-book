class Alarm {
  final String alarmText;
  final String alarmTime;

  Alarm({
    required this.alarmText,
    required this.alarmTime,
  });

  Map<String, dynamic> toJson() => {
        "alarmText": alarmText,
        "alarmTime": alarmTime,
      };

  factory Alarm.fromJson(Map<String, dynamic> json) {
    return Alarm(
      alarmText: json["alarmText"],
      alarmTime: json["alarmTime"],
    );
  }
}
