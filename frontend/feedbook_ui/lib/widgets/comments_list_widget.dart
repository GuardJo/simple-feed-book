import 'package:feedbook_ui/models/comment_model.dart';
import 'package:feedbook_ui/widgets/comment_card_widget.dart';
import 'package:feedbook_ui/widgets/write_comment_widget.dart';
import 'package:flutter/material.dart';

class CommentListWidget extends StatelessWidget {
  const CommentListWidget({super.key});

  List<Comment> getComments() {
    // TODO 추후 API 연동하기
    return [
      Comment(
          id: 1,
          author: "Tester1",
          createTime: "2024-05-27 18:28",
          content: "Hello~"),
      Comment(
          id: 2,
          author: "Tester2",
          createTime: "2024-05-27 16:33",
          content: "Hahahaha"),
      Comment(
          id: 3,
          author: "Tester2",
          createTime: "2024-05-26 16:00",
          content: "Test"),
      Comment(
          id: 1,
          author: "Tester1",
          createTime: "2024-05-27 18:28",
          content: "Hello~"),
      Comment(
          id: 2,
          author: "Tester2",
          createTime: "2024-05-27 16:33",
          content: "Hahahaha"),
      Comment(
          id: 3,
          author: "Tester2",
          createTime: "2024-05-26 16:00",
          content: "Test"),
    ];
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
                    child: ListView(
                      children: [
                        for (var comment in getComments())
                          CommentCard(comment: comment)
                      ],
                    ),
                  ),
                  const SizedBox(
                    width: 500,
                    height: 100,
                    child: WriteCommentWidget(),
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
