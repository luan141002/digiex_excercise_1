package com.example.lab_1.common.exceptions;


import com.example.lab_1.common.utils.RestAPIStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author 689Cloud
 */

public class ApplicationException extends RuntimeException {

    private RestAPIStatus apiStatus;
    private Object data;

    public ApplicationException(RestAPIStatus apiStatus) {
        super(apiStatus.getDescription());
        this.apiStatus = apiStatus;
    }

    /**
     * This constructor is builded only for handling BAD REQUEST exception
     * Careful when use it with other purpose ;)
     *
     * @param apiStatus
     * @param data
     */
    public ApplicationException(RestAPIStatus apiStatus, Object data) {
        this(apiStatus);
        this.data = data;
    }

    public ApplicationException(Throwable cause) {
        super(cause);
    }

    public ApplicationException(RestAPIStatus apiStatus, String message) {
        super(message);
        this.apiStatus = apiStatus;
    }

    public RestAPIStatus getApiStatus() {
        return apiStatus;
    }

    public Object getData() {
        return data;
    }


}
