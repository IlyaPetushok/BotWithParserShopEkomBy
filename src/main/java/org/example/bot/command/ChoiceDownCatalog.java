package org.example.bot.command;

import org.example.parser.entity.ServerAttribute;
import org.example.parser.read.Parser;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChoiceDownCatalog {
    public SendMessage sendMsg(Message message, String addAttribute) throws IOException {
        Parser parser = new Parser();
        List<List<InlineKeyboardButton>> buttons;
        List<ServerAttribute> typeItems = parser.getInputDownCatalog(message.getText(), addAttribute);
        buttons = createButtons(typeItems);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText("Список устройст лежащих в данном каталоге:" + typeItems.get(0).getNameCatalog());
        sendMessage.setReplyMarkup(InlineKeyboardMarkup
                .builder()
                .keyboard(buttons)
                .build());
        return sendMessage;
    }

    public List<List<InlineKeyboardButton>> createButtons(List<ServerAttribute> itemsList) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> button = new ArrayList<>();
        for (int j = 0; j < itemsList.size(); j++) {
            System.out.println("name  "+itemsList.get(j).getName());
            button.add(
                    InlineKeyboardButton
                            .builder()
                            .text(itemsList.get(j).getName())
                            .callbackData("ShowItem:" + itemsList.get(j).getName())
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


//      можно и так но в гралвном классе
//    execute(SendMessage
//                .builder()
//                .chatId(message.getChatId())
//                .replyToMessageId(message.getMessageId())
//                .text("Список устройст лежащих в данном каталоге:" + typeItems.get(0).getNameCatalog())
//                .replyMarkup(
//                        InlineKeyboardMarkup
//                                .builder()
//                                .keyboard(buttons)
//                                .build()
//                )
//                .build());