package com.aslbekhar.aslbekharandroid.models;

import com.alibaba.fastjson.JSON;
import com.aslbekhar.aslbekharandroid.utilities.AppController;
import com.aslbekhar.aslbekharandroid.utilities.StaticData;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.aslbekhar.aslbekharandroid.utilities.Constants.BRAND_LIST;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.FALSE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.STORE_LIST;
import static com.aslbekhar.aslbekharandroid.utilities.Snippets.getSP;
import static com.aslbekhar.aslbekharandroid.utilities.Snippets.setSP;

/**
 * Created by Amin on 19/05/2016.
 */
public class BrandModel {


    String _id;
    String bId;
    String bName;
    String cName;
    String bLogo;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public String getbName() {
        return bName;
    }

    public void setbName(String bName) {
        this.bName = bName;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getbLogo() {
        return bLogo;
    }

    public void setbLogo(String bLogo) {
        this.bLogo = bLogo;
    }

    public static List<BrandModel> getBrandListFromAssets() {
        List<BrandModel> brandModelList = null;
        if (getSP(BRAND_LIST).equals(FALSE)) {
            String json = null;
            try {
                InputStream is = AppController.getInstance().getAssets().open("BrandList.txt");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, "UTF-8");
            } catch (IOException ex) {
                ex.printStackTrace();
                setSP(BRAND_LIST, FALSE);
            } finally {
                setSP(BRAND_LIST, json);
                try {
                    brandModelList = JSON.parseArray(json, BrandModel.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                brandModelList = JSON.parseArray(getSP(BRAND_LIST), BrandModel.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return brandModelList;
    }

    public static String getBrandLogo(String brandName){
        List<BrandModel> brandModelList = StaticData.getBrandModelList();
        for (BrandModel brand : brandModelList) {
            if (brand.getbName().equals(brandName)){
                return brand.getbLogo();
            }
        }
        return "";
    }

    public static List<BrandModel> getBrandListBasedOnCat(String cityCode,String catName) {
        List<BrandModel> brandModelList = StaticData.getBrandModelList();
        List<BrandModel> brandModelListBasedOnCat = new ArrayList<>();
        List<StoreModel> storeModelList = JSON.parseArray(getSP(cityCode + STORE_LIST), StoreModel.class);
        for (BrandModel model : brandModelList) {
            if (model.getcName().equals(catName)){
                for (StoreModel storeModel : storeModelList) {
                    if(storeModel.getbId().equals(model.getbId())){
                        brandModelListBasedOnCat.add(model);
                        break;
                    }
                }
            }
        }
        return brandModelListBasedOnCat;
    }

    public static List<String> getBrandLogosBasedOnCat(String cityCode, String catName, List<StoreModel> storeModelList) {
        List<String> getBrandLogosBasedOnCat;
        if (getSP(cityCode + catName).equals(FALSE)) {
            getBrandLogosBasedOnCat = new ArrayList<>();
            List<BrandModel> brandModelList = StaticData.getBrandModelList();
            for (BrandModel model : brandModelList) {
                if (model.getcName().equals(catName)){
                    for (StoreModel storeModel : storeModelList) {
                        if (model.getbId().equals(storeModel.getbId())){
                            getBrandLogosBasedOnCat.add(model.bLogo);
                            break;
                        }
                    }
                }
            }
            setSP(cityCode + catName, JSON.toJSONString(getBrandLogosBasedOnCat));
        } else {
            getBrandLogosBasedOnCat = JSON.parseArray(getSP(cityCode + catName), String.class);
        }
        return getBrandLogosBasedOnCat;
    }
}
