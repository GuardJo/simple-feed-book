import 'package:feedbook_ui/pages/main_screen.dart';
import 'package:flutter/material.dart';

class WriteFeedWidget extends StatefulWidget {
  const WriteFeedWidget({
    super.key,
  });

  @override
  State<WriteFeedWidget> createState() => _WriteFeedWidgetState();
}

class _WriteFeedWidgetState extends State<WriteFeedWidget> {
  final _formKey = GlobalKey<FormState>();

  final String _userId = "testUserId";
  String _feedTitle = "";
  String _feedContent = "";

  void _submitFeed() {
    // TODO API 연동하기
    final formState = _formKey.currentState;

    if (formState!.validate()) {
      formState.save();
    }

    print("userId = $_userId, title = $_feedTitle, content = $_feedContent");

    Navigator.pushAndRemoveUntil(context, MaterialPageRoute(builder: (context) {
      return const AllFeedPage();
    }), (route) => false);
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
              "Write Feed",
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
                        decoration: const InputDecoration(
                          border: OutlineInputBorder(),
                          hintText: "Enter Feed Content",
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
                              "Save",
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
