package com.ipaylinks.jiaqi.serviceencryptor.controller;

import java.util.Map;

/**
 * @author Nthforever
 * @date 2018-09-28
 * @describe 验签和加密结果
 */
public class CryptorAndSignRes {

    private boolean success;
    private String msg;
    private Map<String, String> result;
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, String> getResult() {
        return result;
    }

    public void setResult(Map<String, String> result) {
        this.result = result;
    }
}
