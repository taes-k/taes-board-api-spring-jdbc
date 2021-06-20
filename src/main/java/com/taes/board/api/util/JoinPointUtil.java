package com.taes.board.api.util;

import com.taes.board.api.exception.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

public class JoinPointUtil
{
    private JoinPointUtil()
    {
        throw new IllegalStateException("Utility class");
    }

    public static <T> T getParamByClass(JoinPoint jp, Class<T> clazz)
    {
        return Arrays.stream(jp.getArgs())
            .filter(clazz::isInstance)
            .map(clazz::cast)
            .findFirst()
            .orElseThrow(() -> new ApiException("정의된 Paramter를 찾을수 없습니다."));
    }

    public static Object getParamByName(JoinPoint jp, String targetName)
    {
        MethodSignature signature = (MethodSignature) jp.getSignature();
        Method method = signature.getMethod();

        Parameter[] params = method.getParameters();
        for (int i = 0; i < params.length; i++)
        {
            if (StringUtils.equals(targetName, params[i].getName()))
                return jp.getArgs()[i];
        }

        throw new ApiException("정의된 Paramter를 찾을수 없습니다.");
    }
}
