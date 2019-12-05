package me.zhengjie.blog.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.io.Serializable;


/**
* @author wangweiying
* @date 2019-10-31
*/
@Data
public class BlogContentDTO implements Serializable {

    // 主键
    private Integer id;

    // 审核
    private String enableStatus;

    private String author;

    private String label;

    private String title;

    private String contentInfo;
    private String contentInfoHtml;
    private Integer sortNum;
    // 创建时间
    private Timestamp createTime;

    // 创建人
    private String createBy;

    // 修改时间
    private Timestamp modifyTime;

    // 修改人名称
    private String modifyBy;

    // 删除标志 F:未删除 T:已删除
    private String deleted;
}