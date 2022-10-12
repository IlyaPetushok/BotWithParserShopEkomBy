package org.example.bot;

import org.example.parser.entity.ServerAttribute;
import org.example.parser.read.Parser;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
                choiceCommandForMessage(update.getMessage(), update);
            } catch (TelegramApiException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void choiceCommandForMessage(Message message, Update update) throws TelegramApiException, IOException {
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
                            .text("Выберите Команду")
                            .text("Выберите команду \n /choice_catalog")
                            .build());
                    break;
                case "/choice_catalog":
                    Parser parser = new Parser();
//                    String url=parser.showCategory();
                    List<ServerAttribute> catalogs = parser.showCategory();
                    for (int i = 0; i < catalogs.size(); i++) {
                        List<List<InlineKeyboardButton>> buttons = createButtons(catalogs.get(i));
                        execute(SendMessage
                                .builder()
                                .chatId(message.getChatId())
                                .text("<i>" + catalogs.get(i).getName() + "</i>")
                                .parseMode(ParseMode.HTML)
                                .replyMarkup(
                                        InlineKeyboardMarkup
                                                .builder()
                                                .keyboard(buttons)
                                                .build()

                                )
                                .build());
                    }
                    break;
            }
        }
    }

    public void choiceInputDownCatalog(CallbackQuery callbackQuery) throws TelegramApiException, InterruptedException, IOException {
        Parser parser = new Parser();
        Message message = callbackQuery.getMessage();
        String[] callback = callbackQuery.getData().split(":");
        String action = callback[0];
        String addAttribute = callback[1];
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        switch (action) {
            case "DownCatalog":
                List<ServerAttribute> items = parser.getInputDownCatalog(message.getText(), addAttribute);
                buttons=createButtons(items);
//                message.setText(items.get(0).getNameCatalog());
                execute(SendMessage
                        .builder()
                        .chatId(message.getChatId())
                        .replyToMessageId(message.getMessageId())
                        .text("Список устройст лежащих в данном каталоге: \n"+items.get(0).getNameCatalog())
                        .replyMarkup(
                                InlineKeyboardMarkup
                                        .builder()
                                        .keyboard(buttons)
                                        .build()
                        )
                        .build());
                break;
        }

//        execute(AnswerCallbackQuery
//                .builder()
//                .showAlert(true)
//                .text("hello")
//                .callbackQueryId(callbackQuery.getId())
//                .build());
    }

    public List<List<InlineKeyboardButton>> createButtons(ServerAttribute serverAttributes) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> button = new ArrayList<>();
        List<String> downCatalog = serverAttributes.getNameDownCatalog();
        for (int j = 0; j < downCatalog.size(); j++) {
            button.add(
                    InlineKeyboardButton
                            .builder()
                            .text(downCatalog.get(j))
                            .callbackData("DownCatalog:" + j)
                            .build()
            );
            if (j % 2 != 0) {
                buttons.add(button);
                button = new ArrayList<>();
            }
            if (downCatalog.size() % 2 != 0 && j == downCatalog.size() - 1) {
                buttons.add(button);
            }
        }
        return buttons;
    }

    public List<List<InlineKeyboardButton>> createButtons(List<ServerAttribute> itemsList) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> button = new ArrayList<>();

        for (int j = 0; j < itemsList.size(); j++) {
            button.add(
                    InlineKeyboardButton
                            .builder()
                            .text(itemsList.get(j).getName())
                            .callbackData("DownCatalog:" + j)
                            .build()
            );
            if (j % 2 != 0) {
                buttons.add(button);
                button = new ArrayList<>();
            }
            if (itemsList.size() % 2 != 0 && j == itemsList.size() - 1) {
                buttons.add(button);
            }
        }
        return buttons;
    }
}
