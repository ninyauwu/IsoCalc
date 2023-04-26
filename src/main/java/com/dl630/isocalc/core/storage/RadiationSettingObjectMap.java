package com.dl630.isocalc.core.storage;

import java.util.Map;
import java.util.TreeMap;

public class RadiationSettingObjectMap extends VersionedData {
    private TreeMap<String, RadiationSettingObject> savedRadiationSettingMap;

    public RadiationSettingObjectMap() {
        super("0.1");
    }

    public Map<String, RadiationSettingObject> getSavedRadiationSettingList() {
        if (savedRadiationSettingMap == null) savedRadiationSettingMap = new TreeMap<>();
        return savedRadiationSettingMap;
    }

    public Map<String, RadiationSettingObject> getSavedRadiationSettingListDescending() {
        if (savedRadiationSettingMap == null) savedRadiationSettingMap = new TreeMap<>();
        return savedRadiationSettingMap.descendingMap();
    }

    public void setSavedRadiationSettingList(TreeMap<String, RadiationSettingObject> savedRadiationSettingList) {
        savedRadiationSettingMap = savedRadiationSettingList;
    }

    public boolean addRadiationSettingToMap(String key, RadiationSettingObject setting) {
        if (savedRadiationSettingMap == null) savedRadiationSettingMap = new TreeMap<>();
        savedRadiationSettingMap.put(key, setting);
        return true;
    }

    public void removeRadiationSettingFromMap(String key) {
        if (savedRadiationSettingMap == null) savedRadiationSettingMap = new TreeMap<>();
        if (key == null) return;
        savedRadiationSettingMap.remove(key);
    }

    public RadiationSettingObject getRadiationSettingFromMap(String key) {
        for (String s : savedRadiationSettingMap.keySet()) {
            if (s.equals(key)) return savedRadiationSettingMap.get(key);
        }

        return null;
    }

    public boolean hasRadiationSettingKey(String key) {
        return savedRadiationSettingMap.containsKey(key);
    }
}
