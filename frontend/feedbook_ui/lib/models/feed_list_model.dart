import 'package:feedbook_ui/models/feed_model.dart';

class FeedListResponse {
  final List<Feed> feeds;
  final num totalPage;

  FeedListResponse({required this.feeds, required this.totalPage});

  FeedListResponse.fromJson(Map<String, dynamic> json)
      : feeds = [for (var content in json["feeds"]) Feed.fromJson(content)],
        totalPage = json["totalPage"];
}
