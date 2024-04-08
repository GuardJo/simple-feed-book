import 'package:feedbook_ui/models/base_response.dart';
import 'package:feedbook_ui/models/feed_model.dart';
import 'package:feedbook_ui/models/modify_feed_request.dart';
import 'package:feedbook_ui/pages/main_screen.dart';
import 'package:feedbook_ui/services/feed_api_service.dart';
import 'package:flutter/material.dart';

class ModifyFeedWidget extends StatefulWidget {
  final Feed feed;
  final String token;
  const ModifyFeedWidget({
    super.key,
    required this.feed,
    required this.token,
  });

  @override
  State<ModifyFeedWidget> createState() => _ModifyFeedWidgetState(token: token);
}

class _ModifyFeedWidgetState extends State<ModifyFeedWidget> {
  final _formKey = GlobalKey<FormState>();

  final String token;

  _ModifyFeedWidgetState({required this.token});

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
    final formState = _formKey.currentState;

    if (formState!.validate()) {
      formState.save();

      _modifyFeed();

      Navigator.pop(context);
    }
  }

  void _modifyFeed() async {
    BaseResponse response = await FeedApiService.modifyFeed(
      token,
      ModifyFeedRequest(
        feedId: _feedId,
        title: _feedTitle,
        content: _feedContent,
      ),
    );

    if (response.isOk()) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          backgroundColor: Colors.greenAccent,
          content: Text(response.body),
        ),
      );
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          backgroundColor: Colors.redAccent,
          content: Text("Failed : ${response.body}"),
        ),
      );
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
