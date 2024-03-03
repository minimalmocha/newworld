package NewWorld.service;

import NewWorld.domain.*;
import NewWorld.dto.ChangeInfoDto;
import NewWorld.dto.SolvedQuizDto;
import NewWorld.dto.UserDto;
import NewWorld.exception.CustomError;
import NewWorld.exception.ErrorCode;
import NewWorld.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 로그인 처리
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * 회원가입 아이디 중복체크
     *
     * @param loginId
     * @return
     */
    public Boolean isLoginIdPresent(String loginId) throws CustomError {
        User idCheck = userRepository.findUserByUserId(loginId)
                .orElseThrow(()->new CustomError(ErrorCode.USER_NOT_FOUND));;
        return idCheck != null;
    }

    /**
     * 회원가입 중복체크
     *
     * @param phoneNumber
     * @param name
     * @return
     */
    public Boolean isUserPresent(String phoneNumber, String name) throws CustomError {
        User userCheck = userRepository.findUserByNameAndPhoneNumber(name, phoneNumber)
                .orElseThrow(()->new CustomError(ErrorCode.USER_NOT_FOUND));
        return userCheck != null;
    }

    /**
     * 회원가입 중복체크
     *
     * @param nickname
     * @return
     */
    public Boolean isNicknamePresent(String nickname) throws CustomError {
        User userCheck = getUser(nickname);
        return userCheck != null;
    }

    /**
     * 회원가입
     *
     * @param joinInfo
     */
    @Override
    public String join(UserDto joinInfo) throws CustomError {
        //유저 정보 중복체크
        String result = validateJoinUser(joinInfo);

        User user = User.of(joinInfo);

        userRepository.save(user);
        return result;
    }


    /**
     * user기본정보 수정
     */
    @Override
    public String updateUserInfo(ChangeInfoDto changeInfoDto) throws CustomError {

        User user = userRepository.findUserByUserId(changeInfoDto.getUserId())
                .orElseThrow(()->new CustomError(ErrorCode.USER_NOT_FOUND));


        String currentPassword = user.getUserPassword();
        String newPassword = changeInfoDto.getNewPassword();

        if (changeInfoDto.getCurrentPassword() != currentPassword){
            throw new CustomError(ErrorCode.SAME_PASSWORD) ;
        }
        if (newPassword == currentPassword){
            throw new CustomError(ErrorCode.NOT_CHANGE) ;
        }

        user.changePassword(changeInfoDto.getNewPassword());

        return "s";
    }

    /**
     * user기본정보 조회
     */
    @Override
    public UserDto getUserInfo(UserDto userDto) throws CustomError {
        User user = getUser(userDto.getNickname());
        if(user == null){
            return null;
        }
        UserDto result = UserDto.of(user);

//        ImageFile imageFile = user.getImageFile();
//        String fileName = imageFile.getFileName();
//        //추후업로드경로필요
//        String path = imageFile.getPath();
//        File file = new File(path, fileName);
//
//        if(!file.isFile()) return null;
//        result.setImageFile(file);

        List<UserQuizSolvedDate> quizList = user.getQuizList();
        if(quizList == null){
            result.setPuzzleCount(0);
        }else{
            result.setPuzzleCount(quizList.size());
        }

        return result;
    }

    /**
     * 내가 푼 문제 불러오기
     * @param userDto
     * @return
     */
    public List<SolvedQuizDto> getSolveQuizList(UserDto userDto) throws CustomError {
        List<SolvedQuizDto> result = new ArrayList<>();
        User user = userRepository.findByNickname(userDto.getNickname())
                .orElseThrow(()->new CustomError(ErrorCode.USER_NOT_FOUND));

        List<UserQuizSolvedDate> solvedQuizList = user.getQuizList();

        for(UserQuizSolvedDate solvedQuiz : solvedQuizList){
            SolvedQuizDto solvedQuizDto = SolvedQuizDto.of(solvedQuiz);
            result.add(solvedQuizDto);
        }

        return result;
    }
    /**
     * 회원탈퇴
     * @param userInfo
     */
    public void withdraw(String userInfo) {
        // 플레이기록 , 게시물 제거
    }

    /**
     * user기본정보 조회
     *  @param userNickname
     */
    private User getUser(String userNickname) throws CustomError {
        User user = userRepository.findByNickname(userNickname)
                .orElseThrow(()->new CustomError(ErrorCode.USER_NOT_FOUND));
        return user;
    }

    private String validateJoinUser(UserDto joinInfo) throws CustomError {
        if (isLoginIdPresent(joinInfo.getUserId())) {
            return "f1";
        }

        if(isUserPresent(joinInfo.getName(), joinInfo.getPhoneNumber())){
            return "f2";
        }

        if(isNicknamePresent(joinInfo.getNickname())){
            return "f3";
        }

        return null;
    }
}
