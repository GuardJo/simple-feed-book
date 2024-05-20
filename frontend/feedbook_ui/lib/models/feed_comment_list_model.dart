import 'package:feedbook_ui/models/comment_model.dart';

class FeedCommentList {
  final num totalPage;
  final num number;
  final List<Comment> comments;

  FeedCommentList({
    required this.totalPage,
    required this.number,
    required this.comments,
  });

  FeedCommentList.fromJson(Map<String, dynamic> json)
      : comments = [
          for (var comment in json["comments"]) Comment.fromJson(comment)
        ],
        totalPage = json["totalPage"],
        number = json["number"];
}
