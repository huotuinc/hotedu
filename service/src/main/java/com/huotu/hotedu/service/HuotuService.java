package com.huotu.hotedu.service;
import com.huotu.hotedu.entity.Huotu;
import com.huotu.hotedu.repository.HuotuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 *
 * @author jiashubing
 */
@Service
public class HuotuService {

    @Autowired
    private HuotuRepository huotuRepository;
    //修改公司
    public void modifyHuotu(Huotu huotu){
        huotuRepository.save(huotu);
    }
    //查找公司
    public List<Huotu> findHuotu(){
        return huotuRepository.findAll();
    }
}
