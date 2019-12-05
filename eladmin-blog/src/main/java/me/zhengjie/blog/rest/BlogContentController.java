package me.zhengjie.blog.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.blog.domain.BlogContent;
import me.zhengjie.blog.params.EnumsStatus;
import me.zhengjie.blog.service.BlogContentService;
import me.zhengjie.blog.service.dto.BlogContentDTO;
import me.zhengjie.blog.service.dto.BlogContentQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

/**
* @author wangweiying
* @date 2019-10-31
*/
@Api(tags = "BlogContent管理")
@RestController
@RequestMapping("/api/blogContent")
public class BlogContentController {

    private final BlogContentService blogContentService;

    public BlogContentController(BlogContentService blogContentService) {
        this.blogContentService = blogContentService;
    }

    @GetMapping
    @Log("查询BlogContent")
    @ApiOperation("查询BlogContent")
    @PreAuthorize("@el.check('blogContent:list')")
    public ResponseEntity getBlogContents(BlogContentQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(blogContentService.queryAll(criteria,pageable),HttpStatus.OK);
    }



    @GetMapping("/enable/{id}")
    @Log("上下架")
    @ApiOperation("上下架")
    @PreAuthorize("@el.check('blogContent:enable')")
    public ResponseEntity enable(@PathVariable Integer id){
        BlogContentDTO blog = blogContentService.findById(id);
        blog.setEnableStatus(Objects.equals(blog.getEnableStatus(),EnumsStatus.ENABLE_STATUS_TRUE.getKey())?EnumsStatus.ENABLE_STATUS_FALSE.getKey():EnumsStatus.ENABLE_STATUS_TRUE.getKey());
        BlogContent blogContent = new BlogContent();
        blogContent.setEnableStatus(blog.getEnableStatus());
        blogContent.setId(blog.getId());
        blogContentService.update(blogContent);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PostMapping
    @Log("新增BlogContent")
    @ApiOperation("新增BlogContent")
    @PreAuthorize("@el.check('blogContent:add')")
    public ResponseEntity create(@Validated @RequestBody BlogContent resources){
        resources.setCreateTime(new Timestamp(System.currentTimeMillis()));
        resources.setCreateBy(SecurityContextHolder.getContext().getAuthentication().getName());
        resources.setModifyTime(new Timestamp(System.currentTimeMillis()));
        resources.setModifyBy(SecurityContextHolder.getContext().getAuthentication().getName());
        resources.setAuthor(SecurityContextHolder.getContext().getAuthentication().getName());
        resources.setEnableStatus(EnumsStatus.ENABLE_STATUS_FALSE.getKey());
        return new ResponseEntity<>(blogContentService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改BlogContent")
    @ApiOperation("修改BlogContent")
    @PreAuthorize("@el.check('blogContent:edit')")
    public ResponseEntity update(@Validated @RequestBody BlogContent resources){
        resources.setModifyTime(new Timestamp(System.currentTimeMillis()));
        resources.setModifyBy(SecurityContextHolder.getContext().getAuthentication().getName());
        blogContentService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除BlogContent")
    @ApiOperation("删除BlogContent")
    @PreAuthorize("@el.check('blogContent:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        blogContentService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }




}