import 'package:feedbook_ui/models/alarm_model.dart';
import 'package:feedbook_ui/widgets/alarm_card_widget.dart';
import 'package:flutter/material.dart';

class AlarmListWidget extends StatelessWidget {
  const AlarmListWidget({super.key});

  void _clearAlarams() {
    print("알림 제거");
  }

  List<Alarm> getAlarms() {
    // TODO API 연동 필요
    return [
      Alarm(alarmText: "test1님이 xxx 게시글에 신규 댓글을 등록하였습니다.", alarmTime: "1초"),
      Alarm(alarmText: "test2 님 외 222명이 xxx 게시글을 좋아합니다.", alarmTime: "1일"),
      Alarm(alarmText: "test3 님이 yyyy 게시글에 신규 댓글을 등록하였습니다.", alarmTime: "7일"),
    ];
  }

  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      child: Row(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                crossAxisAlignment: CrossAxisAlignment.end,
                children: [
                  const Text(
                    "알림 내역",
                    style: TextStyle(
                      fontWeight: FontWeight.w500,
                      fontSize: 30,
                    ),
                  ),
                  TextButton(
                    onPressed: _clearAlarams,
                    child: const Text("알림 초기화"),
                  ),
                ],
              ),
              Padding(
                padding: const EdgeInsets.symmetric(
                  vertical: 30,
                ),
                child: Column(
                  children: [
                    for (var alarm in getAlarms())
                      AlamrCard(
                        alarm: alarm,
                      ),
                    const SizedBox(
                      height: 10,
                    ),
                  ],
                ),
              ),
            ],
          ),
        ],
      ),
    );
  }
}
