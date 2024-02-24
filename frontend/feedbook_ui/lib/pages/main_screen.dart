import 'package:feedbook_ui/pages/login_screen.dart';
import 'package:flutter/material.dart';

class AllFeedPage extends StatefulWidget {
  const AllFeedPage({super.key});

  @override
  State<AllFeedPage> createState() => _AllFeedPageState();
}

class _AllFeedPageState extends State<AllFeedPage> {
  final String _appName = "Simple Feed Book";

  PageType pageType = PageType.allFeed;

  void _onTapUpdate(PageType pageType) {
    setState(() {
      pageType = pageType;
    });
  }

  void _onTapGoToLoginPage() {
    Navigator.push(context, MaterialPageRoute(builder: (context) {
      return const LoginPage();
    }));
  }

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(_appName),
        backgroundColor: Colors.white,
        shadowColor: const Color(0xFF000000),
        elevation: 2,
      ),
      drawer: Drawer(
        child: ListView(
          children: [
            const DrawerHeader(
              child: Text("Menu"),
            ),
            ListTile(
              title: const Text(
                "All Feeds",
              ),
              onTap: () {
                _onTapUpdate(PageType.allFeed);
                Navigator.pop(context);
              },
            ),
            ListTile(
              title: const Text(
                "My Feeds",
              ),
              onTap: () {
                _onTapUpdate(PageType.allFeed);
                Navigator.pop(context);
              },
            ),
            ListTile(
              title: const Text(
                "Write Feed",
              ),
              onTap: () {
                _onTapUpdate(PageType.allFeed);
                Navigator.pop(context);
              },
            ),
            ListBody(
              children: [
                TextButton(
                  onPressed: _onTapGoToLoginPage,
                  child: const Text("Login"),
                ),
              ],
            )
          ],
        ),
      ),
      body: const Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(
              "Simple Feed Book",
              style: TextStyle(
                color: Colors.black,
                fontWeight: FontWeight.bold,
                fontSize: 50,
              ),
            ),
            Text("Copyright. Guardjo"),
          ],
        ),
      ),
    );
  }
}

enum PageType {
  allFeed,
  myFeed,
  writeFeed,
}
