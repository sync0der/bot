package uz.giza.bot;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import uz.giza.bot.entity.Course;
import uz.giza.bot.repository.CourseRepository;
import uz.giza.bot.repository.UserRepository;

import java.util.List;

@EnableJpaAuditing
//@EnableAsync
@EnableCaching
@SpringBootApplication
@RequiredArgsConstructor
public class BotApplication {
    private final CourseRepository courseRepository;

    public static void main(String[] args) {
        SpringApplication.run(BotApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            if (courseRepository.count() == 0) {
                List<Course> courses = List.of(
                        new Course(1L, "«Biznes noldan»", """
                                Biznesni noldan boshlash – bu jasorat va sabr yo‘li. 🚀 Harakat qiling, o‘rganing, taslim bo‘lmang – muvaffaqiyat keladi! 💪🔥
                                
                                📝 Kursda sizni nimalar kutmoqda?
                                - Yulduz Mavlyanova bilan 3 ta dars\s
                                
                                Mavzular:
                                1⃣-Ong osti bilan ishlash va start\s
                                2⃣-Tizim va jamoa
                                3⃣-Marketing va Sotuv
                                
                                Kursdan so‘ng quyidagi natijalarga erishasiz:
                                
                                ✅ Pul va muvaffaqiyatga to‘g‘ri qarash shakllanadi – fikrlash tarziniz o‘zgaradi! 🧠⚡️
                                ✅ Biznesni noldan boshlash
                                ✅ Kuchli jamoa tuzish sirlarini bilish
                                ✅ Samarali marketing asoslari
                                ✅ O‘zingizga bo‘lgan ishonch oshirish
                                ✅ Yangi imkoniyatlar va moliyaviy o‘sish 🚀
                                ✅ Mukammal jamoani tuzishni o‘rganasiz
                                ✅ Ilk savdolaringizni tayyorlaysiz va sinovdan o‘tkazasiz
                                
                                Faqatgina bugun kurs narxi: <s>990.000 so'm</s>
                                 99.000 so'm
                                
                                👇🏻Xarid qilish uchun "Kursga yozilish" tugmasini bossing
                                
                                To'lovni amalga oshirgandan so'ng, siz barcha darslar joylangan kanalga havolani olasiz
                                
                                """, 99000L)
                );
                courseRepository.saveAll(courses);
                System.out.println("1 fake courses have been saved.");
            }
        };
    }

}
