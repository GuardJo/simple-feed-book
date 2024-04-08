import 'dart:js';

import 'package:feedbook_ui/models/base_response.dart';
import 'package:feedbook_ui/models/feed_model.dart';
import 'package:feedbook_ui/services/feed_api_service.dart';
import 'package:feedbook_ui/widgets/modify_feed_widget.dart';
import 'package:flutter/material.dart';

class FeedCard extends StatelessWidget {
  final Feed feed;
  final String token;

  const FeedCard({super.key, required this.feed, required this.token});

  void _submmitDeleteRequest(BuildContext context) {
    _deleteFeed(context);

    Navigator.pop(context);
  }

  void _deleteFeed(BuildContext context) async {
    BaseResponse response = await FeedApiService.removeFeed(feed.id, token);
    Color bgColor = response.isOk() ? Colors.greenAccent : Colors.redAccent;

    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Text(response.body),
        backgroundColor: bgColor,
      ),
    );
  }

  void _showModifyingDialog(BuildContext context) {
    showDialog(
      context: context,
      builder: (cotext) {
        return Dialog(
          child: ModifyFeedWidget(
            feed: feed,
            token: token,
          ),
        );
      },
    );
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
                  feed.title,
                  style: const TextStyle(
                    fontSize: 32,
                    fontWeight: FontWeight.w400,
                  ),
                ),
                if (feed.isOwner)
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
              feed.author,
              style: const TextStyle(
                fontSize: 16,
              ),
            ),
            const SizedBox(
              height: 20,
            ),
            Text(
              feed.content,
              style: const TextStyle(
                fontSize: 22,
              ),
            ),
          ],
        ),
      ),
    );
  }
}
