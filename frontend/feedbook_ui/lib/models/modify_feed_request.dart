class ModifyFeedRequest {
  final num feedId;
  final String title;
  final String content;

  ModifyFeedRequest({
    required this.feedId,
    required this.title,
    required this.content,
  });

  Map<String, dynamic> toJson() => {
        "feedId": feedId,
        "title": title,
        "content": content,
      };
}
