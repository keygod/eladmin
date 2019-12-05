package me.zhengjie.blog.service.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.blog.domain.BlogContent;
import me.zhengjie.blog.service.dto.BlogContentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author wangweiying
* @date 2019-10-31
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BlogContentMapper extends BaseMapper<BlogContentDTO, BlogContent> {

}