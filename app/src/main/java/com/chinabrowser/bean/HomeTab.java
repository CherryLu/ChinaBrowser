package com.chinabrowser.bean;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/23.
 */

public class HomeTab implements Serializable{
    public String title;
    public Bitmap bitmap;

    public int type;

    public Title titleData;// LISTPAG
    public String newsId;
    public String url;

    public static int HOMEPAGE = 0;
    public static int LISTPAG = 1;
    public static int NEWSPAGE = 2;
    public static int TRANSLATEPAGE = 3;
    public static int URLWEBPAGE = 4;
    public static int SEARCHPAGE = 5;




}
