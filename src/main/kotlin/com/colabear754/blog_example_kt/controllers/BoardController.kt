package com.colabear754.blog_example_kt.controllers

import com.colabear754.blog_example_kt.domain.BoardDTO
import com.colabear754.blog_example_kt.domain.DeleteDTO
import com.colabear754.blog_example_kt.domain.LikeDTO
import com.colabear754.blog_example_kt.mapper.BoardMapper
import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.apache.commons.io.FilenameUtils
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import javax.servlet.http.HttpServletRequest

@Api(tags = ["블로그 API"], description = "블로그 글 관련 API")
@RestController
class BoardController(val boardMapper: BoardMapper) {
    private val PAGESIZE = 6
    private val BLOCKSIZE = 10
    private val PATH = "D:\\blog\\img\\"

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

    @ApiOperation("글 추천")
    @ApiImplicitParams(
        ApiImplicitParam(name = "id", value = "추천자"),
        ApiImplicitParam(name = "seq", value = "추천할 글 번호", required = true)
    )
    @PostMapping("/document/{seq}/like")
    fun postLike(@PathVariable seq: Int, @RequestParam(required = false) id: String?, request: HttpServletRequest): LikeDTO {
        val inputParams = mutableMapOf<String, Any>()
        inputParams["id"] = id ?: request.remoteAddr
        inputParams["seq"] = seq

        return try {
            LikeDTO(seq, inputParams["id"] as String, boardMapper.like(inputParams), true)
        } catch (e: Exception) {
            LikeDTO(seq, inputParams["id"] as String, boardMapper.cancelLike(inputParams), false)
        }
    }

    @ApiOperation("글 작성")
    @ApiImplicitParams(
        ApiImplicitParam(name = "subject", value = "글 제목", required = true),
        ApiImplicitParam(name = "content", value = "글 내용", required = true),
        ApiImplicitParam(name = "category_id", value = "글을 분류할 카테고리의 일련번호")
    )
    @PostMapping(value = ["/write"])
    fun write(
        @RequestParam subject: String,
        @RequestParam content: String,
        @RequestParam(required = false) category_id: Int?,
        @ApiParam("업로드 할 썸네일") @RequestPart(required = false) uploadFile: MultipartFile?
    ): BoardDTO? {
        Files.createDirectories(Paths.get(PATH))
        var filename: String? = null

        if (uploadFile?.isEmpty == false) {
            val extension = FilenameUtils.getExtension(uploadFile.originalFilename)
            val timemillis = System.currentTimeMillis()
            val random = IntRange(10000, 99999).random()
            filename = "${random}${timemillis}.$extension"

            uploadFile.transferTo(File("${PATH}${filename}"))
        }
        val document = BoardDTO(subject, content, category_id ?: 0, filename)
        boardMapper.write(document)

        return boardMapper.getDocument(document.seq ?: 0)
    }

    @ApiOperation("글 수정")
    @ApiImplicitParams(
        ApiImplicitParam(name = "seq", value = "수정할 글 번호", required = true),
        ApiImplicitParam(name = "subject", value = "글 제목", required = true),
        ApiImplicitParam(name = "content", value = "글 내용", required = true),
        ApiImplicitParam(name = "category_id", value = "글을 분류할 카테고리의 일련번호")
    )
    @PutMapping("/update/{seq}")
    fun update(
        @PathVariable seq: Int,
        @RequestParam subject: String,
        @RequestParam content: String,
        @RequestParam(required = false) category_id: Int?,
        @ApiParam("업로드 할 썸네일") @RequestPart(required = false) uploadFile: MultipartFile?
    ): BoardDTO? {
        val document = boardMapper.getDocument(seq)
        Files.createDirectories(Paths.get(PATH))
        val filename: String

        if (document == null) {
            return BoardDTO("존재하지 않는 게시글입니다.")
        }

        if (uploadFile?.isEmpty == false) {
            val preThumbnail = File("${PATH}${document.thumbnail}")
            val extension = FilenameUtils.getExtension(uploadFile.originalFilename)
            val timemillis = System.currentTimeMillis()
            val random = IntRange(10000, 99999).random()
            filename = "${random}${timemillis}.$extension"

            uploadFile.transferTo(File("${PATH}${filename}"))
            preThumbnail.delete()
            document.thumbnail = filename
        }

        document.subject = subject
        document.content = content
        document.category_id = category_id ?: 0
        boardMapper.update(document)

        return boardMapper.getDocument(seq)
    }

    @ApiOperation("글 삭제")
    @ApiImplicitParam(name = "seq", value = "삭제할 글 번호", required = true)
    @DeleteMapping("/document/delete/{seq}")
    fun delete(@PathVariable seq: Int): DeleteDTO {
        val thumbnail = boardMapper.getDocument(seq)?.thumbnail
        if (thumbnail?.isBlank() == false) {
            File("${PATH}${thumbnail}").delete()
        }

        return DeleteDTO(seq, boardMapper.delete(seq))
    }
}