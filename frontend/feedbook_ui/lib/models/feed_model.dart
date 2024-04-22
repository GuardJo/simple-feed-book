class Feed {
  final num id;
  final String title;
  final String content;
  final String author;
  final bool isOwner;
  bool isFavorite;

  Feed(
      {required this.id,
      required this.title,
      required this.content,
      required this.author,
      required this.isOwner,
      this.isFavorite = false});

  Map<String, dynamic> toJson() => {
        "id": id,
        "title": title,
        "content": content,
        "author": author,
        "isOwner": isOwner,
        "isFavorite": isFavorite,
      };

  factory Feed.fromJson(Map<String, dynamic> json) {
    return Feed(
      id: json["id"],
      title: json["title"],
      content: json["content"],
      author: json["author"],
      isOwner: json["isOwner"],
      isFavorite: json["isFavorite"] ?? false, // 초기값 구성
    );
  }
}
