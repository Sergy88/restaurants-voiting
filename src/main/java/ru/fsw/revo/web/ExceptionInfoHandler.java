package ru.fsw.revo.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.fsw.revo.utils.ValidationUtil;
import ru.fsw.revo.utils.exception.*;

import javax.servlet.http.HttpServletRequest;

import static ru.fsw.revo.utils.exception.ErrorType.*;

@RestControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class ExceptionInfoHandler {
    private static Logger log = LoggerFactory.getLogger(ExceptionInfoHandler.class);

    //  http://stackoverflow.com/a/22358422/548473
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(NotFoundException.class)
    public ErrorInfo handleError(HttpServletRequest req, NotFoundException e) {
        return getErrorInfo(req, e, DATA_NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.CONFLICT)  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorInfo conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        return getErrorInfo(req, e, DATA_ERROR);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)  // 422
    @ExceptionHandler({IllegalRequestDataException.class, MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class, VotePerDayException.class, VoteTimeModConstraint.class})
    public ErrorInfo illegalRequestDataError(HttpServletRequest req, Exception e) {
        return getErrorInfo(req, e, VALIDATION_ERROR);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorInfo handleError(HttpServletRequest req, Exception e) {
        return getErrorInfo(req, e, APP_ERROR);
    }

    //    https://stackoverflow.com/questions/538870/should-private-helper-methods-be-static-if-they-can-be-static
    private static ErrorInfo getErrorInfo(HttpServletRequest req, Exception e, ErrorType errorType) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        return new ErrorInfo(req.getRequestURL(), errorType, rootCause.toString());
    }
}
