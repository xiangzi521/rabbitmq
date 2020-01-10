package com.mxz.rabbitMQ;

/**
 * Created by Administrator on 2020/1/10.
 */
public class client {
    public static void main(String[] args) {
        Sender sender = new Sender();
        sender.send();
    }
}
