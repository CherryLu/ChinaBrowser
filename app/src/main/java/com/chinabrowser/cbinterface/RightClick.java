package com.chinabrowser.cbinterface;

import com.chinabrowser.bean.Content;
import com.chinabrowser.bean.Recommend;

/**
 * Created by 95470 on 2018/4/23.
 */

public interface RightClick {

    void itemClick(Recommend recommend);
    void startUrl(Content content);
}
