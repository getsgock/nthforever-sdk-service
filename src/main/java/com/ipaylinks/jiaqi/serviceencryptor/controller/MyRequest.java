package com.ipaylinks.jiaqi.serviceencryptor.controller;

import com.alibaba.fastjson.JSON;
import com.ipay.api.common.IpayObject;
import com.ipay.api.common.request.IpayRequest;

import java.util.HashMap;
import java.util.Map;

public class MyRequest implements IpayRequest<MyResponse> {

    
    /**************************基础参数*********************************/
    private String bizContent;
    private String terminalType = "IOS";
    private String terminalInfo = "nthforever";
    private String prodCode = "ipayLinks";
    private String notifyUrl ="https://www.ipaylinks.com/cn/landing.html";
    private String returnUrl ="https://www.ipaylinks.com/cn/landing.html";
    private boolean needEncrypt=true;
    private IpayObject bizModel=null;



    /*****************************业务参数******************************/

    private String name;
    private String age;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    /***********************************************************/
    public String getBizContent() {
        return bizContent;
    }

    public void setBizContent(String bizContent) {
        this.bizContent = bizContent;
    }


    @Override
    public String getApiMethodName() {
        return "test.hello";
    }

    @Override
    public Map<String, String> getTextParams() {
        //通过BO形式设置参数
        Map txtParams = new HashMap();
        txtParams.put("name", this.name);
        txtParams.put("age", this.age);
        if (isNeedEncrypt()){
            bizContent = JSON.toJSONString(txtParams);
            txtParams.clear();
            txtParams.put("biz_content", bizContent );
        }else {

        }
        return txtParams;
    }
    @Override
    public String getApiVersion() {
        return "3.0.1";
    }
    @Override
    public void setApiVersion(String s) {

    }
    @Override
    public String getTerminalType() {
        return terminalType;
    }
    @Override
    public void setTerminalType(String s) {
        terminalType =s;
    }
    @Override
    public String getTerminalInfo() {
        return terminalInfo;
    }
    @Override
    public void setTerminalInfo(String s) {
        terminalInfo =s;
    }
    @Override
    public String getProdCode() {
        return prodCode;
    }
    @Override
    public void setProdCode(String s) {
        prodCode =s;
    }
    @Override
    public String getNotifyUrl() {
        return notifyUrl;
    }
    @Override
    public void setNotifyUrl(String s) {
        notifyUrl =s;
    }
    @Override
    public String getReturnUrl() {
        return returnUrl;
    }
    @Override
    public void setReturnUrl(String s) {
        returnUrl =s;
    }
    @Override
    public Class<MyResponse> getResponseClass() {
        return MyResponse.class;
    }
    @Override
    public boolean isNeedEncrypt() {
        return needEncrypt;
    }
    @Override
    public void setNeedEncrypt(boolean b) {
        this.needEncrypt = b;
    }
    @Override
    public IpayObject getBizModel() {
        return null;
    }
    @Override
    public void setBizModel(IpayObject ipayObject) {

    }
}
