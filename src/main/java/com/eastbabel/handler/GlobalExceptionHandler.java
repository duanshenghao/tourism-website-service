package com.eastbabel.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;

import com.eastbabel.bo.base.ResponseEntity;
import com.eastbabel.exception.CustomException;
import com.eastbabel.exception.UnauthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@ControllerAdvice
@Order(0)
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public GlobalExceptionHandler() {
    }


    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<String> handleCustomAuthException(ConstraintViolationException e) {
        log.error(e.getMessage(), e);
        ResponseEntity<String> res = new ResponseEntity();
        res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        res.setMessage(e.getMessage());
        return res;
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<String> handleCustomAuthException(MethodArgumentNotValidException e) {
        String message = (String) e.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(";"));
        log.error(message, e);
        ResponseEntity<String> res = new ResponseEntity();
        res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        res.setMessage(message);
        return res;
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<String> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error(e.getMessage(), e);
        ResponseEntity<String> res = new ResponseEntity();
        res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        res.setMessage(e.getMessage());
        return res;
    }

    @ExceptionHandler({UnauthException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ResponseEntity<String> handleUnauthException(UnauthException e) {
        log.error(e.getMessage(), e);
        ResponseEntity<String> res = new ResponseEntity();
        res.setStatus(HttpStatus.UNAUTHORIZED.value());
        res.setMessage(e.getMessage());
        return res;
    }

    @ExceptionHandler({CustomException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<String> handleCustomException(CustomException e) {
        log.error(e.getMessage(), e);
        ResponseEntity<String> res = new ResponseEntity();
        res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        res.setMessage(e.getMessage());
        return res;
    }

    @ExceptionHandler({HttpServerErrorException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<String> handleHttpException(HttpServerErrorException e) {
        log.error(e.getMessage(), e);
        ResponseEntity<String> res = new ResponseEntity();
        res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

        String message;
        try {
            JSONObject data = (JSONObject) JSONArray.parseArray(e.getMessage().substring(e.getMessage().indexOf(": ") + 2)).get(0);
            message = data.get("message").toString();
        } catch (Exception var5) {
            message = e.getMessage();
        }

        res.setMessage(message);
        return res;
    }

    @ExceptionHandler({HttpClientErrorException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ResponseEntity<String> handleHttpClientErrorException(HttpClientErrorException e) {
        log.error(e.getMessage(), e);
        ResponseEntity<String> res = new ResponseEntity();
        res.setStatus(HttpStatus.UNAUTHORIZED.value());

        String message;
        try {
            JSONObject data = (JSONObject) JSONArray.parseArray(e.getMessage().substring(e.getMessage().indexOf(": ") + 2)).get(0);
            message = data.get("message").toString();
        } catch (Exception var5) {
            message = e.getMessage();
        }

        res.setMessage(message);
        return res;
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<String> handleException(Exception e) {
        log.error(e.getMessage(), e);
        ResponseEntity<String> res = new ResponseEntity();
        res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        res.setMessage("服务器错误，请稍后重试或联系管理员。");
        return res;
    }
}
