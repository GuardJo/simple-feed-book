import 'package:feedbook_ui/models/base_response.dart';
import 'package:feedbook_ui/pages/main_screen.dart';
import 'package:feedbook_ui/pages/signup_screen.dart';
import 'package:feedbook_ui/services/account_api_service.dart';
import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

class LoginPage extends StatefulWidget {
  const LoginPage({super.key});

  @override
  State<LoginPage> createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  final String _appName = "Simple Feed Book";
  final _formKey = GlobalKey<FormState>();
  final AccountApiService _accountApiCaller = AccountApiService();

  late SharedPreferences preferences;

  String _username = "";
  String _password = "";

  @override
  void initState() {
    super.initState();
    _initPreferences();
  }

  Future<void> _initPreferences() async {
    preferences = await SharedPreferences.getInstance();
  }

  void _updateUserInfo() async {
    final form = _formKey.currentState;
    if (form!.validate()) {
      form.save();
    }

    BaseResponse response = await _accountApiCaller.login(_username, _password);

    if (response.isOk()) {
      String token = response.body;
      preferences.setString("token", token);
      _showSuccessDialog();
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text(
            response.body,
          ),
        ),
      );
    }
  }

  void _showSuccessDialog() {
    showDialog(
      context: context,
      builder: (context) {
        return AlertDialog(
          content: const Text("Login Successes"),
          actions: [
            ElevatedButton(
              onPressed: () => {
                Navigator.pushAndRemoveUntil(
                  context,
                  MaterialPageRoute(
                    builder: (context) {
                      return const AllFeedPage();
                    },
                  ),
                  (route) => false,
                ),
              },
              child: const Text("Ok"),
            ),
          ],
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(_appName),
        backgroundColor: Colors.white,
        shadowColor: const Color(0xFF000000),
        elevation: 2,
      ),
      body: SingleChildScrollView(
        child: Column(
          children: [
            const SizedBox(
              height: 100,
            ),
            Container(
              alignment: Alignment.center,
              child: SizedBox(
                width: 400,
                child: Form(
                  key: _formKey,
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      const Text(
                        "Login",
                        style: TextStyle(
                          fontWeight: FontWeight.bold,
                          fontSize: 45,
                        ),
                      ),
                      const SizedBox(
                        height: 30,
                      ),
                      TextFormField(
                        decoration: const InputDecoration(
                          border: UnderlineInputBorder(),
                          labelText: "Username",
                        ),
                        validator: (value) {
                          if (value == null || value.isEmpty) {
                            return "Please enter Username";
                          }
                          return null;
                        },
                        onSaved: (newValue) => _username = newValue!,
                      ),
                      const SizedBox(
                        height: 30,
                      ),
                      TextFormField(
                        obscureText: true,
                        decoration: const InputDecoration(
                          border: UnderlineInputBorder(),
                          labelText: "Password",
                        ),
                        validator: (value) {
                          if (value == null || value.isEmpty) {
                            return "Please enter Password";
                          }
                          return null;
                        },
                        onSaved: (newValue) => _password = newValue!,
                      ),
                      const SizedBox(
                        height: 30,
                      ),
                      ElevatedButton(
                        onPressed: _updateUserInfo,
                        style: ButtonStyle(
                          backgroundColor: MaterialStateColor.resolveWith(
                              (states) => Colors.black),
                        ),
                        child: const Padding(
                          padding: EdgeInsets.all(10),
                          child: Text(
                            "Login",
                            style: TextStyle(
                              fontWeight: FontWeight.bold,
                              fontSize: 20,
                              color: Colors.white,
                            ),
                          ),
                        ),
                      ),
                      const SizedBox(
                        height: 10,
                      ),
                      TextButton(
                        onPressed: () {
                          Navigator.push(
                            context,
                            MaterialPageRoute(
                              builder: (context) {
                                return const SignupPage();
                              },
                            ),
                          );
                        },
                        child: const Text("Sign up"),
                      ),
                    ],
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
