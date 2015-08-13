package com.huotu.hotedu.test.web;

import com.huotu.hotedu.WebTestBase;
import com.huotu.hotedu.entity.Editor;
import com.huotu.hotedu.service.LoginService;
import com.huotu.iqiyi.sdk.IqiyiVideoRepository;
import com.huotu.iqiyi.sdk.model.Video;
import com.huotu.iqiyi.sdk.model.VideoForPlay;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;




/**
 * Created by Administrator on 2015/7/31.
 */
public class VideoTest extends WebTestBase {

    @Autowired
    private LoginService loginService;
    @Autowired
    IqiyiVideoRepository iqiyiVideoRepository;

    @Test
    public void printAll() throws IOException {
        for(Video video:iqiyiVideoRepository.findAll()){
            VideoForPlay play = iqiyiVideoRepository.play(video.getFileId());
            System.out.println(play);
        }
    }

    @Test
    public void page() throws Exception {
        String password = UUID.randomUUID().toString();

        String editorUsername = UUID.randomUUID().toString();
        Editor editor = new Editor();
        editor.setLoginName(editorUsername);
        loginService.newLogin(editor, password);
        mockMvc.perform(
                get("/backend/loadVideo")
                .session(loginAs(editorUsername, password))
        )
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }



    @Test
    @Ignore
    public void add() throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        StreamUtils.copy(getClass().getResourceAsStream("testUpload.jpg"), buffer);
        String password = UUID.randomUUID().toString();
        String editorUsername = UUID.randomUUID().toString();
        Editor editor = new Editor();
        editor.setLoginName(editorUsername);
        loginService.newLogin(editor, password);
        mockMvc.perform(
                fileUpload("/backend/addSaveVideo")
                        .file("videoFile", buffer.toByteArray())
                        .session(loginAs(editorUsername, password))
                        .param("videoName", "测试1")
                .param("tags", "搞笑,励志")
                .param("description", "这是一个很棒的视频")
        )
                .andDo(print())
                .andExpect(status().isFound())
        ;
    }
}
