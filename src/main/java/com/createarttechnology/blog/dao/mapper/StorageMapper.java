package com.createarttechnology.blog.dao.mapper;

import com.createarttechnology.blog.bean.Article;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by lixuhui on 2018/9/13.
 */
@Repository
public interface StorageMapper {

    @Select("SELECT * FROM article WHERE id = #{id}")
    Article getArticle(@Param("id") long id) throws Exception;

    @Select("SELECT * FROM article ORDER BY create_time")
    List<Article> getArticleList() throws Exception;

    @Insert("INSERT INTO article(author_id, title, simple_content, rich_content, tags, pics, videos) VALUES " +
            "(#{article.authorId}, #{article.title}, #{article.simpleContent}, #{article.richContent}, #{article.tags}, #{article.pics}, #{article.videos})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "article.id")
    void saveArticle(@Param("article") Article article) throws Exception;

    @Update("UPDATE article SET title = #{article.title}, simple_content = #{article.simpleContent}, rich_content = #{article.richContent}, tags = #{article.tags}, pics = #{article.pics}, videos = #{article.videos} WHERE id = #{id}")
    void updateArticle(@Param("id") long id, @Param("article") Article article) throws Exception;

}
