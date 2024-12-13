package com.ruoyi.common.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.enums.SpecialCharacter;
import com.ruoyi.framework.web.domain.TreeNode;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 树状结构生成工具类
 *
 * @author zhaowang
 * @since 2024/8/20 11:43
 */
public class TreeUtil {
    private TreeUtil() {
    }

    @SneakyThrows
    public static <K extends Serializable, T extends TreeNode<K, T>> List<T> toObjTree(Collection<T> list) {
        // 创建结果列表
        List<T> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(list)) {
            return result;
        }

        // 使用 HashMap 预先初始化所有可能的父节点，并创建空的子节点列表
        Map<Serializable, T> map = new HashMap<>();
        for (T node : list) {
            map.put(node.getId(), node);
            // 预先初始化
            node.setChildren(new ArrayList<>());
        }

        // 构建树形结构
        for (T node : list) {
            T parent = map.get(node.getParentId());
            if (parent != null) {
                // 直接添加到父节点的子节点列表中
                parent.getChildren().add(node);
                node.setParent(parent);
            } else {
                // 没有父节点的是根节点
                result.add(node);
            }
            String ancestors = node.getAncestors();
            if (StringUtils.isNotBlank(ancestors)) {
                List<T> ancestorList = Lists.newArrayList();
                String[] ids = ancestors.split(SpecialCharacter.COMMA.getValue().toString());
                K k = node.getId();
                Method method = k.getClass().getMethod("valueOf", String.class);
                for (String id : ids) {
                    if (!"0".equals(id)) {
                        ancestorList.add(map.get(method.invoke(k, id)));
                    }
                }
                node.setAncestor(ancestorList);
            }
        }
        return result;
    }

    public static <K extends Serializable, T extends TreeNode<K, T>> JSONArray toTree(Collection<T> list) {
        return toTree(list, "id", "pid", "children");
    }

    public static <K extends Serializable, T extends TreeNode<K, T>> JSONArray toTree(
            Collection<T> list, String idKey, String pidKey, String childrenKey) {
        return listToTree(JSON.parseArray(JSON.toJSONString(list)), idKey, pidKey, childrenKey);
    }

    public static <PlainArea> JSONArray toTreeArea(Collection<PlainArea> list) {
        return listToTree(JSON.parseArray(JSON.toJSONString(list)), "id", "parentId", "children");
    }

    /**
     * listToTree
     * <p>方法说明<p>
     * 将JSONArray数组转为树状结构
     *
     * @param arr      需要转化的数据
     * @param idKey    数据唯一的标识键值
     * @param pidKey   父id唯一标识键值
     * @param childKey 子节点键值
     * @return JSONArray
     */
    public static JSONArray listToTree(JSONArray arr, String idKey, String pidKey, String childKey) {
        JSONArray r = new JSONArray();
        JSONObject hash = new JSONObject();
        // 将数组转为Object的形式，key为数组中的id
        for (int i = 0; i < arr.size(); i++) {
            JSONObject json = arr.getJSONObject(i);
            hash.put(json.getString(idKey), json);
        }
        //遍历结果集
        for (int j = 0; j < arr.size(); j++) {
            // 单条记录
            JSONObject aVal = arr.getJSONObject(j);
            // 在hash中取出key为单条记录中pid的值
            JSONObject hashVp = hash.getJSONObject(aVal.getString(pidKey));
            // 如果记录的pid存在，则说明它有父节点，将她添加到孩子节点的集合中
            if (hashVp != null) {
                JSONArray ch = hashVp.getJSONArray(childKey);
                // 检查是否有child属性
                if (ch == null) {
                    ch = new JSONArray();
                }
                ch.add(aVal);
                hashVp.put(childKey, ch);
            } else {
                r.add(aVal);
            }
        }
        return r;
    }
}
