package entity;

//返回状态码
public class StatusCode {

    public static final int OK = 20000;//成功
    public static final int ERROR = 200001; //失败
    public static final int LOGINERROR = 200002; //用户名或密码错误
    public static final int ACCESSERROR = 200003;//权限不足
    public static final int REMOTEERROR = 200004;//远程调用失败
    public static final int REPERROR = 200005;//重复操作
}
