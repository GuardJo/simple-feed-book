import 'package:feedbook_ui/models/base_response.dart';
import 'package:feedbook_ui/models/comment_model.dart';
import 'package:feedbook_ui/models/feed_comment_list_model.dart';
import 'package:feedbook_ui/services/feed_comment_api_service.dart';
import 'package:feedbook_ui/widgets/comment_card_widget.dart';
import 'package:feedbook_ui/widgets/write_comment_widget.dart';
import 'package:flutter/material.dart';

class CommentListWidget extends StatelessWidget {
  final num feedId;
  final String token;
  const CommentListWidget({
    super.key,
    required this.feedId,
    required this.token,
  });

  Future<List<Comment>> getComments() async {
    BaseResponse response =
        await FeedCommentService.getFeedComments(feedId, token);

    if (response.isOk()) {
      FeedCommentList feedCommentList = FeedCommentList.fromJson(response.body);
      return feedCommentList.comments;
    }

    return [];
  }

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Card(
            child: SizedBox(
              width: 500,
              height: 600,
              child: Column(
                children: [
                  const Text(
                    "Comments",
                    style: TextStyle(
                      fontSize: 42,
                      fontWeight: FontWeight.w600,
                    ),
                  ),
                  const SizedBox(
                    height: 30,
                  ),
                  SizedBox(
                      height: 400,
                      child: FutureBuilder(
                        future: getComments(),
                        builder: (context, snapshot) {
                          if (snapshot.hasData) {
                            return ListView(children: [
                              for (var comment in snapshot.data!)
                                CommentCard(
                                  comment: comment,
                                )
                            ]);
                          } else {
                            return const Center(
                              child: CircularProgressIndicator(),
                            );
                          }
                        },
                      )),
                  SizedBox(
                    width: 500,
                    height: 100,
                    child: WriteCommentWidget(
                      feedId: feedId,
                      token: token,
                    ),
                  ),
                ],
              ),
            ),
          )
        ],
      ),
    );
  }
}
