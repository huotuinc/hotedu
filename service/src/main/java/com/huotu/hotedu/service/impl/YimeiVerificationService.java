package com.huotu.hotedu.service.impl;

import com.huotu.hotedu.common.exception.InterrelatedException;
import com.huotu.hotedu.entity.Result;
import com.huotu.hotedu.entity.VerificationCode;
import com.huotu.hotedu.service.VerificationService;
import com.huotu.hotedu.util.SMSHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Formatter;
import java.util.Locale;

/**
 * @author CJ
 */
@Profile("!cloopenEnabled")
@Service
public class YimeiVerificationService extends AbstractVerificationService implements VerificationService {

    private static final Log log = LogFactory.getLog(YimeiVerificationService.class);
    @Autowired
    private Environment env;

    public YimeiVerificationService() {
        log.info("伊美短信平台使用中……");
    }

    @Override
    public boolean supportVoice() {
        return false;
    }

    @Override
    protected void doSend(VerificationProject project, VerificationCode code) throws InterrelatedException {
        if (env.acceptsProfiles("test") && !env.acceptsProfiles("prod"))
            return;

        String smsContent = new Formatter(Locale.CHINA).format(project.getFormat(),code.getCode()).toString();
        SMSHelper sms = new SMSHelper();
        Result resultSMS = sms.send(code.getMobile(), smsContent);
        if (resultSMS.getStatus() != 0)
            throw new InterrelatedException();
    }
}
