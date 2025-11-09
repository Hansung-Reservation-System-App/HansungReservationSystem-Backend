package hansung.app.server.domain.user.exception;

import hansung.app.server.domain.user.exception.code.UserErrorCode;
import hansung.app.server.global.apiPayload.exception.GeneralException;

public class UserException extends GeneralException {
    public UserException(UserErrorCode code) {
        super(code);
    }
}
