package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.net.http.HttpResponse;


public class Bot implements LongPollingSingleThreadUpdateConsumer {
    private String tokenAPI = "7454140776:AAHRMZ7D2d9nSf9TzR9yZ99Xty16rB7Thvg";
    private TelegramClient telegramClient = new OkHttpTelegramClient(tokenAPI);

    public enum states {
        WAITING_CITY,
        WAITING_COMMAND,
        WAITING_PRINT
    }

    private states state = states.WAITING_COMMAND;


    @Override
    public void consume(Update update) {
        String message = (update.getMessage().getText());
        String idChat = String.valueOf(update.getMessage().getChatId());
        if(!message.equals("/start")) {
            if (state == states.WAITING_COMMAND) {
                if (!message.startsWith("/")) {
                    SendMessage sendMessage = new SendMessage(idChat, "Inserisci un comando valido!");
                    send(sendMessage);
                } else {
                    handleCommand(message, idChat);
                    state = states.WAITING_CITY;
                }
            } else {
                if (state == states.WAITING_CITY) {
                    String city = update.getMessage().getText();
                    double temp = fetchCurrentWeather(idChat, message, city);
                    if (temp != -0) {
                        SendMessage sendMessage = new SendMessage(idChat, "La temperatura è di " + temp + " gradi");
                        send(sendMessage);
                        state = states.WAITING_COMMAND;
                    } else {
                        SendMessage sendMessage = new SendMessage(idChat, "Inserisci una città valida!");
                        send(sendMessage);
                    }
                }
            }
        }
    }


    public double fetchCurrentWeather(String message, String idChat, String city) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://api.weatherapi.com/v1/current.json?key=fbe35283638d4440a06190648242605&q=" + city)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if(!response.isSuccessful()){
                throw new Exception();
            }else {
                JSONObject jsonObject = new JSONObject(response.body().string());
                JSONObject current = jsonObject.getJSONObject("current");
                int temp_c = current.getInt("temp_c");
                return temp_c;
            }
        }catch (Exception exception) {
            exception.printStackTrace();
            return -0;
        }
    }

    private void handleCommand(String message, String idChat) {
        switch (message) {
            case "/command1":
                SendMessage sendMessage = new SendMessage(idChat, "Inserisci città: ");
                send(sendMessage);
                break;
        }
    }

    private void send(SendMessage sendMessage){
        try{
            System.out.println(sendMessage.getChatId());
            telegramClient.execute(sendMessage);
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }
}
