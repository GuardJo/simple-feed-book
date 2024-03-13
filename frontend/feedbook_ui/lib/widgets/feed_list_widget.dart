import 'package:feedbook_ui/models/feed_model.dart';
import 'package:feedbook_ui/widgets/feed_card_widget.dart';
import 'package:flutter/material.dart';

class FeedListWidget extends StatefulWidget {
  const FeedListWidget({super.key});

  @override
  State<FeedListWidget> createState() => _FeedListWidgetState();
}

class _FeedListWidgetState extends State<FeedListWidget> {
  late final feedList;

  void _initFeedList() {
    // TODO 추후 데이터 주입하도록 변경

    feedList = [
      Feed(
          id: 1,
          title: "Title",
          content: "content",
          author: "tester",
          isOwner: true),
      Feed(
          id: 2,
          title: "Title",
          content: "content",
          author: "tester",
          isOwner: true),
      Feed(
          id: 3,
          title: "Title",
          content: "content",
          author: "tester",
          isOwner: true),
      Feed(
          id: 4,
          title: "Title",
          content: "content",
          author: "ddd",
          isOwner: false),
    ];
  }

  @override
  void initState() {
    super.initState();
    _initFeedList();
  }

  @override
  Widget build(BuildContext context) {
    return LayoutBuilder(
      builder: (context, constraints) {
        return SingleChildScrollView(
          child: ConstrainedBox(
            constraints: BoxConstraints(minHeight: constraints.maxHeight),
            child: Column(
              children: [for (var feed in feedList) FeedCard(feed: feed)],
            ),
          ),
        );
      },
    );
  }
}
