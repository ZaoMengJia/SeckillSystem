package com.zaomengjia.common.vo.page;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/8 00:28
 */
@Data
public class PageVO<T> {
    private List<T> data;
    private int pageNum;
    private int pageTotal;
    private long total;

    public PageVO(Page<T> page) {
        data = page.getContent();
        pageNum = page.getNumber();
        pageTotal = page.getTotalPages();
        total = page.getTotalElements();
    }

    public PageVO(List<T> data, int pageNum, int pageTotal, long total) {
        this.data = data;
        this.pageNum = pageNum;
        this.pageTotal = pageTotal;
        this.total = total;
    }
}
