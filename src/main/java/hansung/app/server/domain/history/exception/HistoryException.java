package hansung.app.server.domain.history.exception;

import hansung.app.server.global.apiPayload.code.BaseErrorCode;
import hansung.app.server.global.apiPayload.exception.GeneralException;

public class HistoryException extends GeneralException {
    public HistoryException(BaseErrorCode code) {
        super(code);
    }
}
