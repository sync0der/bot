package uz.giza.bot.payme;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/payme")
@RequiredArgsConstructor
@Slf4j
public class PaymeController {
    private final PaymeService paymeService;

    @PostMapping("/callback")
    public ResponseEntity<?> handlePaymeRequest(@RequestBody Map<String, Object> request) {
        if (!request.containsKey("method")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Missing method"));
        }

        String method = (String) request.get("method");

        return switch (method) {
            case "CheckPerformTransaction" -> paymeService.checkPerformTransaction(request);
            case "CreateTransaction" -> paymeService.createTransaction(request);
            case "PerformTransaction" -> paymeService.performTransaction(request);
            case "CancelTransaction" -> paymeService.cancelTransaction(request);
            default -> ResponseEntity.badRequest().body(Map.of("error", "Invalid method"));
        };
    }

}
