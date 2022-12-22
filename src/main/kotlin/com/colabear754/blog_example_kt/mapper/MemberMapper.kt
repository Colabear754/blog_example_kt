package com.colabear754.blog_example_kt.mapper

import com.colabear754.blog_example_kt.domain.MemberDTO
import org.apache.ibatis.annotations.Mapper

@Mapper
interface MemberMapper {
    fun sign_in(member: MemberDTO): Boolean
    fun isDuplicated(id: String): Boolean
    fun sign_up(member: MemberDTO): Int
    fun updateMember(member: MemberDTO): Int
    fun withdrawal(params: Map<String, String>)
    fun getMember(id: String): MemberDTO
}