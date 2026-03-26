package com.bookstore.dao;

import java.util.List;
import java.util.Map;

class CartSqlProvider {
    // 批量更新选中状态
    public String batchUpdateSelected(Map<String, Object> params) {
        List<Integer> ids = (List<Integer>) params.get("ids");
        Boolean selected = (Boolean) params.get("selected");

        if (ids == null || ids.isEmpty()) {
            return "";
        }

        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE bookstore.cart_item SET selected = ");
        sql.append(selected ? "1" : "0");
        sql.append(" WHERE id IN (");

        for (int i = 0; i < ids.size(); i++) {
            sql.append(ids.get(i));
            if (i < ids.size() - 1) {
                sql.append(", ");
            }
        }

        sql.append(")");
        return sql.toString();
    }

    // 批量删除
    public String batchDelete(Map<String, Object> params) {
        List<Integer> ids = (List<Integer>) params.get("ids");

        if (ids == null || ids.isEmpty()) {
            return "";
        }

        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM bookstore.cart_item WHERE id IN (");

        for (int i = 0; i < ids.size(); i++) {
            sql.append(ids.get(i));
            if (i < ids.size() - 1) {
                sql.append(", ");
            }
        }

        sql.append(")");
        return sql.toString();
    }

}
