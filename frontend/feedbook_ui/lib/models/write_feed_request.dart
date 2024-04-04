class WriteFeedRequest {
  String title;
  String content;

  WriteFeedRequest({required this.title, required this.content});

  Map<String, dynamic> toJson() => {
        "title": title,
        "content": content,
      };
}
