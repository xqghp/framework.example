package com.xyzla.netty.codec.msgpack;

import org.msgpack.annotation.Message;

import java.io.Serializable;

@Message
public class UserInfo implements Serializable {
    private int age;
    private String name;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
