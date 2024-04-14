package com.gamecms.integracao.wyd.Configurations.Security;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class IPsBlacklist {
    private static IPsBlacklist instance;
    private Map<String, Integer> ipMap;

    private IPsBlacklist() {
        ipMap = new HashMap<>();
    }

    public static synchronized IPsBlacklist getInstance() {
        if (instance == null) {
            instance = new IPsBlacklist();
        }
        return instance;
    }

    public void addIP(String ip) {
        if (ipMap.containsKey(ip)) {
            int count = ipMap.get(ip);
            ipMap.put(ip, count + 1);
        } else {
            ipMap.put(ip, 1);
        }
    }

    public void addIPAndBlockNow(String ip) {
        if (ipMap.containsKey(ip)) {
            int count = ipMap.get(ip);
            ipMap.put(ip, 15);
        } else {
            ipMap.put(ip, 15);
        }
    }

    public boolean containsIP(String ip) {
        return ipMap.containsKey(ip);
    }

    public void removeIP(String ip) {
        ipMap.remove(ip);
    }

    public void clear() {
        ipMap.clear();
    }

    public int getIPCount() {
        return ipMap.size();
    }

    public int getIPCount(String ip) {
        return ipMap.get(ip);
    }

    public Map<String, Integer> getAllIPs() {
        return new HashMap<>(ipMap);
    }
}
