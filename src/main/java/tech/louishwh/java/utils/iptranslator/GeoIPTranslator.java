package tech.louishwh.java.utils.iptranslator;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.*;


import java.io.InputStream;
import java.net.InetAddress;


public class GeoIPTranslator {

    public static final String IP_CITY_DB_URL = "/ipDb/GeoLite2-City.mmdb";
    private static DatabaseReader cityReader;

    public static final String IP_COUNTRY_DB_URL = "/ipDb/GeoLite2-Country.mmdb";
    private static DatabaseReader countryReader;

    static {
        InputStream cityDatabase = GeoIPTranslator.class.getResourceAsStream(IP_CITY_DB_URL);
        try {
            cityReader = new DatabaseReader.Builder(cityDatabase).build();
        } catch (Exception e) {
            cityReader = null;
        }

        InputStream countryDatabase = GeoIPTranslator.class.getResourceAsStream(IP_COUNTRY_DB_URL);
        try {
            countryReader = new DatabaseReader.Builder(countryDatabase).build();
        } catch (Exception e) {
            countryReader = null;
        }

    }


    public static Boolean ipInHk(String ip) {
        String ipCountryCode = getCountryCode(ip);
        return "HK".equals(ipCountryCode);
    }

    public static String getCountryCode(String ip) {
        Country ipCountryDetail = countryDetail(ip);
        if (ipCountryDetail == null) return "";
        return ipCountryDetail.getIsoCode();
    }

    public static String getCountryChineseName(String ip) {
        Country ipCountryDetail = countryDetail(ip);
        if (ipCountryDetail == null) return "";
        return ipCountryDetail.getNames().get("zh-CN");
    }

    public static String getCountryEnName(String ip) {
        Country ipCountryDetail = countryDetail(ip);
        if (ipCountryDetail == null) return "";
        return ipCountryDetail.getName();
    }

    private static Country countryDetail(String ip) {
        if (!ip.contains(".")) return null;
        try {
            InetAddress ipAddress = InetAddress.getByName(ip);
            CountryResponse response = countryReader.country(ipAddress);
            return response.getCountry();
        } catch (Exception e) {
            return null;
        }
    }

    private static City cityDetail(String ip) {
        if (!ip.contains(".")) return null;
        try {
            InetAddress ipAddress = InetAddress.getByName(ip);
            CityResponse response = cityReader.city(ipAddress);
            return response.getCity();
        } catch (Exception e) {
            return null;
        }
    }
    public static void main(String[] args) {
        String cnSZ = "27.46.110.36";
        String india = "27.48.110.36";

        System.out.println(GeoIPTranslator.cityDetail(cnSZ));
        System.out.println(GeoIPTranslator.cityDetail(india));
    }
}
