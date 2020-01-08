package com.example.olyaave.app_randtopics;

import java.util.Random;

/*  Class for choose action */

public class RandGen {

    private int MIN = 1;
    private int MAX;

    public RandGen(long CountLine) {
        MAX = (int)CountLine;
    }

    public int RandLine(){
        Random rnd = new Random(System.currentTimeMillis());
        // Получаем случайное число в диапазоне от min до max (включительно)
        int number = MIN + rnd.nextInt(MAX - MIN + 1);
        return number;
    }
}
