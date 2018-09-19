package com.createarttechnology.blog.dao.mapper;

import com.createarttechnology.blog.dao.entity.ArticleEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by lixuhui on 2018/9/13.
 */
@Repository
public interface ArticleMapper {

    @Select("SELECT * FROM article WHERE id = #{id}")
    ArticleEntity getArticle(@Param("id") long id) throws Exception;

    @Select("SELECT * FROM article WHERE tag = #{tag} ORDER BY create_time")
    List<ArticleEntity> getArticleList(@Param("tag") int tagId) throws Exception;

    @Insert("INSERT INTO article(title, simple_content, rich_content, tag, pics, create_time, update_time) VALUES " +
            "(#{article.title}, #{article.simpleContent}, #{article.richContent}, #{article.tag}, #{article.pics}, UNIX_TIMESTAMP(), UNIX_TIMESTAMP())")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "article.id")
    void saveArticle(@Param("article") ArticleEntity article) throws Exception;

    @Update("UPDATE article SET title = #{article.title}, simple_content = #{article.simpleContent}, rich_content = #{article.richContent}, tag = #{article.tag}, pics = #{article.pics}, update_time = UNIX_TIMESTAMP() WHERE id = #{id}")
    void updateArticle(@Param("id") long id, @Param("article") ArticleEntity article) throws Exception;

}
