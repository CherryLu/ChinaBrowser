package com.chinabrowser.bean;

import com.chinabrowser.utils.Constant;
import com.chinabrowser.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 95470 on 2018/4/15.
 */

public class Recommend {
    private int type ;
    private String id;
    private String title;
    private String url;
    private Title maintitle;
    private Action contentAction;
    private Action titleAction;
    private List<Content> contents;
    private boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public Action getTitleAction() {
        return titleAction;
    }

    public Action getContentAction() {
        return contentAction;
    }

    public Title getMaintitle() {
        return maintitle;
    }

    public void setMaintitle(Title maintitle) {
        this.maintitle = maintitle;
    }

    public int getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public List<Content> getContents() {
        return contents;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setContents(List<Content> contents) {
        this.contents = contents;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public void parse(JSONObject jsonObject){
            if (jsonObject!=null){
                String  layout_style = JsonUtils.getString(jsonObject,"layout_style");
                getTypeByLayout(layout_style);
                JSONObject obj = JsonUtils.getJSONObject(jsonObject,"title");
                maintitle = new Title();
                maintitle.parse(obj);
                JSONArray arry = JsonUtils.getJSONArray(jsonObject,"action_data");
                JSONObject aobj = JsonUtils.getJsonArray(arry,0);
                contentAction = new Action();
                contentAction.parse(aobj);

                JSONArray arr = JsonUtils.getJSONArray(jsonObject,"action_title");
                JSONObject tobj = JsonUtils.getJsonArray(arr,0);

                titleAction = new Action();
                titleAction.parse(tobj);

                JSONArray array = JsonUtils.getJSONArray(jsonObject,"data");
                contents = new ArrayList<>();
                for (int i = 0;i<array.length();i++){
                    JSONObject object = JsonUtils.getJsonArray(array,i);
                    Content content = new Content();
                    content.parse(object);
                    contents.add(content);
                }
            }



    }

    public void getTypeByLayout(String layout_style){
        switch (layout_style){
            case Constant.BANNER:
                type  = Constant.SLIDE;
                break;
            case Constant.HOT:
                type = Constant.TWOLINE;
                break;
            case Constant.TRA:
                type = Constant.ONELINE;
                break;
            case Constant.RADIOLAYOUT:
                type = Constant.RADIO;
                break;
            case Constant.LABSTITLE:
                type = Constant.LABS;
                break;

        }
    }
}
