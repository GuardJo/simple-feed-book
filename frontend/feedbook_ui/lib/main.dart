import 'package:feedbook_ui/pages/main_screen.dart';
import 'package:flutter/material.dart';

void main() {
  runApp(const RootPage());
}

class RootPage extends StatelessWidget {
  final String _appName = "Simple Feed Book";
  const RootPage({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: _appName,
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.white),
        useMaterial3: true,
      ),
      locale: const Locale('ko', 'KR'),
      home: const AllFeedPage(),
    );
  }
}
