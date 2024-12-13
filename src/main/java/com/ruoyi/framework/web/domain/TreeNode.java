package com.ruoyi.framework.web.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * 树节点 泛型接口
 *
 * @param <K> 通常用来表示节点的标识符（ID）的类型。例如，如果树中的每个节点都有一个唯一的整数ID，那么 `K` 可能是 `Integer` 类型。
 * @param <T> 通常用来表示节点的子节点类型。在一个典型的树结构中，子节点也是树节点的类型，因此 `T` 通常与 `TreeNode<K, T>` 本身类型相同，但也可以是任何其他类型。
 * @author zhaowang
 * @since 2024/8/20 11:39
 */
public interface TreeNode<K, T extends TreeNode<K, T>> {
    /**
     * 获取父节点id
     *
     * @return 父节点id
     */
    K getParentId();

    /**
     * 获取节点id
     *
     * @return 节点id
     */
    K getId();

    /**
     * 获取祖级id列表
     *
     * @return 祖级id列表
     */
    @JsonIgnore
    String getAncestors();

    /**
     * 获取父节点对象
     *
     * @return 父节点对象
     */
    @JsonIgnore
    T getParent();

    /**
     * 设置父节点对象
     *
     * @param parent 父节点对象
     */
    void setParent(T parent);

    /**
     * 获取祖孙顺序节点列表
     *
     * @return 祖孙顺序节点列表
     */
    @JsonIgnore
    List<T> getAncestor();

    /**
     * 设置祖孙顺序节点列表
     *
     * @param ancestor 祖孙顺序节点列表
     */
    void setAncestor(List<T> ancestor);

    /**
     * 获取节点子节点列表
     *
     * @return 子节点列表
     */
    List<T> getChildren();

    /**
     * 设置节点子节点列表
     *
     * @param list 子节点列表
     */
    void setChildren(List<T> list);
}
