package com.aslbekhar.aslbekharandroid.utilities;

import com.alibaba.fastjson.JSON;
import com.aslbekhar.aslbekharandroid.models.BrandModel;
import com.aslbekhar.aslbekharandroid.models.CityModel;

import java.util.ArrayList;
import java.util.List;

import static com.aslbekhar.aslbekharandroid.utilities.Constants.BRAND_LIST;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CITY_LIST;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.FALSE;
import static com.aslbekhar.aslbekharandroid.utilities.Snippets.getSP;

/**
 * Created by Amin on 19/05/2016.
 *
 */
public class StaticData {

    private static List<CityModel> cityModelList = new ArrayList<>();
    private static List<BrandModel> brandModelList = new ArrayList<>();

    public static List<BrandModel> getBrandModelList() {
        if (brandModelList == null || brandModelList.size() == 0) {
            brandModelList = JSON.parseArray(getSP(BRAND_LIST), BrandModel.class);
        }
        return brandModelList;
    }

    public static void setBrandModelList(List<BrandModel> brandModelList) {
        StaticData.brandModelList = brandModelList;
    }

    public static List<CityModel> getCityModelList() {
        if (cityModelList == null || cityModelList.size() == 0 && !getSP(CITY_LIST).equals(FALSE)) {
            cityModelList = JSON.parseArray(getSP(CITY_LIST), CityModel.class);
        }
        return cityModelList;
    }

    public static void setCityModelList(List<CityModel> cityModelList) {
        StaticData.cityModelList = cityModelList;
    }
}
