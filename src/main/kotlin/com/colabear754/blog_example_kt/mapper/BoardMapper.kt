package com.colabear754.blog_example_kt.mapper

import com.colabear754.blog_example_kt.domain.BoardDTO
import org.apache.ibatis.annotations.Mapper

@Mapper
interface BoardMapper {
    fun getDocuments(params: Map<String, Any?>): List<BoardDTO>
    fun getDocument(seq: Int): BoardDTO?
    fun getDocumentsCount(category_id: Int): Int
    fun write(document: BoardDTO)
    fun update(document: BoardDTO): Int
    fun delete(document: BoardDTO): Int
    fun isLike(params: Map<String, Any?>): Boolean
    fun like(params: Map<String, Any?>): Int
    fun cancelLike(params: Map<String, Any?>): Int
    fun increaseViewCnt(seq: Int)
}