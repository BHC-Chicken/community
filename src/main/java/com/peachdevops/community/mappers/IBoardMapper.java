package com.peachdevops.community.mappers;

import com.peachdevops.community.dto.article.ArticleResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface IBoardMapper {
    List<ArticleResponse> findAllArticles(String boardCode);
}
