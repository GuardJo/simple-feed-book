import 'dart:convert';
import 'dart:io';

import 'package:feedbook_ui/models/base_response.dart';
import 'package:http/http.dart' as http;

class AlarmApiService {
  static const String baseUrl = "http://localhost:8080/api";
  static final Map<String, String> defaultHeaders = {
    HttpHeaders.contentTypeHeader: "application/json",
  };

  static Future<BaseResponse> getFeedAlarms(String token) async {
    var uri = Uri.parse("$baseUrl/accounts/alarms");

    var response = await http.get(
      uri,
      headers: {
        ...defaultHeaders,
        HttpHeaders.authorizationHeader: token,
      },
    );

    if (response.statusCode == 200) {
      Map<String, dynamic> content =
          jsonDecode(utf8.decode(response.bodyBytes));

      return BaseResponse.fromJson(content);
    } else {
      return BaseResponse.internalError();
    }
  }
}
