package com.huotu.hotedu.repository;

import com.huotu.hotedu.entity.VerificationCode;
import com.huotu.hotedu.model.CodeType;
import com.huotu.hotedu.model.VerificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author CJ
 */
@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode,Long> {

    /**
     * 根据手机和类型返回验证码
     * @param mobile
     * @param type
     * @param codeType
     * @return
     */
    VerificationCode findByMobileAndTypeAndCodeType(String mobile, VerificationType type, CodeType codeType);

    /**
     * 根据手机和类型返回验证码
     * @param mobile
     * @param type
     * @param last 最晚许可的发送时间
     * @return
     */
    List<VerificationCode> findByMobileAndTypeAndSendTimeGreaterThan(String mobile, VerificationType type, Date last);
}
