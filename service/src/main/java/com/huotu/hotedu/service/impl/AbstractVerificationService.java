package com.huotu.hotedu.service.impl;

import com.huotu.hotedu.exception.InterrelatedException;
import com.huotu.hotedu.entity.Result;
import com.huotu.hotedu.entity.VerificationCode;
import com.huotu.hotedu.model.CodeType;
import com.huotu.hotedu.model.VerificationType;
import com.huotu.hotedu.repository.VerificationCodeRepository;
import com.huotu.hotedu.service.VerificationService;
import com.huotu.hotedu.util.SysRegex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author CJ
 */
public abstract class AbstractVerificationService implements VerificationService {

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;
    /**
     * 允许重发时间间隔60
     */
    private int gapSeconds = 60;

    private int invalidSeconds = 600;

    @Transactional
    public void sendCode(String mobile, VerificationService.VerificationProject project, String code, Date currentDate, VerificationType type, CodeType sentType)
            throws IllegalStateException, IllegalArgumentException, NoSuchMethodException, InterrelatedException {
        if (!SysRegex.IsValidMobileNo(mobile)) {
            throw new IllegalArgumentException("号码不对");
        }
        if (sentType == null) {
            sentType = CodeType.text;
        }

        if (!supportVoice() && sentType == CodeType.voice) {
            throw new NoSuchMethodException("还不支持语音播报");
        }

        List<VerificationCode> codeList = verificationCodeRepository.findByMobileAndTypeAndCodeTypeAndSendTimeGreaterThan(mobile, type, sentType,new Date(currentDate.getTime() - gapSeconds * 1000));
        if (codeList.size()>0) {
            //刚刚发送过
            throw new IllegalStateException("刚刚发过");
        } else {
            VerificationCode verificationCode = new VerificationCode();
            verificationCode.setMobile(mobile);
            verificationCode.setType(type);
            verificationCode.setCodeType(sentType);
            verificationCode.setSendTime(currentDate);
            verificationCode.setCode(code);
            doSend(project, verificationCode);
            verificationCodeRepository.save(verificationCode);
        }
    }

    protected abstract void doSend(VerificationProject project, VerificationCode code) throws InterrelatedException;

    @Transactional
    public Result verifyCode(String phoneNo,String code,VerificationType type) throws IllegalArgumentException {
        Result result = new Result();
        if(!SysRegex.IsValidNum(code)) {
            result.setMessage("无效的验证码");
        }else {
            List<VerificationCode> codeList = verificationCodeRepository.findByMobileAndTypeOrderBySendTimeDesc(phoneNo, type);
            if (codeList.size() == 0) {
                result.setMessage("请先发送验证码");
            } else {
                VerificationCode verificationCode = codeList.get(0);
                if ((verificationCode.getSendTime().getTime() + invalidSeconds * 1000) < new Date().getTime()) {
                    result.setMessage("验证码已失效，请重新发送");
                } else if(verificationCode.getCode().equals(code)){
                    result.setStatus(1);
                    result.setMessage("验证码正确");
                }else {
                    result.setMessage("验证码错误");
                }
            }
        }
        return result;
    }

    public int getGapSeconds() {
        return gapSeconds;
    }

    public void setGapSeconds(int gapSeconds) {
        this.gapSeconds = gapSeconds;
    }

    public int getInvalidSeconds() {
        return invalidSeconds;
    }

    public void setInvalidSeconds(int invalidSeconds) {
        this.invalidSeconds = invalidSeconds;
    }
}
