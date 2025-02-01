package uz.giza.bot.payme;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.giza.bot.repository.TransactionRepository;
import uz.giza.bot.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymeService {

    private final TransactionRepository transactionRepository;

    @Value("${payme.merchant_id}")
    private String merchantId;

    public String getPaymentUrl(String orderId, double amount) {
        String url = "https://payme.uz/checkout";
        String formattedAmount = String.format("%.0f", amount * 100); //TODO: tiyin ko'rinishida

        return url + "/" + merchantId + "&ac.order_id=" + orderId + "&a=" + formattedAmount;
    }

    // Foydalanuvchining buyurtmasi mavjudligini tekshirish
    public ResponseEntity<?> checkPerformTransaction(Map<String, Object> request) {
        Map<String, Object> params = (Map<String, Object>) request.get("params");
        Long userId = Long.valueOf((String) ((Map<String, Object>) params.get("account")).get("user_id"));
        Integer amount = (Integer) params.get("amount");

        if (amount < 1000) {
            return ResponseEntity.badRequest().body(Map.of("error", "Minimal summa 1000 so‘m"));
        }

        // TODO: Baza yoki boshqa manbadan foydalanuvchi buyurtmasini tekshirish
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    // Yangi tranzaksiyani yaratish
    @org.springframework.transaction.annotation.Transactional
    public ResponseEntity<?> createTransaction(Map<String, Object> request) {
        Map<String, Object> params = (Map<String, Object>) request.get("params");
        String transactionId = (String) params.get("id");
        Long userId = Long.valueOf((String) ((Map<String, Object>) params.get("account")).get("user_id"));
        Integer amount = (Integer) params.get("amount");

        // Tranzaksiya allaqachon mavjudligini tekshirish
        Optional<Transaction> existingTransaction = transactionRepository.findByTransactionId(transactionId);
        if (existingTransaction.isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Transaction already exists"));
        }

        // Yangi tranzaksiya yaratish
        Transaction transaction = new Transaction();
        transaction.setTransactionId(transactionId);
        transaction.setUserId(userId);
        transaction.setAmount(amount);
        transaction.setState("created");
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());

        transactionRepository.save(transaction);

        return ResponseEntity.ok(Map.of("result", "transaction_created"));
    }

    // Tranzaksiyani yakunlash va statusni yangilash
    @org.springframework.transaction.annotation.Transactional
    public ResponseEntity<?> performTransaction(Map<String, Object> request) {
        Map<String, Object> params = (Map<String, Object>) request.get("params");
        String transactionId = (String) params.get("id");

        Optional<Transaction> transactionOpt = transactionRepository.findByTransactionId(transactionId);
        if (transactionOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Transaction not found"));
        }

        Transaction transaction = transactionOpt.get();
        if (!"created".equals(transaction.getState())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Transaction is already performed or canceled"));
        }

        // Tranzaksiyani bajarilgan deb belgilash
        transaction.setState("performed");
        transaction.setUpdatedAt(LocalDateTime.now());
        transactionRepository.save(transaction);

        return ResponseEntity.ok(Map.of("result", "transaction_performed"));
    }

    // Tranzaksiyani bekor qilish
    @Transactional
    public ResponseEntity<?> cancelTransaction(Map<String, Object> request) {
        Map<String, Object> params = (Map<String, Object>) request.get("params");
        String transactionId = (String) params.get("id");

        Optional<Transaction> transactionOpt = transactionRepository.findByTransactionId(transactionId);
        if (transactionOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Transaction not found"));
        }

        Transaction transaction = transactionOpt.get();
        if ("performed".equals(transaction.getState())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Transaction is already performed"));
        }

        // Tranzaksiyani bekor qilingan deb belgilash
        transaction.setState("canceled");
        transaction.setUpdatedAt(LocalDateTime.now());
        transactionRepository.save(transaction);

        return ResponseEntity.ok(Map.of("result", "transaction_canceled"));
    }
}
