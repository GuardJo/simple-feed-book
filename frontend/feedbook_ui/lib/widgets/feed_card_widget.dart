import 'package:feedbook_ui/models/base_response.dart';
import 'package:feedbook_ui/models/feed_model.dart';
import 'package:feedbook_ui/services/feed_api_service.dart';
import 'package:feedbook_ui/widgets/comments_list_widget.dart';
import 'package:feedbook_ui/widgets/modify_feed_widget.dart';
import 'package:flutter/material.dart';

class FeedCard extends StatefulWidget {
  final Feed feed;
  final String token;

  const FeedCard({super.key, required this.feed, required this.token});

  @override
  State<FeedCard> createState() => _FeedCardState();
}

class _FeedCardState extends State<FeedCard> {
  void _submmitDeleteRequest(BuildContext context) {
    _deleteFeed(context);

    Navigator.pop(context);
  }

  void _deleteFeed(BuildContext context) async {
    BaseResponse response =
        await FeedApiService.removeFeed(widget.feed.id, widget.token);
    Color bgColor = response.isOk() ? Colors.greenAccent : Colors.redAccent;

    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Text(response.body),
        backgroundColor: bgColor,
      ),
    );
  }

  void _showModifyingDialog(BuildContext context) {
    setState(() {
      showDialog(
        context: context,
        builder: (cotext) {
          return Dialog(
            child: ModifyFeedWidget(
              feed: widget.feed,
              token: widget.token,
            ),
          );
        },
      );
    });
  }

  void _showCommentsListDialog(BuildContext context) {
    setState(() {
      showDialog(
        context: context,
        builder: (context) {
          return const CommentListWidget();
        },
      );
    });
  }

  void _showDeleteDialog(BuildContext context) {
    showDialog(
        context: context,
        builder: (context) {
          return AlertDialog(
            title: const Text("Delete Feed"),
            content: const Text("Deleting This Feed?"),
            icon: const Icon(Icons.warning),
            actions: [
              ElevatedButton(
                onPressed: () {
                  _submmitDeleteRequest(context);
                },
                child: const Text("Yes"),
              ),
              ElevatedButton(
                onPressed: () {
                  Navigator.pop(context);
                },
                child: const Text("No"),
              ),
            ],
          );
        });
  }

  void _updateFavorite() async {
    await FeedApiService.updateFavorite(widget.feed.id, widget.token);

    setState(() {
      widget.feed.isFavorite = !widget.feed.isFavorite;
      widget.feed.totalFavorites += widget.feed.isFavorite ? 1 : -1;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(
        vertical: 30,
        horizontal: 100,
      ),
      child: Container(
        padding: const EdgeInsets.all(20),
        decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(20),
          border: Border.all(color: Colors.black26),
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text(
                  widget.feed.title,
                  style: const TextStyle(
                    fontSize: 32,
                    fontWeight: FontWeight.w400,
                  ),
                ),
                if (widget.feed.isOwner)
                  Row(
                    children: [
                      IconButton(
                        onPressed: () {
                          _showModifyingDialog(context);
                        },
                        icon: const Icon(Icons.edit_document),
                      ),
                      IconButton(
                        onPressed: () {
                          _showDeleteDialog(context);
                        },
                        icon: const Icon(
                          Icons.delete_forever,
                        ),
                      ),
                    ],
                  )
              ],
            ),
            const SizedBox(
              height: 10,
            ),
            Text(
              widget.feed.author,
              style: const TextStyle(
                fontSize: 16,
              ),
            ),
            const SizedBox(
              height: 20,
            ),
            Text(
              widget.feed.content,
              style: const TextStyle(
                fontSize: 22,
              ),
            ),
            const SizedBox(
              height: 40,
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Row(
                  children: [
                    IconButton(
                      onPressed: _updateFavorite,
                      icon: Icon(
                        widget.feed.isFavorite
                            ? Icons.favorite
                            : Icons.favorite_border,
                      ),
                    ),
                    Text("+ ${widget.feed.totalFavorites}"),
                  ],
                ),
                TextButton(
                  onPressed: () {
                    _showCommentsListDialog(context);
                  },
                  child: const Text("Comments..."),
                ),
              ],
            )
          ],
        ),
      ),
    );
  }
}
