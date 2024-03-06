import 'package:feedbook_ui/models/feed_model.dart';
import 'package:flutter/material.dart';

class FeedCard extends StatelessWidget {
  final Feed feed;

  const FeedCard({super.key, required this.feed});

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
                        onPressed: () {},
                        icon: const Icon(Icons.edit_document),
                      ),
                      IconButton(
                        onPressed: () {},
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
