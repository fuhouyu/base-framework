/*
 * Copyright 2024-2034 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fuhouyu.framework.security.core.authentication.wechat;

import com.fasterxml.jackson.annotation.JsonAlias;

/**
 * <p>
 * 微信用户详情
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/15 12:33
 */
public class WechatAppletsUserInfo {

    /**
     * 会话密钥
     */
    @JsonAlias("session_key")
    private String sessionKey;

    /**
     * 用户在开放平台的唯一标识符，若当前小程序已绑定到微信开放平台账号下会返回
     */
    @JsonAlias("unionid")
    private String unionId;

    /**
     * 错误信息
     */
    @JsonAlias("errmsg")
    private String errMsg;

    /**
     * 用户唯一标识
     */
    @JsonAlias("openid")
    private String openId;

    /**
     * 错误码
     */
    @JsonAlias("errcode")
    private Integer errCode;

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }

    @Override
    public String toString() {
        return "WechatAppletsUserInfo{" +
                "sessionKey='" + sessionKey + '\'' +
                ", unionId='" + unionId + '\'' +
                ", errMsg='" + errMsg + '\'' +
                ", openId='" + openId + '\'' +
                ", errCode=" + errCode +
                '}';
    }
}
