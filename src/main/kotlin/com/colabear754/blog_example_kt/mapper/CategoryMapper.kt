package com.colabear754.blog_example_kt.mapper

import com.colabear754.blog_example_kt.domain.CategoryDTO
import org.apache.ibatis.annotations.Mapper

@Mapper
interface CategoryMapper {
    fun getCategories(): List<CategoryDTO>
    fun getCategory(category_id: Int): CategoryDTO
    fun addCategory(category: CategoryDTO): Int
    fun updateCategory(params: Map<String, Any?>): Int
    fun deleteCategory(category_id: Int): Int
}