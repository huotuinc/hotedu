package com.huotu.hotedu.test.web;

import com.huotu.hotedu.WebTestBase;
import com.huotu.iqiyi.sdk.IqiyiVideoRepository;
import com.huotu.iqiyi.sdk.model.Video;
import com.huotu.iqiyi.sdk.model.VideoForPlay;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * Created by Administrator on 2015/7/30.
 */
public class VideoTest extends WebTestBase{

    @Autowired
    IqiyiVideoRepository iqiyiVideoRepository;

    @Test
    public void printAll() throws IOException {
        for(Video video:iqiyiVideoRepository.findAll()){
            VideoForPlay play = iqiyiVideoRepository.play(video.getFileId());
            System.out.println(play);
        }
    }
}
