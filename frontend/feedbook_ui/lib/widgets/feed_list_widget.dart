import 'package:feedbook_ui/models/base_response.dart';
import 'package:feedbook_ui/models/feed_list_model.dart';
import 'package:feedbook_ui/models/feed_model.dart';
import 'package:feedbook_ui/services/feed_api_service.dart';
import 'package:feedbook_ui/widgets/feed_card_widget.dart';
import 'package:flutter/material.dart';

class AllFeedListWidget extends FeedListWidget {
  AllFeedListWidget({
    super.key,
    required super.token,
    super.isSelf = false,
  });
}

class MyFeedListWidget extends FeedListWidget {
  MyFeedListWidget({
    super.key,
    required super.token,
    super.isSelf = true,
  });
}

abstract class FeedListWidget extends StatefulWidget {
  String token;
  bool isSelf;
  FeedListWidget({
    super.key,
    required this.token,
    required this.isSelf,
  });

  @override
  State<FeedListWidget> createState() => _FeedListWidgetState();
}

class _FeedListWidgetState extends State<FeedListWidget> {
  Future<List<Feed>> _getFeeds() async {
    BaseResponse response = widget.isSelf
        ? await FeedApiService.getMyFeeds(widget.token)
        : await FeedApiService.getFeeds(widget.token);

    if (response.isOk()) {
      FeedListResponse feedListResponse =
          FeedListResponse.fromJson(response.body);

      return feedListResponse.feeds;
    }

    return [];
  }

  @override
  Widget build(BuildContext context) {
    return LayoutBuilder(
      builder: (context, constraints) {
        return SingleChildScrollView(
            child: FutureBuilder(
          future: _getFeeds(),
          builder: (context, snapshot) {
            if (snapshot.hasData) {
              return ConstrainedBox(
                constraints: BoxConstraints(
                  minHeight: constraints.minHeight,
                ),
                child: Column(
                  children: [
                    for (var feed in snapshot.data!)
                      FeedCard(
                        feed: feed,
                        token: widget.token,
                      ),
                  ],
                ),
              );
            } else {
              return const CircularProgressIndicator();
            }
          },
        ));
      },
    );
  }
}
