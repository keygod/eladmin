package me.zhengjie.blog.service.impl;

import me.zhengjie.blog.domain.BlogContent;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.blog.repository.BlogContentRepository;
import me.zhengjie.blog.service.BlogContentService;
import me.zhengjie.blog.service.dto.BlogContentDTO;
import me.zhengjie.blog.service.dto.BlogContentQueryCriteria;
import me.zhengjie.blog.service.mapper.BlogContentMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import java.util.List;
import java.util.Map;

/**
* @author wangweiying
* @date 2019-10-31
*/
@Service
@CacheConfig(cacheNames = "blogContent")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class BlogContentServiceImpl implements BlogContentService {

    private final BlogContentRepository blogContentRepository;

    private final BlogContentMapper blogContentMapper;

    public BlogContentServiceImpl(BlogContentRepository blogContentRepository, BlogContentMapper blogContentMapper) {
        this.blogContentRepository = blogContentRepository;
        this.blogContentMapper = blogContentMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(BlogContentQueryCriteria criteria, Pageable pageable){
        Page<BlogContent> page = blogContentRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(blogContentMapper::toDto));
    }

    @Override
    @Cacheable
    public List<BlogContentDTO> queryAll(BlogContentQueryCriteria criteria){
        return blogContentMapper.toDto(blogContentRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public BlogContentDTO findById(Integer id) {
        BlogContent blogContent = blogContentRepository.findById(id).orElseGet(BlogContent::new);
        ValidationUtil.isNull(blogContent.getId(),"BlogContent","id",id);
        return blogContentMapper.toDto(blogContent);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public BlogContentDTO create(BlogContent resources) {
        return blogContentMapper.toDto(blogContentRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(BlogContent resources) {
        BlogContent blogContent = blogContentRepository.findById(resources.getId()).orElseGet(BlogContent::new);
        ValidationUtil.isNull( blogContent.getId(),"BlogContent","id",resources.getId());
        blogContent.copy(resources);
        blogContentRepository.save(blogContent);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        blogContentRepository.deleteById(id);
    }
}