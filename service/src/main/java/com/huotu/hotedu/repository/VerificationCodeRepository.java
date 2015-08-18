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
     *
     * @param code
     * @return
     */
    List<VerificationCode> findByMobileAndCodeOrderBySendTimeDesc(String phoneNo, String code);

    /**
     *
     * @param mobile
     * @param type
     * @param last 最晚的发送时间
     * @return
     */
    List<VerificationCode> findByMobileAndTypeAndCodeTypeAndSendTimeGreaterThan(String mobile, VerificationType type,CodeType codeType, Date last);
}
