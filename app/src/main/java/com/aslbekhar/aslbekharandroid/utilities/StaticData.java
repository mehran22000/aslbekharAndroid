package com.aslbekhar.aslbekharandroid.utilities;

import com.alibaba.fastjson.JSON;
import com.aslbekhar.aslbekharandroid.models.BrandModel;
import com.aslbekhar.aslbekharandroid.models.CityModel;
import com.aslbekhar.aslbekharandroid.models.StoreModel;

import java.util.ArrayList;
import java.util.List;

import static com.aslbekhar.aslbekharandroid.utilities.Constants.BRAND_LIST;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CITY_LIST;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.FALSE;
import static com.aslbekhar.aslbekharandroid.utilities.Snippets.getSP;

/**
 * Created by Amin on 19/05/2016.
 */
public class StaticData {

    private static List<CityModel> cityModelList = new ArrayList<>();
    private static List<BrandModel> brandModelList = new ArrayList<>();

    static {
        if (getSP(CITY_LIST).equals(FALSE)) {
            cityModelList.add(new CityModel("021", "تهران", "Tehran"));
            cityModelList.add(new CityModel("031", "اصفهان", "Isfahan"));
            cityModelList.add(new CityModel("076", "کیش", "Kish"));
            cityModelList.add(new CityModel("071", "شیراز", "Shiraz"));
            cityModelList.add(new CityModel("041", "تبريز", "Tabriz"));
            cityModelList.add(new CityModel("051", "مشهد", "Mashhad"));
        }

        brandModelList = BrandModel.getBrandListFromAssets();

        StoreModel.getStoreListFromAssets();
    }

    public static List<BrandModel> getBrandModelList() {
        if (brandModelList == null) {
            brandModelList = JSON.parseArray(getSP(BRAND_LIST), BrandModel.class);
        }
        return brandModelList;
    }

    public static void setBrandModelList(List<BrandModel> brandModelList) {
        StaticData.brandModelList = brandModelList;
    }

    public static List<CityModel> getCityModelList() {
        return cityModelList;
    }

    public static void setCityModelList(List<CityModel> cityModelList) {
        StaticData.cityModelList = cityModelList;
    }
}
