package com.huotu.hotedu.service.impl;

import com.huotu.hotedu.common.exception.InterrelatedException;
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
     * 允许间隔60
     */
    private int gapSeconds = 60;

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

        VerificationCode verificationCode = verificationCodeRepository.findByMobileAndTypeAndCodeType(mobile, type, sentType);
        if (verificationCode != null) {
            //刚刚发送过
            if (currentDate.getTime() - verificationCode.getSendTime().getTime() < gapSeconds * 1000) {
                throw new IllegalStateException("刚刚发过");
            }
        } else {
            verificationCode = new VerificationCode();
            verificationCode.setMobile(mobile);
            verificationCode.setType(type);
            verificationCode.setCodeType(sentType);
        }
        verificationCode.setSendTime(currentDate);
        verificationCode.setCode(code);
        verificationCode = verificationCodeRepository.save(verificationCode);

        doSend(project, verificationCode);
    }

    protected abstract void doSend(VerificationProject project, VerificationCode code) throws InterrelatedException;

    @Transactional
    public boolean verifyCode(String mobile, VerificationProject project, String code, Date currentDate, VerificationType type) throws IllegalArgumentException {
        if (!SysRegex.IsValidMobileNo(mobile)) {
            throw new IllegalArgumentException("号码不对");
        }
        List<VerificationCode> codeList = verificationCodeRepository.findByMobileAndTypeAndSendTimeGreaterThan(mobile, type, new Date(currentDate.getTime() - gapSeconds * 1000));
        for (VerificationCode verificationCode : codeList) {
            if (verificationCode.getCode().equals(code))
                return true;
        }
        return false;
    }

    public int getGapSeconds() {
        return gapSeconds;
    }

    public void setGapSeconds(int gapSeconds) {
        this.gapSeconds = gapSeconds;
    }
}
