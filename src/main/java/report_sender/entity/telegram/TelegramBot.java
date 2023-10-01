package report_sender.entity.telegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.HashSet;
import java.util.Set;


public class TelegramBot extends TelegramLongPollingBot {
    private Set<Long> chatIds = new HashSet<>();
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public TelegramBot(String botToken) {
        super(botToken);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            try {
                execute(SendMessage.builder()
                        .chatId(message.getChatId())
                        .text("Hello!!!")
                        .build());
                chatIds.add(message.getChatId());
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sendReport() throws TelegramApiException {
        for (Long id : chatIds) {
            execute(SendDocument.builder()
                    .chatId(id)
                    .document(new InputFile(new File(fileName)))
                    .build());
        }
    }

    @Override
    public String getBotUsername() {
        return "@WhiteTeamAndersenBot";
    }

    public Set<Long> getChatIds() {
        return chatIds;
    }

    public void setChatIds(Set<Long> chatIds) {
        this.chatIds = chatIds;
    }
}