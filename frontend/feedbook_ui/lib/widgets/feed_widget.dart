import 'package:flutter/material.dart';

class WriteFeedWidget extends StatelessWidget {
  const WriteFeedWidget({
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return const Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Text(
            "Write Feed",
            style: TextStyle(
              color: Colors.black,
              fontWeight: FontWeight.bold,
              fontSize: 50,
            ),
          ),
          Text("Copyright. Guardjo"),
        ],
      ),
    );
  }
}
