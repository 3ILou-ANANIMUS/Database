package org.ananimus.Redis;

public record Request(

        String key,
        String object,
        String value

){}
