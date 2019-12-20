package com.example.myapp.webDav;

public class Result {

    private String flag;

    public static String SUCCESS = "success";

    public static String FAILURE = "failure";

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Boolean isSuccess() {
        if (SUCCESS.equals(flag)) {
            return true;
        }
        return false;
    }

    public static Result setSuccess(String message){
        Result result = new Result();
        result.setFlag(SUCCESS);
        result.setMessage(message);
        return result;
    }

    public static Result setSuccess(){
        Result result = new Result();
        result.setFlag(SUCCESS);
        return result;
    }

    public static Result setFailure(String message){
        Result result = new Result();
        result.setFlag(FAILURE);
        result.setMessage(message);
        return result;
    }
}
