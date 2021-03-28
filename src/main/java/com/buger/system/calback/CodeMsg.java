package com.buger.system.calback;

public class CodeMsg {

    private int code;
    private String msg;

    //构造函数
    public CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    //通用的错误码,定义错误码后，这些东西我们写一遍就完事了，这就是优雅的代码
    public static CodeMsg SUCCESS = new CodeMsg(0,"success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500,"服务端异常");
    //登录错误码
    public static CodeMsg BIND_ERROR = new CodeMsg(500201,"参数绑定异常：&s");
    public static CodeMsg USER_HAS_SAVE = new CodeMsg(500201,"用户名已存在");
    public static CodeMsg TOKEN_UPDATE_ERROR = new CodeMsg(500201,"token处理异常");
    public static CodeMsg USER_PASSWORD_ERROR = new CodeMsg(500202,"用户名或密码输入错误！");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500202,"密码验证错误！");
    public static CodeMsg SESSION_ERROR = new CodeMsg(400,"登录失效！请重新登录");

    public static CodeMsg NO_POWER = new CodeMsg(500203,"权限不足或账号已被禁用，请登录有权限的的账号！");
    public static CodeMsg IMAGE_TYPE_ERROR = new CodeMsg(500203,"请上传图片！");
    public static CodeMsg IMAGE_SIZE_ERROR = new CodeMsg(500203,"文件过大！");
    public static CodeMsg SERVER_UPLOAD_ERROR = new CodeMsg(500203,"文件上传错误！");
    public static CodeMsg FILE_NULL = new CodeMsg(500203,"文件为空！");
    public static CodeMsg GOODS_IS_ONLINE = new CodeMsg(500203,"订单无法取消！");
    public static CodeMsg ORDER_IS_CLEAN = new CodeMsg(500203,"订单已经取消！");
    public static CodeMsg NO_ORDER = new CodeMsg(500203,"订单未找到！");
    //sql错误
    public static CodeMsg DATA_NULL = new CodeMsg(500204,"数据为空！");
    public static CodeMsg GOODS_NULL = new CodeMsg(500204,"商品不存在！");
    public static CodeMsg GOODS_IS_PUSH_MAX = new CodeMsg(500204,"商品库存不足！");
    public static CodeMsg GOODS_IS_INVALID = new CodeMsg(500204,"部分商品失效，请返回购物车重新下单！");
    public static CodeMsg ONE_GOODS_IS_PUSH_MAX(String goodsName){
        return new CodeMsg(500204,goodsName+" 库存不足！");
    }

    //websocket返回码
    public static CodeMsg CONNECTING = new CodeMsg(0,"全双工链接建立成功！");

    public String getMsg() {
        return msg;
    }
    public int getCode() {
        return code;
    }

    //填充参数。
    public CodeMsg fillArgs(Object... args) {
        int code = this.code;
        String message = String.format(this.msg, args);
        return new CodeMsg(code, message);
    }

}
