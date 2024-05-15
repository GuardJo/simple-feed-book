import 'package:flutter/material.dart';

class WriteCommentWidget extends StatefulWidget {
  const WriteCommentWidget({super.key});

  @override
  State<WriteCommentWidget> createState() => _WriteCommentWidgetState();
}

class _WriteCommentWidgetState extends State<WriteCommentWidget> {
  final _formKey = GlobalKey<FormState>();

  String commentContent = "";

  void _saveComment() {
    // TODO 추후 API 연동
    if (commentContent.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text("댓글을 입력하시오."),
        ),
      );
    } else {
      print("Save New Commentm content = $commentContent");
      Navigator.pop(context);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 8),
      child: Form(
        key: _formKey,
        child: Flexible(
          child: Row(
            mainAxisAlignment: MainAxisAlignment.start,
            children: [
              SizedBox(
                width: 380,
                child: TextField(
                  decoration: const InputDecoration(
                    border: OutlineInputBorder(
                      borderRadius: BorderRadius.only(
                        topLeft: Radius.circular(15),
                        bottomLeft: Radius.circular(15),
                      ),
                    ),
                    hintText: "Enter Comment",
                  ),
                  maxLines: 10,
                  onChanged: (value) => commentContent = value,
                ),
              ),
              SizedBox(
                width: 104,
                height: 100,
                child: OutlinedButton(
                  style: OutlinedButton.styleFrom(
                    shape: const RoundedRectangleBorder(
                      borderRadius: BorderRadius.only(
                        topRight: Radius.circular(15),
                        bottomRight: Radius.circular(15),
                      ),
                    ),
                  ),
                  onPressed: _saveComment,
                  child: const Text(
                    "Save",
                    style: TextStyle(
                      fontWeight: FontWeight.w500,
                      fontSize: 20,
                      color: Colors.black,
                    ),
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
