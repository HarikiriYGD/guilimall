package com.swjtu.guilimall.product.exception;

import com.swjtu.common.execption.BizCodeEnume;
import com.swjtu.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Lil_boat
 * @Date: 2022/8/22 21:48
 * @Description: 集中处理所有异常
 */
@RestControllerAdvice(basePackages = "com.swjtu.guilimall.product.controller")
@Slf4j
public class GuilimallExceptionControllerAdvice {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleValidException(MethodArgumentNotValidException e) {
        log.error("数据校验出现问题{},异常类型{}", e.getMessage(), e.getClass());

        BindingResult bindingResult = e.getBindingResult();

        Map<String, String> map = new HashMap<>();
        bindingResult.getFieldErrors().forEach(error -> {
            // 获取校验错误消息
            String message = error.getDefaultMessage();
            // 获取校验错误的字段
            String field = error.getField();
            map.put(message, field);
        });
        return R.error(BizCodeEnume.VAILD_EXCEPTION.getCode(), BizCodeEnume.VAILD_EXCEPTION.getMsg()).put("data", map);
    }

    @ExceptionHandler(value = Throwable.class)
    public R handleException(Throwable throwable) {
        log.error(throwable.getMessage(), throwable.getClass());
        return R.error(BizCodeEnume.UNKNOW_EXCEPTION.getCode(), BizCodeEnume.UNKNOW_EXCEPTION.getMsg());
    }

}
