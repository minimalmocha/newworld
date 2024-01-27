package NewWorld.service;

import NewWorld.domain.User;
import NewWorld.dto.UserDto;
import NewWorld.exception.JoinException;
import NewWorld.exception.LoginException;
import NewWorld.exception.NotChangeException;
import NewWorld.exception.NotfindUserException;
import NewWorld.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 2024.01.14 jeonil
 * 로그인 처리
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * 회원가입 아이디 중복체크
     *
     * @param loginId
     * @return
     */
    @Override
    public Boolean checkIdValidation(String loginId) {
        Boolean validationCheck = false;
        User idCheck = userRepository.findUserByUserId(loginId);

        if (idCheck != null) {
            validationCheck = true;
        }
        return validationCheck;
    }

    /**
     * 회원가입
     *
     * @param joinInfo
     * @throws JoinException
     */
    @Override
    public String join(UserDto joinInfo) throws JoinException {

        String phoneNumber = joinInfo.getPhoneNumber();
        String name = joinInfo.getName();
        Boolean check = checkIdValidation(joinInfo.getUserId());
        //회원 중복검사
        if (check) {
            throw new JoinException("이미 존재하는 회원입니다.");
        }

        User newUser = User.builder().
                userId(joinInfo.getUserId()).
                userPassword(joinInfo.getUserPassword()).
                name(name).
                nickname(joinInfo.getNickname()).
                phoneNumber(phoneNumber).
                birthday(joinInfo.getBirthday()).
                build();

        userRepository.save(newUser);

        return newUser.getName();
    }


    /**
     * user기본정보 수정
     */
    @Override
    public void updateUserInfo(UserDto changeInfo) throws Exception {
        String phoneNumber = changeInfo.getPhoneNumber();
        String name = changeInfo.getName();
        String newNn = changeInfo.getNickname();
        String newPn = changeInfo.getPhoneNumber();
        String newBd = changeInfo.getBirthday();

        User user = getUser(phoneNumber, name);

        Boolean checkChangeInfo = checkChangeInfo(changeInfo, user);
        //변경된 정보가 있는지 체크
        if (checkChangeInfo) {
            if (changeInfo.getNickname().isEmpty()) newNn = user.getNickname();
            if (changeInfo.getPhoneNumber().isEmpty()) newPn = user.getNickname();
            if (changeInfo.getBirthday().isEmpty()) newBd = user.getBirthday();

            user.basicInfoUpdate(newNn, newPn, newBd);
        } else {
            throw new NotChangeException("수정사항이 없습니다");
        }
        userRepository.save(user);
    }

    /**
     * user기본정보 조회
     */
    @Override
    public UserDto getUserInfo(String userName, String userNickname) throws NotfindUserException {

        User user = getUser(userName, userNickname);

        UserDto userDto = new UserDto();
        UserDto result = userDto.usertoDto(user);

        return result;
    }

    /**
     * 회원탈퇴
     *
     * @param userInfo
     * @throws JoinException
     */
    public void withdraw(String userInfo) {
        // 플레이기록 , 게시물 제거
    }

    /**
     * 2024-01-23 jeonill
     * user기본정보 조회
     */
    private User getUser(String userName, String userNickname) throws NotfindUserException {
        User userInfo = userRepository.findUserByNameAndNickname(userName, userNickname);
        if (userInfo == null) {
            throw new NotfindUserException("찾을수 없는 회원입니다");
        }
        return userInfo;
    }

    /**
     * 변경된 정보 확인
     *
     * @param changeInfo
     * @param userInfo
     * @return
     */
    private Boolean checkChangeInfo(UserDto changeInfo, User userInfo) {
        Boolean check = true;

        if (changeInfo.getBirthday() == userInfo.getBirthday()) {
            check = false;
        }
        if (changeInfo.getNickname() == userInfo.getNickname()) {
            check = false;
        }
        if (changeInfo.getBirthday() == userInfo.getBirthday()) {
            check = false;
        }
        if (changeInfo.getPhoneNumber() == userInfo.getPhoneNumber()) {
            check = false;
        }

        return check;
    }
}