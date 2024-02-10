import 'package:flutter/material.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  final String _appName = "Simple Feed Book";
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: _appName,
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.white),
        useMaterial3: true,
      ),
      home: Scaffold(
        appBar: AppBar(
          title: Text(_appName),
          backgroundColor: Colors.white,
          shadowColor: Colors.black,
          elevation: 2,
        ),
      ),
    );
  }
}
