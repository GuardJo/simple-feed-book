import 'package:feedbook_ui/models/alarm_list_model.dart';
import 'package:feedbook_ui/models/alarm_model.dart';
import 'package:feedbook_ui/models/base_response.dart';
import 'package:feedbook_ui/services/alarm_api_service.dart';
import 'package:feedbook_ui/widgets/alarm_card_widget.dart';
import 'package:flutter/material.dart';

class AlarmListWidget extends StatefulWidget {
  final String token;
  const AlarmListWidget({super.key, required this.token});

  @override
  State<AlarmListWidget> createState() => _AlarmListWidgetState();
}

class _AlarmListWidgetState extends State<AlarmListWidget> {
  Future<void> _clearAlarams(BuildContext context) async {
    BaseResponse response = await AlarmApiService.clearAlarms(widget.token);

    setState(() {
      Color bgColor = response.isOk() ? Colors.greenAccent : Colors.redAccent;

      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text(response.body),
          backgroundColor: bgColor,
        ),
      );
    });
  }

  void _showClearAlert(BuildContext context) {
    showDialog(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: const Text("알림 초기화"),
          content: const Text("알림 내역을 초기화 하시겠습니까?"),
          actions: [
            ElevatedButton(
              onPressed: () {
                _clearAlarams(context);
                Navigator.pop(context);
              },
              child: const Text("Yes"),
            ),
            ElevatedButton(
              onPressed: () {
                Navigator.pop(context);
              },
              child: const Text("No"),
            ),
          ],
        );
      },
    );
  }

  Future<List<Alarm>> _getAlarms() async {
    BaseResponse response = await AlarmApiService.getFeedAlarms(widget.token);

    if (response.isOk()) {
      AlarmList alarmList = AlarmList.fromJson(response.body);

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
                    onPressed: () => _showClearAlert(context),
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
                      if (snapshot.data!.isEmpty) {
                        return const Center(
                          child: Text("No Data"),
                        );
                      } else {
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
                      }
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
