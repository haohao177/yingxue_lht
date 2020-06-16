package com.baizhi.lht.serviceImpl;


import com.baizhi.lht.annotation.AddCache;
import com.baizhi.lht.annotation.AddLog;
import com.baizhi.lht.annotation.DelCache;
import com.baizhi.lht.dao.VideoMapper;
import com.baizhi.lht.entity.Video;
import com.baizhi.lht.entity.VideoExample;
import com.baizhi.lht.repository.VideoRepository;
import com.baizhi.lht.service.VideoService;
import com.baizhi.lht.util.AliyunOSSUtil;
import com.baizhi.lht.vo.VideoVo;
import org.apache.ibatis.session.RowBounds;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class VideoServiceImpl implements VideoService {

    //依赖注入
    @Resource
    VideoMapper videoMapper;


    @Resource
    ElasticsearchTemplate elasticsearchTemplate;

    @Resource
    VideoRepository videoRepository;

    //查询展示数据
    @AddCache
    @Override
    public HashMap<String, Object> showVideoPage(Integer page, Integer rows) {
        HashMap<String, Object> map = new HashMap<String, Object>();

        //存储当前页
        map.put("page", page);

        //计算总条数
        VideoExample example = new VideoExample();
        int i = videoMapper.selectCountByExample(example);
        //存储总条数
        map.put("records", i);

        //计算总页数
        Integer total = i % rows == 0 ? i / rows : i / rows + 1;
        //存储总页数
        map.put("total", total);

        //查询展示数据

        RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
        List<Video> videos = videoMapper.selectByExampleAndRowBounds(example, rowBounds);
        map.put("rows", videos);
        return map;
    }


    //添加视频
    @DelCache
    @AddLog(description = "添加视频")
    @Override
    public String add(Video video) {
        //插入id
        video.setId(UUID.randomUUID().toString());
        //插入时间
        video.setPublishDate(new Date());
        //执行添加
        videoMapper.insertSelective(video);

        //返回id
        return video.getId();
    }


    //修改
    @DelCache
    @AddLog(description = "修改视频")
    @Override
    public void update(Video video) {
        //通过id获取元对象数据
        VideoExample videoExample = new VideoExample();
        videoExample.createCriteria().andIdEqualTo(video.getId());
        Video video1 = videoMapper.selectOneByExample(videoExample);
        System.out.println(video1);
        video1.setTitle(video.getTitle());
        video1.setBrief(video.getBrief());
        System.out.println(video1);

        videoMapper.updateByPrimaryKey(video1);
        videoRepository.save(video1);
    }


    //删除视频
    @DelCache
    @AddLog(description = "删除视频")
    @Override
    public HashMap<String, Object> delete(Video video) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        System.out.println("删除视频启动");
        try {
            //通过id获取video对象
            VideoExample example = new VideoExample();
            example.createCriteria().andIdEqualTo(video.getId());
            Video video1 = videoMapper.selectOneByExample(example);
            System.out.println(video1);
            System.out.println(!video1.getPath().equals(""));
            if (!video1.getPath().equals("")) {
                //分割视频地址，获取视频名字
                String videoName = video1.getPath().replace("https://yingxue-lht.oss-cn-beijing.aliyuncs.com/", "");
                String coverName = video1.getCover().replace("https://yingxue-lht.oss-cn-beijing.aliyuncs.com/", "");
                //删除视频
                //第一个参数是存储空间名
                //第二个参数是删除文件名
                com.baizhi.lht.util.AliyunOSSUtil.deleteFile("yingxue-lht", videoName);
                System.out.println("--" + videoName);
                //删除封面
                //第一个参数是存储空间名
                //第二个参数是删除文件名
                com.baizhi.lht.util.AliyunOSSUtil.deleteFile("yingxue-lht", coverName);
                videoMapper.deleteByExample(example);
                map.put("status", "100");
                map.put("message", "删除成功");

            } else {
                //删除数据库数据
                videoMapper.deleteByExample(example);
                map.put("status", "100");
                map.put("message", "删除成功");
            }
            videoRepository.delete(video1);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", "104");
            map.put("message", "删除失败");
        }

        return map;
    }

    //修改视频文件路径
    @DelCache
    @Override
    public void uploadVideo(MultipartFile headImg, String id, HttpServletRequest request) {

        /*1.向阿里云上传视频文件
         * 参数：
         *    headImg：上传MultipartFile类型的文件
         *   bucketName:存储空间名
         *   fileName:指定上传至阿里云的文件名
         * */
        //获取文件名   2020宣传视频.MP4
        String filename = headImg.getOriginalFilename();
        //给文件名加上事件戳       1590646959346-2020宣传视频.mp4
        String newName = new Date().getTime() + "-" + filename;
        //拼接存储空间文件夹     video/1590646959346-2020宣传视频.mp4
        String newNames = "video/" + newName;

        //拼接网络路径
        String netPath = "https://yingxue-lht.oss-cn-beijing.aliyuncs.com/" + newNames;

        //调用上传方法上传文件
        AliyunOSSUtil.uploadBytes(headImg, "yingxue-lht", newNames);

//        /**2.截取视频封面
//         * 获取指定视频的帧并保存为图片至指定目录
//         * @param videofile  源视频文件路径
//         * @param framefile  截取帧的图片存放路径   上传至阿里云
//         *                   C:\Users\NANAN\Desktop\other\video\2020宣传视频.jpg
//         * @throws Exception
//         */

        String pathCover = AliyunOSSUtil.videoInterceptImage("yingxue-lht", newNames);

        //截取名字
        String[] names = newName.split("\\.");
        //获取截取出的名字
        String interceptName = names[0];  //1590646959346-2020宣传视频
        //        //拼接图片名   1590649233064-火花.jpg
        String coverName = interceptName + ".jpg";

        try {
            AliyunOSSUtil.uploadNet("yingxue-lht", "cover/" + coverName, pathCover);
        } catch (IOException e) {
            e.printStackTrace();
        }


        //5.修改视频信息
        //修改的条件
        VideoExample example = new VideoExample();
        example.createCriteria().andIdEqualTo(id);

        Video video = new Video();

        video.setCover("https://yingxue-lht.oss-cn-beijing.aliyuncs.com/cover/" + coverName); //设置封面
        video.setPath(netPath); //设置视频地址

        //修改
        videoMapper.updateByExampleSelective(video, example);
        video.setId(id);
        Video video1 = videoMapper.selectOne(video);
        videoRepository.save(video1);

    }

    //查询app视频展示数据
    @AddCache
    @Override
    public List<VideoVo> queryByReleaseTime() {
        //查询所有视频对象
        List<VideoVo> videoVoList = videoMapper.queryByReleaseTimes();
        for (VideoVo videoVo : videoVoList) {
            //获取视频id
            String videoId = videoVo.getId();
            //根据视频id查询视频点赞数
            Integer likeCount = 18;
            //将视频点赞数放入对象
            videoVo.setLikeCount(likeCount);
        }
        return videoVoList;
    }


    @AddCache
    public List<Video> quryby() {

        List<Video> quryby = videoMapper.quryby();
        return quryby;

    }

    //搜索
    @AddCache
    @Override
    public List<VideoVo> queryByLikeVideoName(String content) {
        //查询所有视频对象
        List<VideoVo> videoVoList = videoMapper.queryByLikeVideoName(content);
        for (VideoVo videoVo : videoVoList) {
            //获取视频id
            String videoId = videoVo.getId();
            //根据视频id查询视频点赞数
            Integer likeCount = 18;
            //将视频点赞数放入对象
            videoVo.setLikeCount(likeCount);
        }
        return videoVoList;
    }

    //视频详情页
    @AddCache
    @Override
    public VideoVo queryByVideoDetail(String videoId, String cateId, String userId) {
        return null;
    }

    @AddCache
    @Override
    public HashMap<String, Object> queryCateVideoList(String cateId) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            List<VideoVo> videoVoList = videoMapper.queryCateVideoList(cateId);
            for (VideoVo videoVo : videoVoList) {
                //获取视频id
                String videoId = videoVo.getId();
                //根据视频id查询视频点赞数
                Integer likeCount = 18;
                //将视频点赞数放入对象
                videoVo.setLikeCount(likeCount);
            }
            map.put("data", videoVoList);
            map.put("message", "查询成功");
            map.put("status", "100");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("data", new ArrayList<>());
            map.put("message", "查询失败");
            map.put("status", "104");
        }
        return map;
    }

    @Override
    public List<Video> querySearch(String content) {
        HighlightBuilder.Field field = new HighlightBuilder.Field("*");
        field.preTags("<span style='color:red'>");//前缀
        field.postTags("</span>"); //后缀

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIndices("yingx") //索引名
                .withTypes("video")     //类型名
                .withQuery(QueryBuilders.queryStringQuery(content).field("title").field("brief")) //指定查询条件
                .withHighlightFields(field) //处理高亮
                .build();

        //调用搜索方法 参数  searchQuery（查询参数，条件），返回的集合泛型

        AggregatedPage<Video> videos = elasticsearchTemplate.queryForPage(searchQuery, Video.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                ArrayList<Video> videos = new ArrayList<>();
                //获取查询结果

                SearchHit[] hits = searchResponse.getHits().getHits();


                for (SearchHit hit : hits) {
                    //没有高亮的结果
                    Map<String, Object> sourceAsMap = hit.getSourceAsMap();

                    //判断是否为空
                    String id = sourceAsMap.get("id") != null ? sourceAsMap.containsKey("id") ? sourceAsMap.get("id").toString() : null : null;
                    String title = sourceAsMap.get("title") != null ? sourceAsMap.containsKey("title") ? sourceAsMap.get("title").toString() : null : null;
                    String brief = sourceAsMap.get("brief") != null ? sourceAsMap.containsKey("brief") ? sourceAsMap.get("brief").toString() : null : null;
                    String path = sourceAsMap.get("path") != null ? sourceAsMap.containsKey("path") ? sourceAsMap.get("path").toString() : null : null;
                    String cover = sourceAsMap.get("cover") != null ? sourceAsMap.containsKey("cover") ? sourceAsMap.get("cover").toString() : null : null;
                    String categoryId = sourceAsMap.get("categoryId") != null ? sourceAsMap.containsKey("categoryId") ? sourceAsMap.get("categoryId").toString() : null : null;
                    String groupId = sourceAsMap.get("groupId") != null ? sourceAsMap.containsKey("groupId") ? sourceAsMap.get("groupId").toString() : null : null;
                    String userId = sourceAsMap.get("userId") != null ? sourceAsMap.containsKey("userId") ? sourceAsMap.get("userId").toString() : null : null;
                    boolean publishDate1 = sourceAsMap.containsKey("publishDate");

                    Date publishDate = null;
                    if (sourceAsMap.get("publishDate") != null) {
                        if (publishDate1) {


                            String publishDates = sourceAsMap.get("publishDate").toString();
                            System.out.println(publishDates);
                            //改变成date属性
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                publishDate = simpleDateFormat.parse(publishDates);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    System.out.println(publishDate);

                    Video video = new Video(id, title, brief, path, cover, publishDate, categoryId, groupId, userId);
                    System.out.println(video);
                    Map<String, HighlightField> highlightFields = hit.getHighlightFields();

                    //替换高亮属性
                    if (title != null) {
                        if (highlightFields.get("title") != null) {
                            String titles = highlightFields.get("title").fragments()[0].toString();
                            video.setTitle(titles);
                            System.out.println(titles);
                        }
                    }

                    if (brief != null) {
                        if (highlightFields.get("brief") != null) {
                            String briefs = highlightFields.get("brief").fragments()[0].toString();
                            video.setBrief(briefs);
                            System.out.println(briefs);
                        }
                    }

                    //将对象放入集合
                    videos.add(video);
                }

                return new AggregatedPageImpl<T>((List<T>) videos) {
                };

            }
        });
        List<Video> videoList = videos.getContent();
        return videoList;

    }
}
