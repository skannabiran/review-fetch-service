package com.fab.digital;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class Test {
    public static void main(String[] args){
        Instant instant = Instant.ofEpochSecond(1423101467, 813000000);
        System.out.println("date::"+LocalDateTime.ofInstant(instant, ZoneOffset.UTC));
    }
}
