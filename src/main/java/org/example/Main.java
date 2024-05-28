package org.example;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Main {
    public static void main(String[] args) {
        try{
            String tokenAPI = "7454140776:AAHRMZ7D2d9nSf9TzR9yZ99Xty16rB7Thvg";
            TelegramBotsLongPollingApplication weatherBot = new TelegramBotsLongPollingApplication();
            weatherBot.registerBot(tokenAPI, new Bot());
        }catch(TelegramApiException exception){
            System.err.println(exception.getMessage());;
        }
    }
}