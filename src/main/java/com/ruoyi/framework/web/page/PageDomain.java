package com.ruoyi.framework.web.page;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.utils.StringUtils;

/**
 * 分页数据
 *
 * @author ruoyi
 */
public class PageDomain
{
    private static final Long DEFAULT_PAGE_NUM = 1L;
    private static final Long DEFAULT_PAGE_SIZE = 10L;

    /** 当前记录起始索引 */
    private Integer pageNum;

    /** 每页显示记录数 */
    private Integer pageSize;

    /** 排序列 */
    private String orderByColumn;

    /** 排序的方向desc或者asc */
    private String isAsc = "asc";

    /** 分页参数合理化 */
    private Boolean reasonable = true;


    /**
     * 构造mybatis-plus的分页参数
     *
     * @param page 原始参数
     * @param <T>  泛型
     * @return mybatis-plus的分页参数
     */
    public static <T> Page<T> of(PageDomain page) {
        if (page == null) {
            return Page.of(DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE);
        }
        if (page.getPageNum() == null || page.getPageNum() < DEFAULT_PAGE_NUM) {
            page.setPageNum(DEFAULT_PAGE_NUM.intValue());
        }
        if (page.getPageSize() == null || page.getPageSize() < DEFAULT_PAGE_NUM) {
            page.setPageSize(DEFAULT_PAGE_SIZE.intValue());
        }
        return Page.of(page.getPageNum(), page.getPageSize());
    }

    public <T> Page<T> toPage() {
        return of(this);
    }

    public String getOrderBy()
    {
        if (StringUtils.isEmpty(orderByColumn))
        {
            return "";
        }
        return StringUtils.toUnderScoreCase(orderByColumn) + " " + isAsc;
    }

    public Integer getPageNum()
    {
        return pageNum;
    }

    public void setPageNum(Integer pageNum)
    {
        this.pageNum = pageNum;
    }

    public Integer getPageSize()
    {
        return pageSize;
    }

    public void setPageSize(Integer pageSize)
    {
        this.pageSize = pageSize;
    }

    public String getOrderByColumn()
    {
        return orderByColumn;
    }

    public void setOrderByColumn(String orderByColumn)
    {
        this.orderByColumn = orderByColumn;
    }

    public String getIsAsc()
    {
        return isAsc;
    }

    public void setIsAsc(String isAsc)
    {
        if (StringUtils.isNotEmpty(isAsc))
        {
            // 兼容前端排序类型
            if ("ascending".equals(isAsc))
            {
                isAsc = "asc";
            }
            else if ("descending".equals(isAsc))
            {
                isAsc = "desc";
            }
            this.isAsc = isAsc;
        }
    }

    public Boolean getReasonable()
    {
        if (StringUtils.isNull(reasonable))
        {
            return Boolean.TRUE;
        }
        return reasonable;
    }

    public void setReasonable(Boolean reasonable)
    {
        this.reasonable = reasonable;
    }
}
