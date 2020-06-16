package com.baizhi.lht.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoPo {

    private String vId;    //视频数据
    private String vTitle;
    private String vBrief;
    private String vCover;
    private String vPath;
    private String vPublishDate;
    private String uHeadImg;   //用户头像
    private String cCateName; //类别名

}
