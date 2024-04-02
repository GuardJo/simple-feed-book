import 'dart:convert';

import 'package:feedbook_ui/models/base_response.dart';
import 'package:feedbook_ui/models/signup_request.dart';
import 'package:http/http.dart' as http;

class AccountApiService {
  static const String _baseUrl = "http://localhost:8080/api";
  static const _defaultHeaders = {
    "Content-Type": "application/json",
  };

  Future<BaseResponse> signup(SignupRequest request) async {
    var uri = Uri.parse("$_baseUrl/signup");
    var response = await http.post(uri,
        body: jsonEncode(request), headers: _defaultHeaders);

    if (response.statusCode == 200) {
      Map<String, dynamic> content = jsonDecode(response.body);
      return BaseResponse.fromJson(content);
    } else {
      return BaseResponse.internalError();
    }
  }
}
