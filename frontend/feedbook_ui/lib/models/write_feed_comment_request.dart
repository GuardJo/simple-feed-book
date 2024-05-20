class FeedCommentCreateRequest {
  String content;

  FeedCommentCreateRequest({required this.content});

  Map<String, dynamic> toJson() => {
        "content": content,
      };
}
