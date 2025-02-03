package uz.giza.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import uz.giza.bot.entity.User;
import uz.giza.bot.entity.UserPaymentStatus;
import uz.giza.bot.repository.UserRepository;
import uz.giza.bot.service.input.InputContainer;
import uz.giza.bot.service.input.UserStates;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public boolean userExists(Long chatId) {
        return userRepository.existsUserByChatId(chatId);
    }

    @Override
    public User get(Long chatId) {
        return userRepository.findByChatId(chatId);
    }

    @Override
    @Async
    public void save(User user) {
        userRepository.save(user);
    }


    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getAllByUtm(String utm) {
        return userRepository.findAllByUtmTag(utm);
    }

    @Override
    public List<User> getAllByTargetCourse(Long targetCourseId) {
        return userRepository.findAllByTargetCourse_Id(targetCourseId);
    }

    @Override
    @Async
    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public User updateUserState(Long chatId, UserStates userState) {
        User user = get(chatId);
        return updateUserState(user, userState);
    }

    @Override
    public User updateUserState(User user, UserStates userState) {
        user.setUserState(userState);
        save(user);
        InputContainer.userStates.put(user.getChatId(), userState);
        return user;
    }

    @Override
    @Async
    public void save(Long chatId, String username, String fullName, UserStates userState, String utmTag) {
        InputContainer.setUserState(chatId, userState);
        save(User.builder()
                .chatId(chatId)
                .userName(username)
                .fullName(fullName)
                .userState(userState)
                .utmTag(utmTag)
                .build());
    }

    @Override
    @Async
    public void addPhotoUrl(Long chatId, String photoUrl) {
        User user = get(chatId);
        if (user.getPhotosUrl() == null) {
            user.setPhotosUrl(List.of(photoUrl));
        } else
            user.getPhotosUrl().add(photoUrl);
        save(user);
    }

    @Override
    public List<User> getAllPayedUsers(UserPaymentStatus userPaymentStatus) {
        return userRepository.findAllByStatus(userPaymentStatus);
    }

    @Override
    public List<User> getAllWithUndefinedPhoneNumber() {
        return userRepository.findUsersWithInvalidPhoneNumber();
    }

//    @Override
//    public void addCourse(User user, Course course) {
//        if (user.getCourses() != null) {
//            if (!user.getCourses().contains(course)) {
//                user.getCourses().add(course);
//            }
//        }
//    }
}
