import 'dart:convert';
import 'dart:io';

import 'package:feedbook_ui/models/base_response.dart';
import 'package:feedbook_ui/models/write_feed_comment_request.dart';
import 'package:http/http.dart' as http;

class FeedCommentService {
  static const String BASE_URL = "http://localhost:8080/api";
  static final Map<String, String> DEFEAULT_HEADERS = {
    HttpHeaders.contentTypeHeader: "application/json",
  };

  static Future<BaseResponse> getFeedComments(num feedId, String token) async {
    var uri = Uri.parse("$BASE_URL/feeds/$feedId/comments");

    var response = await http.get(
      uri,
      headers: {
        ...DEFEAULT_HEADERS,
        HttpHeaders.authorizationHeader: "Bearer $token",
      },
    );

    if (response.statusCode == 200) {
      Map<String, dynamic> json = jsonDecode(utf8.decode(response.bodyBytes));
      return BaseResponse.fromJson(json);
    } else {
      return BaseResponse.internalError();
    }
  }

  static Future<BaseResponse> createNewFeedComment(
      num feedId, FeedCommentCreateRequest request, String token) async {
    var uri = Uri.parse("$BASE_URL/feeds/$feedId/comments");

    var response = await http.post(
      uri,
      headers: {
        ...DEFEAULT_HEADERS,
        HttpHeaders.authorizationHeader: "Bearer $token",
      },
      body: jsonEncode(request),
    );

    if (response.statusCode == 200) {
      Map<String, dynamic> json = jsonDecode(utf8.decode(response.bodyBytes));
      return BaseResponse.fromJson(json);
    } else {
      return BaseResponse.internalError();
    }
  }
}
