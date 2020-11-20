package com.leyou.item.mapper;

import com.leyou.item.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends Mapper<Brand> {

    @Insert("INSERT INTO tb_category_brand (category_id, brand_id) VALUES (#{cid},#{bid})")
    int insertCategoryBrand(@Param("cid") Long cid, @Param("bid") Long bid);

    @Select("SELECT b.id,b.`name`,b.letter,b.image\n" +
            "FROM tb_brand b\n" +
            "INNER JOIN tb_category_brand cb ON b.id = cb.brand_id\n" +
            "WHERE cb.category_id = #{cid}\n")
    List<Brand> queryByCategoryId(@Param("cid") Long cid);

}
