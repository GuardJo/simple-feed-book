import 'package:feedbook_ui/models/alarm_model.dart';
import 'package:feedbook_ui/pages/main_screen.dart';
import 'package:flutter/material.dart';

class AlamrCard extends StatelessWidget {
  final Alarm alarm;

  const AlamrCard({super.key, required this.alarm});

  void _goToMyWidget(context) {
    // TODO MyFeed 페이지로 이동하도록 함
    Navigator.push(context, MaterialPageRoute(
      builder: (context) {
        return const AllFeedPage();
      },
    ));
  }

  @override
  Widget build(BuildContext context) {
    return InkWell(
      onTap: () {
        _goToMyWidget(context);
      },
      child: Container(
        width: 400,
        height: 80,
        margin: const EdgeInsets.all(2),
        decoration: BoxDecoration(
          border: Border.all(color: Colors.black26),
          borderRadius: BorderRadius.circular(5),
        ),
        alignment: Alignment.centerLeft,
        child: Padding(
            padding: const EdgeInsets.all(20),
            child: Row(
              children: [
                Text(alarm.alarmText),
                Text(
                  " ${alarm.alarmTime}",
                  style: const TextStyle(
                    color: Colors.black38,
                  ),
                )
              ],
            )),
      ),
    );
  }
}
