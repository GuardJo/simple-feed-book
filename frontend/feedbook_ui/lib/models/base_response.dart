class BaseResponse {
  final String status;
  final dynamic body;

  BaseResponse({
    required this.status,
    required this.body
  });

  BaseResponse.fromJson(Map<String, dynamic> json) :
  status = json["status"],
  body = json["body"];

  BaseResponse.internalError() :
  status = "ERROR",
  body = "Internal Error";

  bool isOk() {
    if (status == "OK") {
      return true;
    }

    return false;
  }
}