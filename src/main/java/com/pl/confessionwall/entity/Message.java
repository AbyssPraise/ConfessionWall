package com.pl.confessionwall.entity;

import lombok.Data;

@Data
public class Message {
    Integer mid;
    Integer uid;
    String username;
    String whom;
    String what;
}
