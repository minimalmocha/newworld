package NewWorld.service;

import NewWorld.domain.User;
import NewWorld.dto.UserDto;
import NewWorld.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingServiceImpl implements RankingService{

    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAttendanceRanking() {
        List<UserDto> results = null;

        List<User> users = userRepository.findTop100ByOrderByAttendanceAsc().orElseThrow(null);
        if (users != null){
            users.stream().forEach(s->results.add(UserDto.of(s).hideInfo()));
        }
        return results;
    }
    @Override
    public List<UserDto> getScoreRanking() {
        List<UserDto> results = null;
        List<User> users = userRepository.findTop100ByOrderByPointAsc().orElseThrow(null)
        if (users != null){
            users.stream().forEach(s->results.add(UserDto.of(s).hideInfo()));
        }
        return results;
    }

    @Override
    public List<UserDto> getTotalRanking() {
        List<UserDto> results = null;
        List<User> users = userRepository.findTop100ByOrderByAttendanceAscPointAsc().orElseThrow(null);
        if (users != null){
            users.stream().forEach(s->results.add(UserDto.of(s).hideInfo()));
        }

        return results;
    }
}
