package com.ruoyi.framework.web.page;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.Page;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.framework.web.domain.R;

import java.util.List;

/**
 * 表格分页数据对象
 * 
 * @author ruoyi
 */
public class TableDataInfo<T> extends R<T>
{
    private static final long serialVersionUID = 1L;

    /** 总记录数 */
    private long total;

    /** 列表数据 */
    private List<T> rows;


    /**
     * 表格数据对象
     */
    public TableDataInfo()
    {
    }

    /**
     * 分页
     * 
     * @param list 列表数据
     * @param total 总记录数
     */
    public TableDataInfo(List<T> list, int total)
    {
        this.rows = list;
        this.total = total;
    }

    /**
     * 表格数据对象
     *
     * @param page 分页数据
     */
    @SuppressWarnings("unchecked")
    public TableDataInfo(Object page) {
        setCode(HttpStatus.SUCCESS);
        setMsg("查询成功");
        if (page instanceof IPage) {
            IPage<T> page0 = (IPage<T>) page;
            setRows(page0.getRecords());
            setTotal(page0.getTotal());
        } else if (page instanceof Page) {
            Page<T> page1 = (Page<T>) page;
            setRows(page1);
            setTotal(page1.getTotal());
        } else {
            List<T> list = (List<T>) page;
            setRows(list);
            setTotal(list.size());
        }
    }


    public long getTotal()
    {
        return total;
    }

    public void setTotal(long total)
    {
        this.total = total;
    }

    public List<T> getRows()
    {
        return rows;
    }

    public void setRows(List<T> rows)
    {
        this.rows = rows;
    }
}