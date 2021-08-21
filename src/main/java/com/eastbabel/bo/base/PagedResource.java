package com.eastbabel.bo.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PagedResource<T> {

    @ApiModelProperty("分页信息")
    private PageInfo pageInfo;

    @ApiModelProperty("数据")
    private List<T> content;


    /**
     * 手动构造分页结果
     *
     * @param content       数据结果
     * @param page          当前页
     * @param size          每页数据量
     * @param totalElements 数据总量
     */
    public PagedResource(List<T> content, int page, int size, long totalElements) {
        this.pageInfo = new PageInfo(page, size, totalElements);
        this.content = content;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    static
    class PageInfo {
        @ApiModelProperty("当前页数")
        private int page;
        @ApiModelProperty("每页数量")
        private int size;
        @ApiModelProperty("总条数")
        private long totalElements;

        @ApiModelProperty("是否还有上一页")
        public boolean isHasPrevious() {
            return page > 1;
        }

        @ApiModelProperty("是否还有下一页")
        public boolean isHasNext() {
            return page < getTotalPages();
        }

        @ApiModelProperty("当前页数")
        public int getTotalPages() {
            return Double.valueOf(Math.ceil(totalElements * 1.0 / size)).intValue();
        }
    }
}
