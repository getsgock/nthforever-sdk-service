package com.ipaylinks.jiaqi.serviceencryptor.controller;

import com.alibaba.fastjson.JSON;
import com.ipay.api.common.IpayApiException;
import com.ipay.api.common.IpayConstants;
import com.ipay.api.common.IpayParser;
import com.ipay.api.common.internal.parser.json.ObjectJsonParser;
import com.ipay.api.common.internal.parser.xml.ObjectXmlParser;
import com.ipay.api.common.internal.util.IpayHashMap;
import com.ipay.api.common.internal.util.IpaySignature;
import com.ipay.api.common.internal.util.RequestParametersHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.annotation.MultipartConfig;
import java.nio.charset.Charset;
import java.util.*;

@Controller
@RequestMapping(value = "/test")
@PropertySource("/abc/ipay.properties")
public class TestController {

    @Value("${ipay.name}")
    private String name;
    @Value("${ipay.publicKey}")
    private String publicKey;
    @Value("${ipay.privateKey}")
    private String privateKey;
    @Value("${ipay.encryptKey}")
    private String encryptKey;
    @Value("${ipay.charSet}")
    private String charSet;

    @RequestMapping(value = "/index")
    public String index(@RequestParam Map<String,String> params){
        Set<Map.Entry<String, String>> entries = params.entrySet();
        for (Map.Entry<String, String> next:entries){
            System.out.println("||--> "+next.getKey()+" == "+next.getValue());
        }
        String method = params.get("method");
        boolean empty = StringUtils.isEmpty(method);
        if (!empty){
            return "forward:"+method;
        }
        return "1111111111111111111";
    }



    @RequestMapping(value = "/hello")
    @ResponseBody
    public String sayHello(@RequestParam Map<String,String> params){


        CryptorAndSignWorker worker = new CryptorAndSignWorker(params,new MyRequest());
        worker.setEncryptKey(encryptKey)
                .setPrivateKey(privateKey)
                .setPublicKey(publicKey);

        CryptorAndSignRes cryptorAndSignRes = worker.checkSignAndDecrypt();
        MyResponse myResponse = new MyResponse();
        if (!cryptorAndSignRes.isSuccess()){
            myResponse.setCode("404");
            myResponse.setSubCode("4040");
            myResponse.setMsg("你错了");
            myResponse.setSubMsg("你真的错了");
            return worker.doEncryptAndSign(myResponse);
        }
        myResponse.setSchool("Harvard University");
        myResponse.setNow(System.currentTimeMillis());
        myResponse.setCode("200");
        myResponse.setSubCode("2000");
        myResponse.setMsg("袜子200一双");
        myResponse.setSubMsg("裤子2000一条");
        return worker.doEncryptAndSign(myResponse);
    }

    @RequestMapping(value = "/data")
    @ResponseBody
    public String data(@RequestBody byte[] data){


            String s = new String(data,Charset.forName("utf-8"));
            System.out.println("RequestBody  = "+s+"   ||end");

        return "-=============";
    }

}
