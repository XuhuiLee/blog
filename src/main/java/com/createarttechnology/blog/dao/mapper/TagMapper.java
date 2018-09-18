package com.createarttechnology.blog.dao.mapper;

import com.createarttechnology.blog.dao.entity.TagEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by lixuhui on 2018/9/14.
 */
@Repository
public interface TagMapper {

    @Select("SELECT * FROM tag WHERE id = #{id}")
    TagEntity getTag(@Param("id") int id) throws Exception;

    @Select("SELeCT * FROM tag WHERE id IN (#{ids})")
    List<TagEntity> getTagList(@Param("ids") String ids) throws Exception;

    @Select("SELeCT * FROM tag")
    List<TagEntity> getAllTagList() throws Exception;

    @Insert("INSERT INTO tag(parent_id, name) VALUES (#{tag.parentId}, #{tag.name})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "tag.id")
    void saveTag(@Param("tag") TagEntity tag) throws Exception;

    @Update("UPDATE tag SET parent_id = #{tag.parentId}, name = #{tag.name}")
    void updateTag(@Param("id") int id, @Param("tag") TagEntity tag) throws Exception;

}
