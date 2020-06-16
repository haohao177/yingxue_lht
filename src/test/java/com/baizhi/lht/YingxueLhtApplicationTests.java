package com.baizhi.lht;

import com.baizhi.lht.dao.CategoryMapper;
import com.baizhi.lht.dao.UserMapper;
import com.baizhi.lht.dao.VideoMapper;
import com.baizhi.lht.entity.Category;
import com.baizhi.lht.entity.Month;
import com.baizhi.lht.entity.Video;
import com.baizhi.lht.repository.VideoRepository;
import com.baizhi.lht.service.CategoryService;
import com.baizhi.lht.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class YingxueLhtApplicationTests {

    @Resource
    UserMapper userMapper;
    @Resource
    VideoMapper videoMapper;

    @Resource
    CategoryMapper categoryMapper;
    @Resource
    CategoryService categoryService;

    @Test
    public void contextLoads() {
//        User admin = userMapper.queryByUsername("admin1");
//        System.out.println(admin);
//        User user = new User();
//        user.setId("1");
//        User user1 = userMapper.selectByPrimaryKey(user);
//        System.out.println(user1);
//
//        List<VideoVo> videoVos = videoMapper.queryByReleaseTimes();
//        for (VideoVo v:videoVos
//             ) {
//            System.out.println(v);
//        }

//        List<Category> categoryPos = categoryMapper.queryAllCategory();
        HashMap<String, Object> map = categoryService.queryAllCategory();
        List<Category> date = (List<Category>) map.get("date");


        for (Category c : date
        ) {
            System.out.println(c);
        }


    }

    @Resource
    UserService userService;

    @Test
    public void easfasf() {
        HashMap<String, Object> map = userService.querydataMonth();
        List<Month> woman = (List<Month>) map.get("woman");
        List<Month> men = (List<Month>) map.get("men");

        for (Month m : men
        ) {
            System.out.println(m);
        }

        for (Month m : woman
        ) {
            System.out.println(m);
        }
    }


    @Resource
    VideoRepository videoRepository;

    @Test
    public void save1() {

        Video video = new Video("1", "小黑", "黑黑黑", "1.mp4", "1.jpg", new Date(), "1", "1", "1");
        videoRepository.save(video);
    }

    @Test
    public void S() {

        //Video video = new Video("1","小黑","黑黑黑","1.mp4","1.jpg",new Date(),"1","1","1");
        Iterable<Video> all = videoRepository.findAll();
        for (Video video : all) {
            System.out.println(video);
        }
    }

    @Test
    public void Ss() {

        List<Video> videos = videoMapper.selectAll();
        for (Video video : videos) {
            videoRepository.save(video);
        }
    }


}
