package com.example.miaosha.redis;

public interface KeyPrefix {

    int getExpireSeconds();

    String getPrefix();

}
