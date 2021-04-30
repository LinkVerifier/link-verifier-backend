package kjm.linkverifier.auth.exceptions;

import kjm.linkverifier.auth.web.response.ExceptionResponse;
import kjm.linkverifier.auth.web.response.SignInFailureResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SignInExceptionHandler {
    @ExceptionHandler(NotActivatedAccountException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse getNotActivatedAccountException(NotActivatedAccountException ex){
        return new ExceptionResponse(ex.getMessage());
    }
    @ExceptionHandler(WrongEmailException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public SignInFailureResponse getBadEmailException(WrongEmailException ex){
        return new SignInFailureResponse(ex.getMessage());
    }
    @ExceptionHandler(WrongPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public SignInFailureResponse getBadPasswordException(WrongPasswordException ex){
        return new SignInFailureResponse(ex.getMessage());
    }
}
