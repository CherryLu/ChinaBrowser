package com.chinabrowser.bean;

/**
 * 情景翻译数据实体
 * @author apple
 * @since 2014-10-8
 */
public class TranslateEntity {

    // 父
	private int cid;
	private int pcid;
	private String name = "";
	
	// 子
	private int id;
	private int no;
	private String sentence = "";
	private String lan = "";
	
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public int getPcid() {
		return pcid;
	}
	public void setPcid(int pcid) {
		this.pcid = pcid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public String getSentence() {
		return sentence;
	}
	public void setSentence(String sentence) {
		this.sentence = sentence;
	}
	public String getLan() {
		return lan;
	}
	public void setLan(String lan) {
		this.lan = lan;
	}
}
