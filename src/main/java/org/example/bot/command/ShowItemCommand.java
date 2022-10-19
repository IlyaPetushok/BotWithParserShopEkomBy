package org.example.bot.command;

import org.example.parser.entity.Item;
import org.example.parser.entity.ServerAttribute;
import org.example.parser.read.Parser;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShowItemCommand {
    public SendMessage sendMsg(Message message, String addAttribute) throws IOException {
        Parser parser = new Parser();
        ServerAttribute infoItem = null;
        infoItem = parser.getHrefItem(message.getReplyToMessage().getText(), message.getText().substring(42), addAttribute);

        List<Item> items = getItem(infoItem);
        if (items !=null) {
            Item item = items.get(0);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(message.getChatId());
            sendMessage.setReplyMarkup(InlineKeyboardMarkup
                    .builder()
                    .keyboard(createButton(0))
                    .build());
            sendMessage.setText(message.getReplyToMessage().getText() + "\n" + message.getText().substring(42) + "\n" + addAttribute + "\n" + item.getUrlPhoto() + "\n"
                    + item.getName() + "\n"
                    + item.getStatus() + "\n"
                    + item.getPrice());


            return sendMessage;
        }
        return null;
    }

    public List<Item> getItem(ServerAttribute itemInfo) throws IOException {
        Parser parser = new Parser();
        List<Item> items = parser.itemsShow(itemInfo.getHref(), "1");
        return items;
    }

    public List<List<InlineKeyboardButton>> createButton(Integer index) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> button = new ArrayList<>();
        button.add(InlineKeyboardButton
                .builder()
                .callbackData("BuyItem:" + index)
                .text("Buy")
                .build());
        button.add(InlineKeyboardButton
                .builder()
                .callbackData("NextItem:" + (index + 1) + ":0")
                .text("Следующий")
                .build());
        buttons.add(button);
        return buttons;
    }
}
