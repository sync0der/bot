package uz.giza.bot.service.callback;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class FileService {
    private final Map<String, byte[]> cachedFiles = new ConcurrentHashMap<>();
    private static final String OFFER_FILE_PATH = "classpath:static/docs/ommaviy_oferta.docx";
    private static final String COURSE_1_PHOTO_PATH = "classpath:static/docs/course_1_image.jpg";

    public static final String OFFER_FILE_KEY = "offer";
    public static final String COURSE_1_PHOTO_KEY = "course1";

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @PostConstruct
    public void initialize() {
        // Preload files asynchronously
        preloadFiles();
    }

    private void preloadFiles() {
        executorService.submit(() -> cacheFiles(OFFER_FILE_KEY, OFFER_FILE_PATH));
        executorService.submit(() -> cacheFiles(COURSE_1_PHOTO_KEY, COURSE_1_PHOTO_PATH));
    }

    private void cacheFiles(String key, String path) {
        try {
            File file = ResourceUtils.getFile(path);
            byte[] fileData = Files.readAllBytes(file.toPath());
            cachedFiles.put(key, fileData);
            log.info("File cached successfully: {}", key);
        } catch (IOException e) {
            log.error("Error loading file: {}", e.getMessage());
            throw new RuntimeException("Failed to load file into memory", e);
        }
    }

    public byte[] getFileData(String key) {
        byte[] fileData = cachedFiles.get(key);
        if (fileData == null) {
            synchronized (this) {
                fileData = cachedFiles.get(key);
                if (fileData == null) {
                    // Load the file on-demand if not already cached
                    String path = getFilePathForKey(key);
                    if (path != null) {
                        cacheFiles(key, path);
                        fileData = cachedFiles.get(key);
                    }
                }
            }
        }
        return fileData;
    }

    private String getFilePathForKey(String key) {
        return switch (key) {
            case OFFER_FILE_KEY -> OFFER_FILE_PATH;
            case COURSE_1_PHOTO_KEY -> COURSE_1_PHOTO_PATH;
            default -> null;
        };
    }

    @PreDestroy
    public void cleanup() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }


}
//    @PostConstruct
//    public void loadFileIntoMemory() {
//        try {
//            File file = ResourceUtils.getFile(OFFER_FILE_PATH);
//            cachedFileData = Files.readAllBytes(file.toPath());
//        } catch (IOException e) {
//            log.error("Error loading file: {}", e.getMessage());
//            throw new RuntimeException("Failed to load file into memory", e);
//        }
//    }
//
//    public byte[] getFileData() {
//        return cachedFileData;
//    }


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


