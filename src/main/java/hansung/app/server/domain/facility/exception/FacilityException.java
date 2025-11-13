package hansung.app.server.domain.facility.exception;

import hansung.app.server.global.apiPayload.code.BaseErrorCode;
import hansung.app.server.global.apiPayload.exception.GeneralException;

public class FacilityException  extends GeneralException {
    public FacilityException(BaseErrorCode code) {super(code);}
}
