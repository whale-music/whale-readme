package org.core.common.page;

import org.core.common.reflection.SFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LambdaQueryWrapper<T> {
    
    HashMap<SFunction<T, ?>, Object> eqData = new HashMap<>();
    HashMap<SFunction<T, ?>, Object> likeData = new HashMap<>();
    HashMap<SFunction<T, ?>, Object> inData = new HashMap<>();
    
    List<SFunction<T, ?>> orderByDescData = new ArrayList<>();
    
    List<SFunction<T, ?>> orderByAscData = new ArrayList<>();
    
    
    public LambdaQueryWrapper<T> eq(Boolean condition, SFunction<T, ?> func, Object id) {
        if (Boolean.TRUE.equals(condition)) {
            eqData.put(func, id);
        }
        return this;
    }
    
    public LambdaQueryWrapper<T> eq(SFunction<T, ?> func, Object id) {
        eqData.put(func, id);
        
        return this;
    }
    
    public LambdaQueryWrapper<T> like(Boolean condition, SFunction<T, ?> func, Object id) {
        if (Boolean.TRUE.equals(condition)) {
            likeData.put(func, id);
        }
        return this;
    }
    
    public LambdaQueryWrapper<T> like(SFunction<T, ?> func, Object id) {
        likeData.put(func, id);
        return this;
    }
    
    public LambdaQueryWrapper<T> in(Boolean condition, SFunction<T, ?> func, Object id) {
        if (Boolean.TRUE.equals(condition)) {
            inData.put(func, id);
        }
        return this;
    }
    
    public LambdaQueryWrapper<T> in(SFunction<T, ?> func, Object id) {
        inData.put(func, id);
        return this;
    }
    
    public LambdaQueryWrapper<T> orderByDesc(SFunction<T, ?> func) {
        orderByDescData.add(func);
        return this;
    }
    
    public LambdaQueryWrapper<T> orderByAsc(SFunction<T, ?> func) {
        orderByAscData.add(func);
        return this;
    }
    
    public  LambdaQueryWrapper<T> orderBy(boolean b, boolean order, SFunction<T, ?> func) {
        if (b) {
            if (order) {
                orderByAsc(func);
            } else {
                orderByDesc(func);
            }
        }
        return this;
    }
    
    public List<SFunction<T, ?>> getOrderByAscData() {
        return orderByAscData;
    }
    
    public List<SFunction<T, ?>> getOrderByDescData() {
        return orderByDescData;
    }
    
    public HashMap<SFunction<T, ?>, Object> getEqData() {
        return eqData;
    }
    
    public HashMap<SFunction<T, ?>, Object> getLikeData() {
        return likeData;
    }
    
    public HashMap<SFunction<T, ?>, Object> getInData() {
        return inData;
    }
    
}
