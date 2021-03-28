package com.buger.admin.user.po;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("t_admin")
public class AdminUser {
    private int id;
    private String username;
    private String password;
    private String token;
    private int power;
    private String time;
    private boolean enable;
    private String ip;


    public boolean isSuperAdmin(){
        if (this.power==0){
            return true;
        }
        return false;
    }
    public boolean isGoodsAdmin(){
        if (this.power<=1){
            return true;
        }
        return false;
    }
    public boolean isOrderAdmin(){
        if (this.power<=2){
            return true;
        }
        return false;
    }


    @Override
    public String toString() {
        return "AdminUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", token='" + token + '\'' +
                ", power=" + power +
                ", time='" + time + '\'' +
                ", enable=" + enable +
                ", ip='" + ip + '\'' +
                '}';
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
