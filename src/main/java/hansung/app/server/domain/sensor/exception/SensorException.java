package hansung.app.server.domain.sensor.exception;

import hansung.app.server.global.apiPayload.code.BaseErrorCode;
import hansung.app.server.global.apiPayload.exception.GeneralException;

public class SensorException extends GeneralException {
    public SensorException(BaseErrorCode code) {
        super(code);
    }
}
