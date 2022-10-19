package org.example.bot;

import org.example.bot.command.ChoiceCatalog;
import org.example.bot.command.ChoiceDownCatalog;
import org.example.bot.command.NextItemCommand;
import org.example.bot.command.ShowItemCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return "EkomUzBot";
    }

    @Override
    public String getBotToken() {
        return "5655971090:AAExNGj3HBDLl9Kj8YeTyzEHI-mohLcOqto";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            try {
                choiceInputDownCatalog(update.getCallbackQuery());
            } catch (TelegramApiException | InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
        if (update.hasMessage()) {
            try {
                choiceCommandForMessage(update.getMessage());
            } catch (TelegramApiException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void choiceCommandForMessage(Message message) throws TelegramApiException, IOException {
        if (message.hasText()) {
            switch (message.getText()) {
                case "/start":
                    execute(SendMessage.builder()
                            .chatId(message.getChatId())
                            .text(" Добро пожаловать,в магазин EkomUz.by.\tEkomUzBot ваш" +
                                    " помощник для покупок профессионального сетевого оборудования. " +
                                    "Просто выберите товары с помощью бота или на нашем сайте  https://ekom.uz " +
                                    "или позвоните по телефону (+998)71 203-45-12," +
                                    " и мы доставим Вам товары лучшего качества в удобное для Вас время.Для просмотра вохможностей бота" +
                                    "  /help")
                            .build());
                    break;
                case "/help":
                    execute(SendMessage
                            .builder()
                            .chatId(message.getChatId())
                            .text("Выберите команду \n /choice_catalog")
                            .build());
                    break;
                case "/choice_catalog":
                    ChoiceCatalog choiceCatalog = new ChoiceCatalog();
                    List<SendMessage> messageList = choiceCatalog.sendMSG(message);
                    for (SendMessage sendMessage : messageList) {
                        execute(sendMessage);
                    }
                    break;
            }
        }
    }

    public void choiceInputDownCatalog(CallbackQuery callbackQuery) throws TelegramApiException, InterruptedException, IOException {
        Message message = callbackQuery.getMessage();
        String[] callback = callbackQuery.getData().split(":");
        String action = callback[0];
        String addAttribute = callback[1];
        String page = null;
        if (action.equals("NextItem")) {
            page = callback[2];
        }
        ShowItemCommand showItemCommand;
        switch (action) {
            case "ChoiceDownCatalog":
                ChoiceDownCatalog choiceDownCatalog = new ChoiceDownCatalog();
                execute(choiceDownCatalog.sendMsg(message, addAttribute));
                break;
            case "ShowItem":
                showItemCommand = new ShowItemCommand();
                if (showItemCommand.sendMsg(message, addAttribute) == null) {
                    execute(AnswerCallbackQuery
                            .builder()
                            .showAlert(true)
                            .text("В данном каталоге сейчас нету товаров")
                            .callbackQueryId(callbackQuery.getId())
                            .build());
                } else {
                    execute(showItemCommand.sendMsg(message, addAttribute));
                }
                break;
            case "NextItem":
                NextItemCommand nextItemCommand = new NextItemCommand();
                if (nextItemCommand.sendMsg(message, addAttribute, page) == null) {
                    execute(AnswerCallbackQuery
                            .builder()
                            .showAlert(true)
                            .text("Это был последний элемент")
                            .callbackQueryId(callbackQuery.getId())
                            .build());
                } else {
                    execute(nextItemCommand.sendMsg(message, addAttribute, page));
                }
                break;
        }
    }

}
