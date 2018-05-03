package com.chinabrowser.utils;

import android.support.v4.app.Fragment;

import com.chinabrowser.APP;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 95470 on 2018/5/3.
 */

public class LabManager {
    private static LabManager labManager;

    private   int currentLab;
    private  Map<Integer,Fragment[]> listMap;
    private  Map<Integer,Integer> integerMap;



    public static LabManager getInstance(){
        if (labManager==null){
            labManager = new LabManager();
        }
        return labManager;
    }

    public Fragment[] getCurrentFragment(){

        return listMap.get(currentLab);
    }

    public void setCurrentLab(int labs){
        currentLab = labs;
    }




    private LabManager() {

        listMap = new HashMap<>();
        integerMap = new HashMap<>();

        Fragment[] fragments = new Fragment[3];
        currentLab = 0;
        listMap.put(currentLab,fragments);


        integerMap = new HashMap<>();
        integerMap.put(currentLab,0);

    }

    private void addLabs(){

        Fragment[] fragments = new Fragment[3];
        listMap.put(APP.homeTabs.size(),fragments);

        integerMap.put(APP.homeTabs.size(),0);
    }





}
