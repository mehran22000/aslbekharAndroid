package com.example.n55.jsonandvally.model;

import android.content.Context;

import com.example.n55.jsonandvally.MySharedPreferences;

import java.util.ArrayList;

public class Kala {




//"id"//"title"//"thumbnail"//"image"//"price"//"prevPrice"//"mark"//"englishTitle"//"desc"//"isSpecialOffer"//"showPrevPrice"
//"callForPrice"//"endDateMS"//"link"//"tags"

	private int id,price,prevPrice;
	private String title,desc, imageurl,link,thumbnailUrl,mark,englishTitle,endDateMS;
	boolean isSpecialOffer,showPrevPrice,callForPrice;
	private ArrayList<String> tags;
	private String WEBSERVER;


	public Kala(Context context) {

		final MySharedPreferences sharedpreapi = new MySharedPreferences(context);
		WEBSERVER = "http://"+sharedpreapi.getStringurl();
	}


	public Kala(int id, String name,String thumbnail,String image ,int price,int prevPrice,String mark,String englishTitle, String desc, boolean isSpecialOffer, boolean showPrevPrice, boolean callForPrice,String link, ArrayList<String> tags){
		this.id=id;
		this.title=name;
		this.thumbnailUrl=thumbnail;
		this.imageurl=image;
		this.price=price;
		this.prevPrice=prevPrice;
		this.mark=mark;
		this.englishTitle=englishTitle;
		this.desc=desc;
		this.isSpecialOffer=isSpecialOffer;
		this.showPrevPrice=showPrevPrice;
		this.callForPrice=callForPrice;
		this.link=link;
		this.tags=tags;
	}


	//id
	public int getid() {
		return id;
	}

	public void setid(int id) {
		this.id = id;
	}

	//title
	public String getTitle() {
		return title;
	}

	public void setTitle(String name) {
		this.title = name;
	}

	//tumbnail
	public String gettumbnail() {
		return thumbnailUrl;
	}

	public void setthumbnail(String thumbnail) {
		this.thumbnailUrl = WEBSERVER+thumbnail;
	}

	//image
	public String getimage() {
		return imageurl;
	}

	public void setimage(String image) {
		this.imageurl = WEBSERVER+image;
	}

	//price
	public int getprice() {
		return price;
	}

	public void setprice(int price) {
		this.price = price;
	}

	//prevPrice
	public int getprevPrice() {
		return price;
	}

	public void setprevPrice(int prevPrice) {
		this.prevPrice = prevPrice;
	}

	//mark
	public String getmark() {
		return mark;
	}

	public void setmark(String mark) {
		this.mark= mark;
	}

	//englishtitle

	public String getenglishTitle() {
		return englishTitle;
	}

	public void setenglishTitle(String name) {
		this.englishTitle = name;
	}

	//desc
	public String getdesc() {
		return desc;
	}

	public void setdesc(String desc) {
		this.desc= desc;
	}

	//isSpecialOffer
	public Boolean getisSpecialOffer() {
		return isSpecialOffer;
	}

	public void setisSpecialOffer(Boolean isSpecialOffer) {
		this.isSpecialOffer = isSpecialOffer;
	}

	//callForPrice
	public Boolean getcallForPrice() {
		return callForPrice;
	}

	public void setcallForPrice(Boolean callForPrice) {
		this.callForPrice = callForPrice;
	}

	//showPrevPrice
	public Boolean getshowPrevPrice() {
		return showPrevPrice;
	}

	public void setshowPrevPrice(Boolean showPrevPrice) {
		this.showPrevPrice = showPrevPrice;
	}

	//link
	public String getlink() {
		return link;
	}

	public void setlink(String link) {
		this.link = link;
	}

	//tags

	public ArrayList<String> gettags() {
		return tags;
	}

	public void settags(ArrayList<String> tags) {
		this.tags = tags;
	}

}
