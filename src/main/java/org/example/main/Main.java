package org.example.main;

import org.example.bot.Bot;
import org.example.parser.read.Parser;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, TelegramApiException {
        Bot testBot = new Bot();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(testBot);
//        Parser parser=new Parser();
//        String url=parser.showCategory();
//        parser.itemsShow(url);
    }
}
