package com.chinabrowser.cbinterface;

import com.chinabrowser.bean.Content;

/**
 * Created by 95470 on 2018/4/18.
 */

public interface HomeCallBack {
    void titleClick(int which);
    void backClick();
    void startContent(Content content);
}
