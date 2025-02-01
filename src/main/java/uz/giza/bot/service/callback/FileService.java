package uz.giza.bot.service.callback;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
@Slf4j
public class FileService {
    private volatile byte[] cachedFileData; // Кэш в памяти
    private static final String FILE_PATH = "classpath:static/docs/offer.pdf";


    @PostConstruct
    public void loadFileIntoMemory() {
        try {
            File file = ResourceUtils.getFile(FILE_PATH);
            cachedFileData = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            log.error("Error loading file: {}", e.getMessage());
            throw new RuntimeException("Failed to load file into memory", e);
        }
    }

    public byte[] getFileData() {
        return cachedFileData;
    }


}
//
//    @Cacheable(value = "forwardedMessages", key = "#chatId + '-' + #messageId")
//    public ForwardMessage forwardMessage(Long chatId, Integer messageId) {
//        return ForwardMessage.builder()
//                .chatId(chatId)
//                .fromChatId("-1002491249504")
//                .messageId(messageId)
//                .build();
//    }
//
//
//    @Cacheable("offer_file")
//    public File getFile() {
//        try {
//            return ResourceUtils.getFile("classpath:static/docs/offer.pdf");
//        } catch (Exception e) {
//            log.error("No such file: {}", e.getMessage());
//            throw new RuntimeException(e.getMessage());
//        }
//    }


