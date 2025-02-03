package uz.giza.bot.service;

import uz.giza.bot.entity.User;
import uz.giza.bot.entity.UserPaymentStatus;
import uz.giza.bot.service.input.UserStates;

import java.util.List;

public interface UserService {
    boolean userExists(Long chatId);

    User get(Long chatId);

    void save(User user);


    List<User> getAll();

    List<User> getAllByUtm(String utm);

    List<User> getAllByTargetCourse(Long targetCourseId);

    void deleteById(Long userId);

    User updateUserState(Long chatId, UserStates userState);

    User updateUserState(User user, UserStates userState);

    void save(Long chatId, String username, String fullName, UserStates userState, String utmTag);

    void addPhotoUrl(Long chatId, String photoUrl);

    List<User> getAllPayedUsers(UserPaymentStatus userPaymentStatus);

    List<User> getAllWithUndefinedPhoneNumber();

//    void addCourse(User user, Course course);
}
