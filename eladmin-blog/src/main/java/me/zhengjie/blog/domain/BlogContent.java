package me.zhengjie.blog.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @author wangweiying
* @date 2019-10-31
*/
@Entity
@Data
@Table(name="blog_content")
public class BlogContent implements Serializable {

    // 主键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    // 审核
    @Column(name = "ENABLE_STATUS")
    private String enableStatus;

    @Column(name = "AUTHOR")
    private String author;

    @Column(name = "LABEL")
    private String label;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CONTENT_INFO")
    private String contentInfo;

    @Column(name = "CONTENT_INFO_HTML")
    private String contentInfoHtml;

    @Column(name = "SORT_NUM")
    private Integer sortNum;
    // 创建时间
    @Column(name = "CREATE_TIME")
    private Timestamp createTime;

    // 创建人
    @Column(name = "CREATE_BY")
    private String createBy;

    // 修改时间
    @Column(name = "MODIFY_TIME")
    private Timestamp modifyTime;

    // 修改人名称
    @Column(name = "MODIFY_BY")
    private String modifyBy;

    // 删除标志 F:未删除 T:已删除
    @Column(name = "DELETED")
    private String deleted;

    public void copy(BlogContent source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}