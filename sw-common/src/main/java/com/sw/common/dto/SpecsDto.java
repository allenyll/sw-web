package com.sw.common.dto;


import lombok.Data;

/**
 * 规格相关Dto
 * @ClassName: com.sw.common.dto.SpecsQto.java
 * @Description:
 * @author: 20012055 yuleilei
 * @date:  2020/12/23 15:22
 * @version V1.0
 */
@Data
public class SpecsDto extends BaseQueryDto {

    /**
     * 规格ID
     */
    private Long specsId;

    /**
     * 规格选项名称
     */
    private String specsOptionName;

}
