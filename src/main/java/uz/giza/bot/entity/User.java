package uz.giza.bot.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uz.giza.bot.service.input.UserStates;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "username")
    private String userName;

    @Column(name = "chat_id", unique = true)
    private Long chatId;

    @Column(name = "utm_tag")
    private String utmTag;

    @Column(name = "user_state")
    @Enumerated(EnumType.STRING)
    private UserStates userState;

    @CreatedDate
    @Column(name = "registered_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP(0)")
    private LocalDateTime registeredAt;

    @ManyToOne(fetch = FetchType.EAGER)
    private Course targetCourse;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "paid_courses",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> paidCourses;

}
