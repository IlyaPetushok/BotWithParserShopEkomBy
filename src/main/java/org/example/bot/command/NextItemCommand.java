package org.example.bot.command;

import org.example.parser.entity.Item;
import org.example.parser.entity.ServerAttribute;
import org.example.parser.read.Parser;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NextItemCommand {
    public SendMessage sendMsg(Message message, String addAttribute, String pageNumber) throws IOException {
        Parser parser = new Parser();
        Integer page = Integer.parseInt(pageNumber);
        String[] msg = message.getText().split("\n");
        ServerAttribute infoItem = parser.getHrefItem(msg[0], msg[1], msg[2]);
        if (Integer.parseInt(addAttribute) > 12) {
            page++;
            addAttribute = "0";
        }
        List<Item> items = getItem(infoItem, String.valueOf(page));
        if (!addAttribute.equals(String.valueOf(items.size()))) {
            Item item = items.get(Integer.parseInt(addAttribute));

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(message.getChatId());
            sendMessage.setReplyMarkup(InlineKeyboardMarkup
                    .builder()
                    .keyboard(createButton(Integer.parseInt(addAttribute), String.valueOf(page)))
                    .build());
            sendMessage.setText(msg[0] + "\n" + msg[1] + "\n" + msg[2] + "\n" +item.toString());
            return sendMessage;
        }
        return null;
    }

    public List<Item> getItem(ServerAttribute itemInfo, String page) throws IOException {
        Parser parser = new Parser();
        List<Item> items = parser.itemsShow(itemInfo.getHref(), page);
        return items;
    }

    public List<List<InlineKeyboardButton>> createButton(Integer index, String page) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> button = new ArrayList<>();
        button.add(InlineKeyboardButton
                .builder()
                .callbackData("BuyItem:" + index)
                .text("Buy")
                .build());
        button.add(InlineKeyboardButton
                .builder()
                .callbackData("NextItem:" + (index + 1) + ":" + page)
                .text("Следующий")
                .build());
        buttons.add(button);
        return buttons;
    }
}
