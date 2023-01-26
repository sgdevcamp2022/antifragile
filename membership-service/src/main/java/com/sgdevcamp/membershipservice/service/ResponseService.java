package com.sgdevcamp.membershipservice.service;

import com.sgdevcamp.membershipservice.dto.response.CommonResponse;
import com.sgdevcamp.membershipservice.dto.response.Response;
import com.sgdevcamp.membershipservice.exception.CustomExceptionStatus;
import org.springframework.stereotype.Service;

@Service
public class ResponseService {
    public CommonResponse getSuccessResponse(){
        CommonResponse response = new CommonResponse();
        response.setIsSuccess(true);
        response.setCode(1000);
        response.setMessage("요청에 성공하였습니다.");
        return response;
    }

    public <T> Response<T> getDataResponse(T data){
        Response<T> response = new Response<>();
        response.setResult(data);
        response.setIsSuccess(true);
        response.setCode(1000);
        response.setMessage("요청에 성공하였습니다.");
        return response;
    }

    public CommonResponse getExceptionResponse(CustomExceptionStatus status){
        CommonResponse response = new CommonResponse();
        response.setIsSuccess(status.isSuccess());
        response.setCode(status.getCode());
        response.setMessage(status.getMessage());
        return response;
    }
}
