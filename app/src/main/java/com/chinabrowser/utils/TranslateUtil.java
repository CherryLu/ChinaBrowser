package com.chinabrowser.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.chinabrowser.R;
import com.chinabrowser.bean.TranslateEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 情景翻译辅助方法
 */
public class TranslateUtil {

    /** 情景翻译 */
    public static final String TRANSLATE_DATAFILE_NAME = "trans.sqlite";                 // 数据库文件名称
    public static final String[] TRANSLATE_COLUMNS = new String[]{"zh-Hans", "en", "tr"};// 数据库表字段
    public static final int[] TRANSLATE_SELECTOR_STRS = new int[]{R.string.translate_language_zh, R.string.translate_language_en, R.string.translate_language_tr};
    public static final String SPF_TRANSLATE_FROM = "spf_translate_from";                // 原语言
    public static final String SPF_TRANSLATE_TO = "spf_translate_to";                    // 目标语言
    
    private Context mContext;
    private Map<String, String> mSecondMap = null;
    
    public TranslateUtil(Context context) {
        this.mContext = context;
    }
    
    public static boolean COPY_TRANSLATEDATA_FORCE = true;
    
    /**
     * 取得一级数据
     * @return
     */
    public List<TranslateEntity> getTranslateSorts() {
        List<TranslateEntity> entities = new ArrayList<TranslateEntity>();
        
        SQLiteDatabase helper = CommUtils.openDatabase(mContext, TRANSLATE_DATAFILE_NAME, R.raw.trans);
        String sqlStr = "select * from tab_sentence_cate where pcid = 0";
        Cursor cursor = helper.rawQuery(sqlStr, null);
        if (cursor.getCount() > 0) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                TranslateEntity entity = new TranslateEntity();
                entity.setCid(cursor.getInt(cursor.getColumnIndex("cid")));
                entity.setPcid(cursor.getInt(cursor.getColumnIndex("pcid")));
                entity.setName(cursor.getString(cursor.getColumnIndex("name")));
                
                entities.add(entity);
            }
        }
        
        cursor.close();
        helper.close();
        return entities;
    }
    
    /**
     * 取得二级三级数据
     * @param pcid
     * @return
     */
    public List<Map<String, Object>> getTranslateChild(int pcid, String pName) {
        initSecondMap();
        
        List<Map<String, Object>> secondList = new ArrayList<Map<String,Object>>();

        SQLiteDatabase helper = CommUtils.openDatabase(mContext, TRANSLATE_DATAFILE_NAME, R.raw.trans);
        
        String sqlStr = "select * from tab_sentence_cate where pcid = " + pcid;
        
        Cursor cursor1 = helper.rawQuery(sqlStr, null);
        if (cursor1.getCount() > 0) {
            Map<String, Object> secondMap = null;
            List<Map<String, Object>> sentenceList = null;
            for (cursor1.moveToFirst(); !cursor1.isAfterLast(); cursor1.moveToNext()) {           // 二级分类 (如: "机场"->"办理登记")
                secondMap = new HashMap<String, Object>();
                sentenceList = new ArrayList<Map<String,Object>>();

                TranslateEntity sortEntity = new TranslateEntity();                              
                sortEntity.setCid(cursor1.getInt(cursor1.getColumnIndex("cid")));
                sortEntity.setPcid(cursor1.getInt(cursor1.getColumnIndex("pcid")));
                String name = getSecondName(cursor1.getString(cursor1.getColumnIndex("name")));
                sortEntity.setName(name.length() <= 0 ? pName : name); 

                sqlStr = "select distinct no from tab_sentence where cid = " + sortEntity.getCid(); 
                
                Cursor cursor2 = helper.rawQuery(sqlStr, null);
                if (cursor2.getCount() > 0) {
                    
                    Map<String, Object> sentenceMap = null;
                    for (cursor2.moveToFirst(); !cursor2.isAfterLast(); cursor2.moveToNext()) { // 三级 (如: "机场"->"办理登记"->"打扰一下，您知道在哪里办理登记手续么?")
                        
                        sentenceMap = new HashMap<String, Object>();// 分别记录各种语言
                        
                        sqlStr = "select * from tab_sentence where no = " + cursor2.getInt(cursor2.getColumnIndex("no"));
                        
                        Cursor cursor3 = helper.rawQuery(sqlStr, null); 
                        if (cursor3.getCount() > 0) {
                            for (cursor3.moveToFirst(); !cursor3.isAfterLast(); cursor3.moveToNext()) {

                                if (cursor3.getString(cursor3.getColumnIndex("lan")).equalsIgnoreCase(TRANSLATE_COLUMNS[0])) {
                                    TranslateEntity sentenceEntity = new TranslateEntity();
                                    sentenceEntity.setId(cursor3.getInt(cursor3.getColumnIndex("id")));
                                    sentenceEntity.setCid(cursor3.getInt(cursor3.getColumnIndex("cid")));
                                    sentenceEntity.setNo(cursor3.getInt(cursor3.getColumnIndex("no")));
                                    sentenceEntity.setSentence(cursor3.getString(cursor3.getColumnIndex("sentence")));
                                    sentenceEntity.setLan(cursor3.getString(cursor3.getColumnIndex("lan")));
                                    sentenceMap.put(TRANSLATE_COLUMNS[0], sentenceEntity);
                                }
                                
                                else if (cursor3.getString(cursor3.getColumnIndex("lan")).equalsIgnoreCase(TRANSLATE_COLUMNS[1])) {
                                    TranslateEntity sentenceEntity = new TranslateEntity();
                                    sentenceEntity.setId(cursor3.getInt(cursor3.getColumnIndex("id")));
                                    sentenceEntity.setCid(cursor3.getInt(cursor3.getColumnIndex("cid")));
                                    sentenceEntity.setNo(cursor3.getInt(cursor3.getColumnIndex("no")));
                                    sentenceEntity.setSentence(cursor3.getString(cursor3.getColumnIndex("sentence")));
                                    sentenceEntity.setLan(cursor3.getString(cursor3.getColumnIndex("lan")));
                                    sentenceMap.put(TRANSLATE_COLUMNS[1], sentenceEntity);
                                }
                                
                                else if (cursor3.getString(cursor3.getColumnIndex("lan")).equalsIgnoreCase(TRANSLATE_COLUMNS[2])) {
                                    TranslateEntity sentenceEntity = new TranslateEntity();
                                    sentenceEntity.setId(cursor3.getInt(cursor3.getColumnIndex("id")));
                                    sentenceEntity.setCid(cursor3.getInt(cursor3.getColumnIndex("cid")));
                                    sentenceEntity.setNo(cursor3.getInt(cursor3.getColumnIndex("no")));
                                    sentenceEntity.setSentence(cursor3.getString(cursor3.getColumnIndex("sentence")));
                                    sentenceEntity.setLan(cursor3.getString(cursor3.getColumnIndex("lan")));
                                    sentenceMap.put(TRANSLATE_COLUMNS[2], sentenceEntity);
                                }
                            }
                        }
                        cursor3.close();
                        
                        // 记录句子的各种语言翻译内容
                        sentenceList.add(sentenceMap);
                    }
                }
                cursor2.close();
                
                if (secondMap != null) {
                    secondMap.put("sortEntity", sortEntity);// TranslateEntity
                    secondMap.put("sentenceList", sentenceList);// List<Map<String, Object>>
                    secondList.add(secondMap);
                }
                secondMap = null;
            }
        }

        cursor1.close();
        helper.close();
        return secondList;
    }

    /**
     * 二级分类名称实现本地化多语言
     */
    private void initSecondMap() {
        mSecondMap = new HashMap<String, String>();
        String[] key = mContext.getResources().getStringArray(R.array.array_translate_key);
        String[] value = mContext.getResources().getStringArray(R.array.array_translate_value);
        for (int i = 0, len = key.length; i < len; i++ ) {
            mSecondMap.put(key[i], value[i]); 
        }
    }

    /**
     * Map<String, String>根据key取得value 
     */
    private String getSecondName(String mkey) {
    	return mSecondMap.get(mkey)==null? "" : mSecondMap.get(mkey);
    	
//    	Log.e("xxxxxxx", "xxxyyyxxx: " + mSecondMap.get(mkey));
//    	
//        String value = "";
//        for (String key : mSecondMap.keySet()) {
//            if (key.equals(mkey)) {
//                value = mSecondMap.get(key);
//                break;
//            }
//        }
//        
//    	Log.e("xxxxxxx", "~~~~~~~~ xxxyyyxxx: " + value);
//        return value;
    }
}
