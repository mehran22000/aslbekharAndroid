package com.aslbekhar.aslbekharandroid.models;

import com.alibaba.fastjson.JSON;
import com.aslbekhar.aslbekharandroid.utilities.AppController;
import com.aslbekhar.aslbekharandroid.utilities.StaticData;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.aslbekhar.aslbekharandroid.utilities.Constants.CAT_LIST;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CAT_LIST_FILE_POSTFIX;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.FALSE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.STORE_LIST;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.STORE_LIST_FILE_POSTFIX;
import static com.aslbekhar.aslbekharandroid.utilities.Snippets.getSP;
import static com.aslbekhar.aslbekharandroid.utilities.Snippets.setSP;

/**
 * Created by Amin on 19/05/2016.
 */
public class StoreModel  implements Comparable<StoreModel>{

    String _id;
    String bId;
    String sId;
    String sName;
    String bName;
    String bCategory;
    String bDistributor;
    String sCity;
    String sAddress;
    String sHours;
    String sAreaCode;
    String sTel1;
    String sTel2;
    String sLat;
    String sLong;
    String sVerified;
    String bCategoryId;
    String dStartDate;
    String dEndDate;
    String dStartDateFa;
    String dEndDateFa;
    String dPrecentage = "0";
    String dNote;
    String distance;

    public StoreModel() {
    }

    public StoreModel(String sName, String bName, String sAddress, String sHour, String sTel1, String sLat, String sLong, int sDiscount, String sVerified) {
        this.sName = sName;
        this.bName = bName;
        this.sAddress = sAddress;
        this.sHours = sHour;
        this.sTel1 = sTel1;
        this.sLat = sLat;
        this.sLong = sLong;
        this.dPrecentage = String.valueOf(sDiscount);
        this.sVerified = sVerified;
    }

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

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getbName() {
        return bName;
    }

    public void setbName(String bName) {
        this.bName = bName;
    }

    public String getbCategory() {
        return bCategory;
    }

    public void setbCategory(String bCategory) {
        this.bCategory = bCategory;
    }

    public String getbDistributor() {
        return bDistributor;
    }

    public void setbDistributor(String bDistributor) {
        this.bDistributor = bDistributor;
    }

    public String getsCity() {
        return sCity;
    }

    public void setsCity(String sCity) {
        this.sCity = sCity;
    }

    public String getsAddress() {
        return sAddress;
    }

    public void setsAddress(String sAddress) {
        this.sAddress = sAddress;
    }

    public String getsHour() {
        return sHours;
    }

    public void setsHour(String sHour) {
        this.sHours = sHour;
    }

    public String getsAreaCode() {
        return sAreaCode;
    }

    public void setsAreaCode(String sAreaCode) {
        this.sAreaCode = sAreaCode;
    }

    public String getsTel1() {
        return sTel1;
    }

    public void setsTel1(String sTel1) {
        this.sTel1 = sTel1;
    }

    public String getsTel2() {
        return sTel2;
    }

    public void setsTel2(String sTel2) {
        this.sTel2 = sTel2;
    }

    public String getsLat() {
        return sLat;
    }

    public void setsLat(String sLat) {
        this.sLat = sLat;
    }

    public String getsLong() {
        return sLong;
    }

    public void setsLong(String sLong) {
        this.sLong = sLong;
    }

    public String getsVerified() {
        return sVerified;
    }

    public void setsVerified(String sVerified) {
        this.sVerified = sVerified;
    }

    public String getbCategoryId() {
        return bCategoryId;
    }

    public void setbCategoryId(String bCategoryId) {
        this.bCategoryId = bCategoryId;
    }

    public String getsHours() {
        return sHours;
    }

    public void setsHours(String sHours) {
        this.sHours = sHours;
    }

    public String getdStartDate() {
        return dStartDate;
    }

    public void setdStartDate(String dStartDate) {
        this.dStartDate = dStartDate;
    }

    public String getdEndDate() {
        return dEndDate;
    }

    public void setdEndDate(String dEndDate) {
        this.dEndDate = dEndDate;
    }

    public String getdStartDateFa() {
        return dStartDateFa;
    }

    public void setdStartDateFa(String dStartDateFa) {
        this.dStartDateFa = dStartDateFa;
    }

    public String getdEndDateFa() {
        return dEndDateFa;
    }

    public void setdEndDateFa(String dEndDateFa) {
        this.dEndDateFa = dEndDateFa;
    }

    public String getdPrecentage() {
        return dPrecentage;
    }

    public int getdPrecentageInt() {
        int discount;
        try {
            discount = Integer.parseInt(dPrecentage);
        } catch (Exception e) {
            discount = 0;
        }
        return discount;
    }

    public void setdPrecentage(String dPrecentage) {
        this.dPrecentage = dPrecentage;
    }

    public String getdNote() {
        return dNote;
    }

    public void setdNote(String dNote) {
        this.dNote = dNote;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public static void getCatAndStoreListFromAssets() {

        for (CityModel city : StaticData.getCityModelList()) {
            if (getSP(city.getId() + CAT_LIST).equals(FALSE)) {
                String json = null;
                try {
                    InputStream is = AppController.getInstance().getAssets().open(city.getId() + CAT_LIST_FILE_POSTFIX);
                    int size = is.available();
                    byte[] buffer = new byte[size];
                    is.read(buffer);
                    is.close();
                    json = new String(buffer, "UTF-8");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    setSP(city.getId() + CAT_LIST, FALSE);
                }
                if (json != null) {
                    setSP(city.getId() + CAT_LIST, json);
                }
                if (getSP(city.getId() + STORE_LIST).equals(FALSE)) {
                    json = null;
                    try {
                        InputStream is = AppController.getInstance().getAssets().open(city.getId() + STORE_LIST_FILE_POSTFIX);
                        int size = is.available();
                        byte[] buffer = new byte[size];
                        is.read(buffer);
                        is.close();
                        json = new String(buffer, "UTF-8");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        setSP(city.getId() + STORE_LIST, FALSE);
                    }
                    if (json != null) {
                        setSP(city.getId() + STORE_LIST, json);
                    }
                }
            }
        }
    }

    public static void getStoreListFromAssets() {
        for (CityModel city : StaticData.getCityModelList()) {
            if (getSP(city.getId() + CAT_LIST).equals(FALSE)) {
                if (getSP(city.getId() + STORE_LIST).equals(FALSE)) {
                    String json = null;
                    try {
                        InputStream is = AppController.getInstance().getAssets().open(city.getId() + STORE_LIST_FILE_POSTFIX);
                        int size = is.available();
                        byte[] buffer = new byte[size];
                        is.read(buffer);
                        is.close();
                        json = new String(buffer, "UTF-8");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        setSP(city.getId() + STORE_LIST, FALSE);
                    }
                    if (json != null) {
                        List<StoreModel> storeModelList = JSON.parseArray(json, StoreModel.class);
                        setSP(city.getId() + STORE_LIST, json);
                        setSP(city.getId() + CAT_LIST, JSON.toJSONString(getCatsFromStoreList(city.getId(), storeModelList)));
                    }
                }
            }
        }
    }


    public static List<StoreModel> getStoreListFromAssetsByCityCode(String CityCode) {
        List<StoreModel> storeModelList = new ArrayList<>();
        String json = null;
        try {
            InputStream is = AppController.getInstance().getAssets().open(CityCode + STORE_LIST_FILE_POSTFIX);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            setSP(CityCode + STORE_LIST, FALSE);
        }
        if (json != null) {
            storeModelList = JSON.parseArray(json, StoreModel.class);
            setSP(CityCode + STORE_LIST, json);
            setSP(CityCode + CAT_LIST, JSON.toJSONString(getCatsFromStoreList(CityCode, storeModelList)));
        }
        return storeModelList;
    }

    public static List<StoreModel> getStoreListBasedOnCatNameAndBrand(String cityCode, String catName, String brandId) {

        List<StoreModel> storeModelList;
        String jsonData = getSP(cityCode + STORE_LIST);
        if (!jsonData.equals(FALSE)) {
            storeModelList = JSON.parseArray(getSP(cityCode + STORE_LIST), StoreModel.class);
        } else {
            storeModelList = getStoreListFromAssetsByCityCode(cityCode);
        }

        List<StoreModel> storeListBasedOnCatNameAndBrand = new ArrayList<>();

        for (StoreModel storeModel : storeModelList) {
            if (storeModel.getbCategory().equals(catName) && storeModel.getbId().equals(brandId)) {
                storeListBasedOnCatNameAndBrand.add(storeModel);
            }
        }

        return storeListBasedOnCatNameAndBrand;
    }

    private static List<CategoryModel> getCatsFromStoreList(String cityCode, List<StoreModel> storeModelList) {
        List<CategoryModel> categoryModelList = new ArrayList<>();
        for (StoreModel storeModel : storeModelList) {
            boolean catFound = false;
            for (CategoryModel catModel : categoryModelList) {
                if (catModel.getTitle().equals(storeModel.bCategory)) {
                    catFound = true;
                    break;
                }
            }
            if (!catFound) {
                categoryModelList.add(new CategoryModel(storeModel.getbCategory(),
                        storeModel.getbCategoryId(),
                        BrandModel.getBrandLogosBasedOnCat(cityCode, storeModel.getbCategory(),
                                storeModelList)));
            }
        }
        Collections.sort(categoryModelList, new Comparator<CategoryModel>() {
            @Override
            public int compare(CategoryModel lhs, CategoryModel rhs) {

                return lhs.getTitle().compareToIgnoreCase(rhs.getTitle());
            }
        });
        return categoryModelList;
    }

    @Override
    public int compareTo(StoreModel another) {
        if (distance == null || distance.length() == 0 || another.distance == null || another.distance.length() == 0){
            return 0;
        }

        try {
            if (Float.parseFloat(distance ) > Float.parseFloat(distance) ) {
                return 1;
            }
            else if (Float.parseFloat(distance ) < Float.parseFloat(distance) ) {
                return -1;
            }
            else {
                return 0;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
