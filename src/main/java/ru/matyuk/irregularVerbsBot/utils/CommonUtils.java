package ru.matyuk.irregularVerbsBot.utils;

public class CommonUtils {

    public static int getRandomNumber(int min, int max){
        return (int) ((Math.random() * (max - min)) + min);
    }
}
