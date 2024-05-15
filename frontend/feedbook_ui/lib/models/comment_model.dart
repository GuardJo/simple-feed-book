class Comment {
  final num id;
  final String author;
  final String createTime;
  final String content;

  Comment({
    required this.id,
    required this.author,
    required this.createTime,
    required this.content,
  });

  Map<String, dynamic> toJson() => {
        "id": id,
        "author": author,
        "createTime": createTime,
        "content": content,
      };

  factory Comment.fromJson(Map<String, dynamic> json) {
    return Comment(
        id: json["id"],
        author: json["author"],
        createTime: json["createTime"],
        content: json["content"]);
  }
}
