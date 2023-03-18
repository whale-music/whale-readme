package org.core.utils;

import org.core.pojo.TbCollectPojo;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CollectSortUtil {
    
    private CollectSortUtil() {
    }
    
    /**
     * 用户所有歌单排序，把喜欢歌曲排序到第一位
     *
     * @param uid             用户ID
     * @param collectPojoList 用户ID
     */
    @NotNull
    public static List<TbCollectPojo> userLikeUserSort(Long uid, List<TbCollectPojo> collectPojoList) {
        // 遍历，把用户喜爱歌单排序到第一位
        List<TbCollectPojo> last = new ArrayList<>();
        for (int i = 0; i < collectPojoList.size(); i++) {
            if (Objects.equals(collectPojoList.get(i).getId(), uid)) {
                last.add(collectPojoList.get(i));
                collectPojoList.remove(i);
                break;
            }
        }
        last.addAll(collectPojoList);
        return last;
    }
}
