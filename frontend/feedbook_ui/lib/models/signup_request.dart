class SignupRequest {
  final String username;
  final String nickname;
  final String password;

  SignupRequest({
    required this.username,
    required this.nickname,
    required this.password
  });

  Map<String, dynamic> toJson() => {
    "username": username,
    "nickname": nickname,
    "password": password,
  };
}