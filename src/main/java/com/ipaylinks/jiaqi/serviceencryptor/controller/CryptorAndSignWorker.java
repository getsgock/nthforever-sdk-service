package com.ipaylinks.jiaqi.serviceencryptor.controller;

import com.alibaba.fastjson.JSON;
import com.ipay.api.common.IpayApiException;
import com.ipay.api.common.internal.util.IpayHashMap;
import com.ipay.api.common.internal.util.IpaySignature;
import com.ipay.api.common.internal.util.RequestParametersHolder;
import com.ipay.api.common.request.IpayRequest;
import com.ipay.api.common.response.IpayResponse;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Nthforever
 * @date 2018-09-28
 */
public class CryptorAndSignWorker implements CryptorAndSign {

    private IpayHashMap params;
    private Map<String,String> originParams;
    private IpayRequest request;
    private String publicKey;
    private String encryptKey;
    private String privateKey;
    private String decryptor;
    private String signType;
    /*********************************** 『必传参数 **********************************************/

    private static final String MUST_KEY_SIGN = "sign";
    private static final String MUST_KEY_METHOD = "method";
    private static final String MUST_KEY_VERSION = "version";
    private static final String MUST_KEY_APP_ID = "app_id";
    private static final String MUST_KEY_SIGN_TYPE = "sign_type";
    private static final String MUST_KEY_TERMINAL_TYPE = "terminal_type";
    private static final String MUST_KEY_TERMINAL_INFO = "terminal_info";
    private static final String MUST_KEY_NOTIFY_URL = "notify_url";
    private static final String MUST_KEY_RETURN_URL = "return_url";
    private static final String MUST_KEY_CHARSET = "charset";
    private static final String MUST_KEY_TIMESTAMP = "timestamp";

    /************************************ 必传参数』*********************************************/


    /*********************************** 『可选参数 **********************************************/

    private static final String OPT_KEY_FORMAT = "format";
    private static final String OPT_KEY_ACCESS_TOKEN = "auth_token";
    private static final String OPT_KEY_APP_AUTH_TOKEN = "app_auth_token";
    private static final String OPT_KEY_APP_IPAY_SDK = "ipay_sdk";
    private static final String OPT_KEY_PROD_CODE = "prod_code";

    /************************************ 可选参数』*********************************************/



    /*********************************** 『业务参数 **********************************************/

    private static final String BIZ_CONTENT_KEY = "biz_content";

    /************************************ 业务参数』*********************************************/

    private String[] mustArray = new String[]{MUST_KEY_METHOD,MUST_KEY_VERSION,MUST_KEY_APP_ID,
            MUST_KEY_SIGN_TYPE,MUST_KEY_TERMINAL_TYPE,MUST_KEY_TERMINAL_INFO,MUST_KEY_NOTIFY_URL,
            MUST_KEY_RETURN_URL,MUST_KEY_CHARSET,MUST_KEY_TIMESTAMP
    };

    private String[] optArray = new String[]{OPT_KEY_FORMAT,OPT_KEY_ACCESS_TOKEN,OPT_KEY_APP_AUTH_TOKEN,
            OPT_KEY_APP_IPAY_SDK,OPT_KEY_PROD_CODE
    };

    private static final String DEFAULT_SIGN_TYPE = "RSA2";
    private static final String DEFAULT_CHARSET = "UTF-8";

    private enum MsgEnum {

        /**
         * 结果状态
         */
        PARAMS_MISS("参数缺失"),
        PUBLIC_KEY_MISS("未设置公钥"),
        PRIVATE_KEY_MISS("未设置私钥"),
        ENCRYPT_KEY_MISS("未设置AES密钥"),
        SUCCESS("操作成功"),
        SIGN_CHECK_FAILURE("签名校验未通过"),
        DECRYPT_FAILURE("解密失败"),;

        private String msg;

        MsgEnum(String msg) {
            this.msg = msg;
        }
    }

    public String getPublicKey() {
        return publicKey;
    }

    public CryptorAndSignWorker setPublicKey(String publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    public String getEncryptKey() {
        return encryptKey;
    }

    public CryptorAndSignWorker setEncryptKey(String encryptKey) {
        this.encryptKey = encryptKey;
        return this;
    }

    public String getDecryptor() {
        return decryptor;
    }

    public String getSignType() {
        return signType;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public CryptorAndSignWorker setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
        return this;
    }

    public CryptorAndSignWorker(Map<String,String> params, IpayRequest request) {

        this.params = new IpayHashMap();
        this.originParams = params;

        Set<Map.Entry<String, String>> entries = params.entrySet();
        for (Map.Entry<String, String> next:entries){
            this.params.put(next.getKey(),next.getValue());
            System.out.println("getKey ==> "+next.getKey()+"getValue"+next.getValue());
        }
        this.request = request;
    }

    /**
     * 对请求验签和解密
     * @return
     */
    @Override
    public CryptorAndSignRes checkSignAndDecrypt() {
        CryptorAndSignRes result = new CryptorAndSignRes();
        if (StringUtils.isEmpty(publicKey)){
            result.setSuccess(false);
            result.setMsg(MsgEnum.PUBLIC_KEY_MISS.msg);
            return result;
        }
        if (null == params || params.size() < mustArray.length+1){
            result.setSuccess(false);
            result.setMsg(MsgEnum.PARAMS_MISS.msg);
            return result;
        }
        //提取必选参数
        IpayHashMap mustMap = new IpayHashMap();
        for (String mustKey: mustArray){
            String target = params.get(mustKey);
            if (StringUtils.isEmpty(target)){
                result.setSuccess(false);
                result.setMsg(MsgEnum.PARAMS_MISS.msg);
                return result;
            }
            mustMap.put(mustKey,target);
            params.remove(mustKey);
        }
        //提取可选参数
        IpayHashMap optMap = new IpayHashMap();
        for (String optKey:optArray){
            String target = params.get(optKey);
            if (StringUtils.isEmpty(target)){
                continue;
            }
            optMap.put(optKey,target);
            params.remove(optKey);
        }
        //提取应用参数
        params.remove(MUST_KEY_SIGN);
        RequestParametersHolder requestHolder = new RequestParametersHolder();
        requestHolder.setProtocalMustParams(mustMap);
        requestHolder.setProtocalOptParams(optMap);
        requestHolder.setApplicationParams(params);
        String signContent = IpaySignature.getSignatureContent(requestHolder);
        String sign = originParams.get(MUST_KEY_SIGN);
        signType = originParams.get(MUST_KEY_SIGN_TYPE);
        if (StringUtils.isEmpty(signType)){
            signType = DEFAULT_SIGN_TYPE;
        }
        String charSet = originParams.get(MUST_KEY_CHARSET);
        if (StringUtils.isEmpty(charSet)){
            charSet = DEFAULT_CHARSET;
        }

        boolean b = false;
        try {
            b = IpaySignature.rsaCheck(signContent, sign, publicKey, charSet, signType);
        } catch (IpayApiException e) {
            e.printStackTrace();
        }
        if (!b){
            result.setSuccess(false);
            result.setMsg(MsgEnum.SIGN_CHECK_FAILURE.msg);
            return result;
        }
        boolean needEncrypt = request.isNeedEncrypt();
        if (needEncrypt){
            if (StringUtils.isEmpty(encryptKey)){
                result.setSuccess(false);
                result.setMsg(MsgEnum.ENCRYPT_KEY_MISS.msg);
                return result;
            }
            String biz_content = params.get(BIZ_CONTENT_KEY);
            if (!StringUtils.isEmpty(biz_content)){
                decryptor = CryptorUtil.decryptor(biz_content,encryptKey);
                if (StringUtils.isEmpty(decryptor)){
                    result.setSuccess(false);
                    result.setMsg(MsgEnum.DECRYPT_FAILURE.msg);
                    return result;
                }
            }
        }
        result.setSuccess(true);
        result.setMsg(MsgEnum.SUCCESS.msg);
        result.setResult(originParams);
        return result;
    }

    @Override
    public String doEncryptAndSign(IpayResponse response) {
        Map<String,Object> res = new HashMap<>();
        String result = "";
        if (StringUtils.isEmpty(encryptKey)){
            throw new RuntimeException(MsgEnum.ENCRYPT_KEY_MISS.msg);
        }
        if (StringUtils.isEmpty(privateKey)){
            throw new RuntimeException(MsgEnum.PRIVATE_KEY_MISS.msg);
        }
        //客户端是按照此字段解析，不能改
        String rootNode = request.getApiMethodName().replace('.', '_') + "_response";
        String responseStr = JSON.toJSONString(response);
        if (request.isNeedEncrypt()){
            String encryptor = CryptorUtil.encryptor(responseStr, encryptKey);
            res.put(rootNode,encryptor);
            String sign = CryptorUtil.sign("\""+encryptor+"\"", privateKey, signType);
            res.put(MUST_KEY_SIGN,sign);
        }else {
            res.put(rootNode,response);
            String sign = CryptorUtil.sign(responseStr, privateKey, signType);
            res.put(MUST_KEY_SIGN,sign);
        }
        result = JSON.toJSONString(res);
        return result;
    }

    @Override
    public <T> T getBizMode(Class<T> clazz) {
        T t = JSON.parseObject(decryptor, clazz);
        return t;
    }
}
