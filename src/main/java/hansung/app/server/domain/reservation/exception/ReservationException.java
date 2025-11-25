package hansung.app.server.domain.reservation.exception;

import hansung.app.server.global.apiPayload.code.BaseErrorCode;
import hansung.app.server.global.apiPayload.exception.GeneralException;

public class ReservationException extends GeneralException {
    public ReservationException(BaseErrorCode code) {
        super(code);
    }
}
