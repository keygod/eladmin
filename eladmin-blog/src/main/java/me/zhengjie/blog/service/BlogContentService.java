package me.zhengjie.blog.service;

import me.zhengjie.blog.domain.BlogContent;
import me.zhengjie.blog.service.dto.BlogContentDTO;
import me.zhengjie.blog.service.dto.BlogContentQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;

/**
* @author wangweiying
* @date 2019-10-31
*/
public interface BlogContentService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(BlogContentQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<BlogContentDTO>
    */
    List<BlogContentDTO> queryAll(BlogContentQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return BlogContentDTO
     */
    BlogContentDTO findById(Integer id);

    BlogContentDTO create(BlogContent resources);

    void update(BlogContent resources);

    void delete(Integer id);
}