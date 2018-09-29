# nthforever-sdk-service
阿里商户调用服务端，加解密及签名校验等封装

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
        //原参数
        Map<String, String> result = cryptorAndSignRes.getResult();
        //解密后字符串
        String decryptor = worker.getDecryptor();
        //
        Biz biz =  worker.getBizMode(Biz.class);
        myResponse.setSchool("Harvard University");
        myResponse.setNow(System.currentTimeMillis());
        myResponse.setCode("200");
        myResponse.setSubCode("2000");
        myResponse.setMsg("袜子200一双");
        myResponse.setSubMsg("裤子2000一条");
        return worker.doEncryptAndSign(myResponse);
    }
