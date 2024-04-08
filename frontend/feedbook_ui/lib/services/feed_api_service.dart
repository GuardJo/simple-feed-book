import 'dart:convert';
import 'dart:io';

import 'package:feedbook_ui/models/base_response.dart';
import 'package:feedbook_ui/models/modify_feed_request.dart';
import 'package:feedbook_ui/models/write_feed_request.dart';
import 'package:http/http.dart' as http;

class FeedApiService {
  static const String _baseUrl = "http://localhost:8080/api";
  static final Map<String, String> _defaultHeaders = {
    HttpHeaders.contentTypeHeader: "application/json",
  };

  Future<BaseResponse> writeFeed(WriteFeedRequest request, String token) async {
    var uri = Uri.parse("$_baseUrl/feeds");

    var response = await http.post(
      uri,
      headers: {
        ..._defaultHeaders,
        HttpHeaders.authorizationHeader: "Bearer $token",
      },
      body: jsonEncode(request),
    );

    if (response.statusCode == 200) {
      Map<String, dynamic> content = jsonDecode(response.body);
      return BaseResponse.fromJson(content);
    } else {
      return BaseResponse.internalError();
    }
  }

  static Future<BaseResponse> getFeeds(String token) async {
    var uri = Uri.parse("$_baseUrl/feeds");

    var response = await http.get(uri, headers: {
      ..._defaultHeaders,
      HttpHeaders.authorizationHeader: "Bearer $token"
    });

    if (response.statusCode == 200) {
      Map<String, dynamic> content = jsonDecode(response.body);
      return BaseResponse.fromJson(content);
    } else {
      return BaseResponse.internalError();
    }
  }

  static Future<BaseResponse> modifyFeed(
      String token, ModifyFeedRequest request) async {
    var uri = Uri.parse("$_baseUrl/feeds");
    var response = await http.patch(
      uri,
      headers: {
        ..._defaultHeaders,
        HttpHeaders.authorizationHeader: "Bearer $token",
      },
      body: jsonEncode(request),
    );

    if (response.statusCode == 200) {
      Map<String, dynamic> content = jsonDecode(response.body);
      return BaseResponse.fromJson(content);
    } else {
      return BaseResponse.internalError();
    }
  }

  static Future<BaseResponse> removeFeed(num feedId, String token) async {
    var uri = Uri.parse("$_baseUrl/feeds/$feedId");
    var response = await http.delete(
      uri,
      headers: {
        ..._defaultHeaders,
        HttpHeaders.authorizationHeader: "Bearer $token",
      },
    );

    if (response.statusCode == 200) {
      Map<String, dynamic> content = jsonDecode(response.body);
      return BaseResponse.fromJson(content);
    } else {
      return BaseResponse.internalError();
    }
  }
}
