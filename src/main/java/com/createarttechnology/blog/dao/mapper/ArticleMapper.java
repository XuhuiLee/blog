package com.createarttechnology.blog.dao.mapper;

import com.createarttechnology.blog.bean.Pager;
import com.createarttechnology.blog.dao.entity.ArticleEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * Created by lixuhui on 2018/9/13.
 */
@Repository
public interface ArticleMapper {

    @Select("SELECT * FROM article WHERE id = #{id}")
    ArticleEntity getArticle(@Param("id") long id) throws Exception;

    @Select("SELECT * FROM article WHERE tag = #{tag} ORDER BY create_time")
    List<ArticleEntity> getArticleListByTagId(@Param("tag") int tagId) throws Exception;

    List<ArticleEntity> getArticleListByParentTagId(@Param("collection") Collection<Integer> tagIds) throws Exception;

    @Insert("INSERT INTO article(title, simple_content, rich_content, tag, pics, create_time, update_time, markdown) VALUES " +
            "(#{article.title}, #{article.simpleContent}, #{article.richContent}, #{article.tag}, #{article.pics}, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), #{article.markdown})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "article.id")
    void saveArticle(@Param("article") ArticleEntity article) throws Exception;

    @Update("UPDATE article SET title = #{article.title}, simple_content = #{article.simpleContent}, rich_content = #{article.richContent}, tag = #{article.tag}, pics = #{article.pics}, update_time = UNIX_TIMESTAMP(), markdown = #{article.markdown} WHERE id = #{id}")
    void updateArticle(@Param("id") long id, @Param("article") ArticleEntity article) throws Exception;

    @Select("SELECT * FROM article ORDER BY update_time DESC LIMIT #{limit}")
    List<ArticleEntity> getRecentEditArticleList(@Param("limit") int limit) throws Exception;

    @Select("SELECT * FROM article ORDER BY create_time DESC LIMIT #{pager.start}, #{pager.limit}")
    List<ArticleEntity> getRecentCreateArticleList(@Param("pager") Pager pager) throws Exception;

    @Select("SELECT COUNT(*) FROM article")
    int getArticleCount() throws Exception;
}
