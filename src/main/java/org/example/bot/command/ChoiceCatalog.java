package org.example.bot.command;

import org.example.parser.entity.ServerAttribute;
import org.example.parser.read.Parser;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ChoiceCatalog {

    public List<SendMessage> sendMSG(Message message) throws TelegramApiException, IOException {
        Parser parser = new Parser();
        List<SendMessage> messageList=new ArrayList<>();
        List<ServerAttribute> catalogs = parser.showCategory();
        SendMessage sendMessage=null;
        for (int i = 0; i < catalogs.size(); i++) {
            sendMessage=new SendMessage();
            List<List<InlineKeyboardButton>> buttons = createButtons(catalogs.get(i));
            sendMessage.setChatId(message.getChatId());
            sendMessage.setText("<i>" + catalogs.get(i).getName() + "</i>");
            sendMessage.setParseMode(ParseMode.HTML);
            sendMessage.setReplyMarkup(InlineKeyboardMarkup
                            .builder()
                            .keyboard(buttons)
                            .build());
//                    .builder().chatId(message.getChatId())
//                    .text("<i>" + catalogs.get(i).getName() + "</i>")
//                    .parseMode(ParseMode.HTML)
//                    .replyMarkup(
//                            InlineKeyboardMarkup
//                                    .builder()
//                                    .keyboard(buttons)
//                                    .build()
//
//                    )
//                    .build();
            messageList.add(sendMessage);
        }
        return messageList;
    }


    private List<List<InlineKeyboardButton>> createButtons(ServerAttribute serverAttributes) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> button = new ArrayList<>();
        List<String> downCatalog = serverAttributes.getNameDownCatalog();
        for (int j = 0; j < downCatalog.size(); j++) {
            button.add(
                    InlineKeyboardButton
                            .builder()
                            .text(downCatalog.get(j))
                            .callbackData("ChoiceDownCatalog:" + j)
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
}
