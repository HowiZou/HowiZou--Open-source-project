package com.leyou.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ExceptionEnum {
    BRAND_NOT_FOUND(400,"品牌不存在"),
    CATEGORY_NOT_FOND(404,"商品分类没查到"),
    BRAND_SAVE_ERROR(500,"新增品牌失败"),
    UPLOAD_FILE_ERROR(500,"文件上传失败"),
    INVALID_FILE_TYPE(400,"无效的文件"),
    SPEC_GROUP_NOT_FOND(404,"商品规格组不存在"),
    SPEC_PARAM_NOT_FOND(404,"商品规格组不存在"),
    GOODS_SAVE_ERROR(500,"新增商品失败")
        ;


    private int code;
    private String msg;



}
