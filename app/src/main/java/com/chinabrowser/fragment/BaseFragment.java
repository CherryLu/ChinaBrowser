package com.chinabrowser.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.chinabrowser.cbinterface.HomeCallBack;

/**
 * Created by Administrator on 2018/3/26.
 */

public class BaseFragment extends Fragment {
    public View rootView;
    public HomeCallBack homeCallBack;

    /**
     * @Variables savedBudleData : 用于保存数据的bundle
     */
    public Bundle savedBudleData = null;
    public String test = "";

}
