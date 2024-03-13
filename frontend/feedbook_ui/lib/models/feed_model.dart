class Feed {
  final num id;
  final String title;
  final String content;
  final String author;
  final bool isOwner;

  Feed(
      {required this.id,
      required this.title,
      required this.content,
      required this.author,
      required this.isOwner});
}
