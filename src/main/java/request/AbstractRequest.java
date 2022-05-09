package request;

import config.RequestType;

public class AbstractRequest {
    protected RequestType type;

    protected AbstractRequest() {
        this.type = RequestType.UNKNOWN;
    }

    public RequestType getType() {
        return type;
    }
}
