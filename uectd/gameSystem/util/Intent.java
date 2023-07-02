package uectd.gameSystem.util;

import java.util.HashMap;

public class Intent {
    private HashMap<String, Integer> integerMap;

    private HashMap<String, Boolean> booleanMap;

    public Intent() {
        integerMap = new HashMap<>();
        booleanMap = new HashMap<>();
    }

    public void setIntegerValue(String key, int value) {
        integerMap.put(key, value);
    }

    public int getIntegerValue(String key) {
        return integerMap.get(key);
    }

    public void setBooleanValue(String key, boolean value) {
        booleanMap.put(key, value);
    }

    public boolean getBooleanValue(String key) {
        return booleanMap.get(key);
    }

    public boolean containsIntegerKey(String key) {
        return integerMap.containsKey(key);
    }

    public boolean containsBooleanKey(String key) {
        return booleanMap.containsKey(key);
    }
}
