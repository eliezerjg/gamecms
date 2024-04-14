package com.gamecms.integracao.wyd.Configurations.Security;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CloudflareIPRangeFetcherSingleton {
    private static CloudflareIPRangeFetcherSingleton instance;
    private final List<String> ipRanges;
    private final List<String> urlIpRanges = Arrays.asList("https://www.cloudflare.com/ips-v4", "https://www.cloudflare.com/ips-v6");

    private CloudflareIPRangeFetcherSingleton() {
        this.ipRanges = fetchIPRanges();
    }

    public static synchronized CloudflareIPRangeFetcherSingleton getInstance() {
        if (instance == null) {
            instance = new CloudflareIPRangeFetcherSingleton();
        }
        return instance;
    }

    public List<String> getIPRanges() {
        return ipRanges;
    }

    private List<String> fetchIPRanges() {
        OkHttpClient client = new OkHttpClient();
        List<String> proxiesIpv4AndIpv6 = new ArrayList<String>();
        urlIpRanges.forEach(url -> {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    proxiesIpv4AndIpv6.addAll(Collections.emptyList());
                }

                String responseBody = response.body().string();
                proxiesIpv4AndIpv6.addAll(Arrays.stream(responseBody.split("\n"))
                        .map(String::trim)
                        .filter(line -> !line.isEmpty())
                        .toList());
            } catch (IOException e) {
                proxiesIpv4AndIpv6.addAll(Collections.emptyList());
            }
        });
       return proxiesIpv4AndIpv6;
    }
}