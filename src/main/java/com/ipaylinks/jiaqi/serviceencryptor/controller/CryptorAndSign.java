package com.ipaylinks.jiaqi.serviceencryptor.controller;

import com.ipay.api.common.response.IpayResponse;


/**
 * @author Nthforever
 * @date 2018-09-28
 */
public interface CryptorAndSign {

    /**
     *接受参数时签名校验和
     * @return
     */
    CryptorAndSignRes checkSignAndDecrypt();

    /**
     * 返回操作时的加密签名操作
     * @param response
     * @return
     */
    String doEncryptAndSign(IpayResponse response);

    /**
     * 获取解密后的业务bean
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T getBizMode(Class<T> clazz);
}
