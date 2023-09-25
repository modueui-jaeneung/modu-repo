package com.modu.MemberServer.exception.exhandler.advice;

import com.modu.MemberServer.exception.*;
import com.modu.MemberServer.exception.exhandler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class ExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoSuchElementException.class)
    public ErrorResult noSuchElementExHandle(NoSuchElementException e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateMemberException.class)
    public ErrorResult duplicateMemberExHandle(DuplicateMemberException e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PasswordNotEqualException.class)
    public ErrorResult passwordNotEqualExHandle(PasswordNotEqualException e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalArgumentExHandle(IllegalArgumentException e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EmailFormatNotSatisfiedException.class)
    public ErrorResult emailFormatNotSatisfiedExHandle(EmailFormatNotSatisfiedException e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PasswordNotSatisfiedException.class)
    public ErrorResult passwordNotSatisfiedExHandle(PasswordNotSatisfiedException e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidSocialTypeException.class)
    public ErrorResult invalidSocialTypeExHandle(InvalidSocialTypeException e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult(e.getMessage());
    }



}
