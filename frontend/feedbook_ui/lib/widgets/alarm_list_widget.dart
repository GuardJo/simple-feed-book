import 'package:feedbook_ui/models/alarm_list_model.dart';
import 'package:feedbook_ui/models/alarm_model.dart';
import 'package:feedbook_ui/models/base_response.dart';
import 'package:feedbook_ui/services/alarm_api_service.dart';
import 'package:feedbook_ui/widgets/alarm_card_widget.dart';
import 'package:flutter/material.dart';

class AlarmListWidget extends StatelessWidget {
  final String token;
  const AlarmListWidget({super.key, required this.token});

  void _clearAlarams() {
    print("알림 제거");
  }

  Future<List<Alarm>> _getAlarms() async {
    BaseResponse response = await AlarmApiService.getFeedAlarms(token);

    if (response.isOk()) {
      AlarmList alarmList = AlarmList.fromJson(response.body);

      print(alarmList);

      return alarmList.feedAlarms;
    } else {
      return [];
    }
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
                child: FutureBuilder(
                  future: _getAlarms(),
                  builder: (context, snapshot) {
                    if (snapshot.hasData) {
                      return Column(
                        children: [
                          for (var alarm in snapshot.data!)
                            AlamrCard(
                              alarm: alarm,
                            ),
                          const SizedBox(
                            height: 10,
                          ),
                        ],
                      );
                    } else {
                      return const Center(
                        child: CircularProgressIndicator(),
                      );
                    }
                  },
                ),
              ),
            ],
          ),
        ],
      ),
    );
  }
}
