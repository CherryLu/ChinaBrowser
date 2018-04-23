package com.chinabrowser.cbinterface;

import com.chinabrowser.bean.Content;
import com.chinabrowser.bean.Title;

/**
 * Created by 95470 on 2018/4/18.
 */

public interface HomeCallBack {
    void titleClick(int which,Title title);
    void backClick();
    void startContent(Content content);
    void startContentByurl(Content content);
}
