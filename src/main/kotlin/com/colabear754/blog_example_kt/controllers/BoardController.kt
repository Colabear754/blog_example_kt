package com.colabear754.blog_example_kt.controllers

import com.colabear754.blog_example_kt.domain.BoardDTO
import com.colabear754.blog_example_kt.mapper.BoardMapper
import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Api(tags = ["블로그 API"], description = "블로그 글 관련 API")
@RestController
class BoardController(val boardMapper: BoardMapper) {
    private val PAGESIZE = 6
    private val BLOCKSIZE = 10

    @ApiOperation("블로그 글 목록 조회")
    @ApiImplicitParams(
        ApiImplicitParam(name = "category_id", value = "글을 조회할 카테고리 번호(0은 전체 조회)", required = true),
        ApiImplicitParam(name = "pageNum", value = "글을 조회할 페이지", defaultValue = "1")
    )
    @GetMapping("/document-list")
    fun document_list(@RequestParam("category_id") id: String?, @RequestParam(required = false) pageNum: String?): List<BoardDTO> {
        val category_id = id?.toInt() ?: -1
        val inputParams = mutableMapOf<String, Any?>()
        val pageNumber = pageNum ?: "1"
        val currentPage = pageNumber.toInt()
        val start = (currentPage - 1) * PAGESIZE + 1
        val end = currentPage * PAGESIZE

        inputParams["category_id"] = category_id
        inputParams["start"] = start
        inputParams["end"] = end

        return boardMapper.getDocuments(inputParams)
    }

    @ApiOperation("블로그 글 보기")
    @ApiImplicitParam(name = "seq", value = "조회할 글 번호", required = true, paramType = "path")
    @GetMapping("/document/{seq}")
    fun document(@PathVariable seq: Int): BoardDTO {
        boardMapper.increaseViewCnt(seq)
        return boardMapper.getDocument(seq) ?: BoardDTO("존재하지 않는 게시글입니다.")
    }
}