import 'package:feedbook_ui/models/feed_model.dart';
import 'package:flutter/material.dart';

class ModifyFeedWidget extends StatefulWidget {
  final Feed feed;
  const ModifyFeedWidget({super.key, required this.feed});

  @override
  State<ModifyFeedWidget> createState() => _ModifyFeedWidgetState();
}

class _ModifyFeedWidgetState extends State<ModifyFeedWidget> {
  final _formKey = GlobalKey<FormState>();

  final String _userId = "testUserId";

  num _feedId = 0;

  String _feedTitle = "";

  String _feedContent = "";

  @override
  void initState() {
    super.initState();
    _feedId = widget.feed.id;
    _feedTitle = widget.feed.title;
    _feedContent = widget.feed.content;
  }

  void _submitFeed() {
    // TODO API 연동하기
    final formState = _formKey.currentState;

    if (formState!.validate()) {
      formState.save();

      print(
          "userId = $_userId, feedId = $_feedId, title = $_feedTitle, content = $_feedContent");

      Navigator.pop(context);
    }
  }

  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      child: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const SizedBox(
              height: 50,
            ),
            const Text(
              "Modify Feed",
              style: TextStyle(
                color: Colors.black,
                fontWeight: FontWeight.bold,
                fontSize: 35,
              ),
            ),
            Card(
              margin: const EdgeInsets.symmetric(
                vertical: 30,
                horizontal: 100,
              ),
              child: Form(
                key: _formKey,
                child: Column(
                  children: [
                    Padding(
                      padding: const EdgeInsets.symmetric(
                        vertical: 20,
                        horizontal: 15,
                      ),
                      child: TextFormField(
                        initialValue: _feedTitle,
                        decoration: const InputDecoration(
                          border: UnderlineInputBorder(),
                          labelText: "Enter Feed Title",
                        ),
                        validator: (value) {
                          if (value == null || value.isEmpty) {
                            return "Please Enter Title";
                          }
                          return null;
                        },
                        onSaved: (newValue) => _feedTitle = newValue!,
                      ),
                    ),
                    Padding(
                      padding: const EdgeInsetsDirectional.symmetric(
                        vertical: 20,
                        horizontal: 15,
                      ),
                      child: TextField(
                        controller:
                            TextEditingController(text: widget.feed.content),
                        decoration: const InputDecoration(
                          border: OutlineInputBorder(),
                        ),
                        maxLines: 10,
                        onChanged: (value) => _feedContent = value,
                      ),
                    ),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.end,
                      children: [
                        ElevatedButton(
                          onPressed: _submitFeed,
                          child: const Padding(
                            padding: EdgeInsets.all(10),
                            child: Text(
                              "Modified",
                              style: TextStyle(
                                fontWeight: FontWeight.bold,
                                fontSize: 20,
                                color: Colors.black,
                              ),
                            ),
                          ),
                        ),
                        const SizedBox(
                          width: 15,
                        ),
                      ],
                    ),
                    const SizedBox(
                      height: 20,
                    ),
                  ],
                ),
              ),
            )
          ],
        ),
      ),
    );
  }
}
