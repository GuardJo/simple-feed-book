import 'package:feedbook_ui/models/base_response.dart';
import 'package:feedbook_ui/models/signup_request.dart';
import 'package:feedbook_ui/services/account_api_service.dart';
import 'package:flutter/material.dart';

class SignupPage extends StatefulWidget {
  const SignupPage({super.key});

  @override
  State<SignupPage> createState() => _SignupPageState();
}

class _SignupPageState extends State<SignupPage> {
  final String _appName = "Sign up";
  final AccountApiService _accountApiCaller = AccountApiService();
  final _formKey = GlobalKey<FormState>();

  String _username = "";
  String _password = "";
  String _nickname = "";

  void _registUser() async {
    final form = _formKey.currentState;

    if (form!.validate()) {
      form.save();
    }
    print(
        "nickName : $_nickname, userName : $_username, password : $_password");

    BaseResponse response = await _accountApiCaller.signup(SignupRequest(
      username: _username,
      nickname: _nickname,
      password: _password,
    ));

    ScaffoldMessenger.of(context).showSnackBar(SnackBar(
      content: Text(response.body),
      showCloseIcon: true,
    ));

    if (response.isOk()) {
      _showSignupSuccessDialog();
    }
  }

  void _showSignupSuccessDialog() {
    showDialog(
      context: context,
      builder: (context) {
        return AlertDialog(
          content: const Text("Signup Successes"),
          actions: [
            ElevatedButton(
              onPressed: _backLoginPage,
              child: const Text("Ok"),
            ),
          ],
        );
      },
    );
  }

  void _backLoginPage() {
    // dialog -> Signup Page -> Login Page
    Navigator.pop(context);
    Navigator.pop(context);
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
                        "Sign up",
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
                          labelText: "Nickname",
                        ),
                        validator: (value) {
                          if (value == null || value.isEmpty) {
                            return "Please enter Nickname";
                          }
                          return null;
                        },
                        onSaved: (newValue) => _nickname = newValue!,
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
                        onPressed: _registUser,
                        style: ButtonStyle(
                          backgroundColor: MaterialStateColor.resolveWith(
                              (states) => Colors.black),
                        ),
                        child: const Padding(
                          padding: EdgeInsets.all(10),
                          child: Text(
                            "Signup",
                            style: TextStyle(
                              fontWeight: FontWeight.bold,
                              fontSize: 20,
                              color: Colors.white,
                            ),
                          ),
                        ),
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
