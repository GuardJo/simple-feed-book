import 'package:feedbook_ui/pages/login_screen.dart';
import 'package:feedbook_ui/widgets/feed_list_widget.dart';
import 'package:feedbook_ui/widgets/feed_widget.dart';
import 'package:feedbook_ui/widgets/home_widget.dart';
import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

class AllFeedPage extends StatefulWidget {
  const AllFeedPage({super.key});

  @override
  State<AllFeedPage> createState() => _AllFeedPageState();
}

class _AllFeedPageState extends State<AllFeedPage> {
  final String _appName = "Simple Feed Book";
  late SharedPreferences sharedPreferences;

  PageType pageType = PageType.allFeed;
  Widget pageWidget = const HomeWidget();

  @override
  void initState() {
    super.initState();
    _initPreferences();
  }

  Future<void> _initPreferences() async {
    sharedPreferences = await SharedPreferences.getInstance();
  }

  void _onTapUpdate(PageType pageType) {
    if (!sharedPreferences.containsKey("token")) {
      _onTapGoToLoginPage();
    } else {
      String token = sharedPreferences.getString("token")!;

      setState(() {
        pageType = pageType;

        switch (pageType) {
          case PageType.allFeed:
            pageWidget = AllFeedListWidget(token: token);
            break;
          case PageType.myFeed:
            pageWidget = MyFeedListWidget(token: token);
            break;
          case PageType.writeFeed:
            pageWidget = WriteFeedWidget(token: token);
            break;
          default:
            pageWidget = const HomeWidget();
        }
        Navigator.pop(context);
      });
    }
  }

  void _onTapGoToLoginPage() {
    Navigator.push(
      context,
      MaterialPageRoute(builder: (context) {
        return const LoginPage();
      }),
    );
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
              },
            ),
            ListTile(
              title: const Text(
                "My Feeds",
              ),
              onTap: () {
                _onTapUpdate(PageType.myFeed);
              },
            ),
            ListTile(
              title: const Text(
                "Write Feed",
              ),
              onTap: () {
                _onTapUpdate(PageType.writeFeed);
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
      body: pageWidget,
    );
  }
}

enum PageType {
  allFeed,
  myFeed,
  writeFeed,
}
