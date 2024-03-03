package NewWorld.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@AllArgsConstructor
public class CustomError extends Exception{
   ErrorCode errorCode;

}
