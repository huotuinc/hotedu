package com.huotu.hotedu.service;

import com.huotu.hotedu.entity.Result;
import com.huotu.hotedu.exception.InterrelatedException;
import com.huotu.hotedu.model.CodeType;
import com.huotu.hotedu.model.VerificationType;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 通用验证码服务
 *
 * @author CJ
 */
public interface VerificationService {

    /**
     * 可以使用验证码的项目
     */
    enum VerificationProject{

        //【粉猫】您的验证码是{1},为了保护您的账户安全,验证短信请勿转发他人。工作人员不会向你索取验证码。
        fanmore("25081","(粉猫验证码:%s)为了保护您的账户安全，验证短信请勿转发他人。工作人员不会向你索取验证码。"),
        hotedu("25082","(云商学院验证码:%s)为了保护您的账户安全，验证短信请勿转发他人。工作人员不会向你索取验证码。"),
        baomin("25083","温馨提示：您已经报名成功，请近期携带相关报名材料到公司缴费确认。咨询电话：0571-56532880");


        private final String format;
        private final String templateId;

        public String getTemplateId() {
            return templateId;
        }

        public String getFormat() {
            return format;
        }

        VerificationProject(String templateId,String format){
            this.templateId = templateId;
            this.format = format;
        }
    }

    /**
     * 是否支持语音播报
     *
     * @return
     */
    boolean supportVoice();

    /**
     * 发送验证码
     *
     * @param mobile      接受者手机号码
     * @param project     项目信息，比如短信包含内容什么的
     * @param code        验证码
     * @param currentDate 当前日期
     * @param type        验证码类型
     * @param sentType    发送类型
     * @throws IllegalStateException    90秒内已发过
     * @throws IllegalArgumentException 手机号码或者其他参数有误
     * @throws NoSuchMethodException    该发送类别不受支持
     * @throws InterrelatedException    第三方平台错误
     */
    @Transactional
    void sendCode(String mobile, VerificationProject project, String code, Date currentDate, VerificationType type, CodeType sentType)
            throws IllegalStateException, IllegalArgumentException, NoSuchMethodException, InterrelatedException, InterrelatedException;

    /**
     * 验证该验证码
     * @return true表示验证通过
     * @throws IllegalArgumentException 手机号码或者其他参数有误
     */
    @Transactional
    Result verifyCode(String phoneNo,String code,VerificationType type) throws IllegalArgumentException;

}
