package me.zhengjie.blog.repository;

import me.zhengjie.blog.domain.BlogContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author wangweiying
* @date 2019-10-31
*/
public interface BlogContentRepository extends JpaRepository<BlogContent, Integer>, JpaSpecificationExecutor<BlogContent> {
}