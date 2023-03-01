package com.kingstar.login.controller;

import com.kingstar.login.aop.LimitRequest;
import com.kingstar.login.bean.AccessTokenDTO;
import com.kingstar.login.bean.CommonResult;
import com.kingstar.login.bean.ShortMessage;
import com.kingstar.login.utils.JwtUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/access")
public class AccessController {

    /**
     * token生成
     *
     * @param accessTokenDTO
     * @return
     */
    @LimitRequest
    @PostMapping("/token")
    public CommonResult<String> getAccessToken(@RequestBody AccessTokenDTO accessTokenDTO) {
        return CommonResult.success(JwtUtils.createToken(accessTokenDTO.getApplicationName(), accessTokenDTO.getAcceptanceFormList().toArray(new String[0])));
    }


    /**
     * 发送短信
     *
     * @param shortMessage
     * @return
     */
    @LimitRequest
    @PostMapping("/send/msg/code")
    public CommonResult<String> sendMsgCode(@RequestBody ShortMessage shortMessage) {


        return CommonResult.success("已发送");
    }

}
