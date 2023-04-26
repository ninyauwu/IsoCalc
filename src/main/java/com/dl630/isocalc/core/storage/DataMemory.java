package com.dl630.isocalc.core.storage;

import com.dl630.isocalc.element.Element;
import com.dl630.isocalc.element.IsotopeMass;
import com.google.gson.reflect.TypeToken;

import java.math.BigDecimal;
import java.util.*;

public class DataMemory {
    private static String selectedRadiationSettingKey;
    private static String selectedMaterialSettingKey;
    private static String currentMaterialSettingKey;

    private static RadiationSettingObjectMap radiationSettingMap;

    private static Map<Element, ArrayList<Integer>> currentSelectedIsotopeMap;
    private static TreeMap<String, MaterialMakeup> savedMasses;

    private static BigDecimal savedIrradiationTime;
    private static Integer savedCycleCount;
    private static BigDecimal savedDecayTime;
    private static Element savedContainerElement;
    private static BigDecimal savedContainerThickness;

    private static String savedIrradiationPath;
    private static String savedDecayPath;

    public static void addSelectedIsotope(Element element, Integer isotope) {
        if (currentSelectedIsotopeMap == null) { currentSelectedIsotopeMap = new HashMap<>(); }

        for (Element e : currentSelectedIsotopeMap.keySet()) {
            if (element == e) {
                currentSelectedIsotopeMap.get(e).add(isotope);
                return;
            }
        }

        // Add new map entry if map doesn't contain element
        ArrayList<Integer> isotopeList = new ArrayList<>();
        isotopeList.add(isotope);
        currentSelectedIsotopeMap.put(element, isotopeList);
    }

    public static void removeSelectedIsotope(Element element, Integer isotope) {
        Element removeElement = null;
        Integer removeIsotope = null;
        for (Element selectedElement : getCurrentSelectedIsotopes().keySet()) {
            if (selectedElement.equals(element)) {
                removeElement = selectedElement;
                for (Integer selectedIsotope : getCurrentSelectedIsotopes().get(selectedElement)) {
                    if (selectedIsotope.equals(isotope)) {
                        removeIsotope = selectedIsotope;
                    }
                }
            }
        }

        if (currentSelectedIsotopeMap.get(removeElement).size() == 1 && removeElement != null) {
            currentSelectedIsotopeMap.remove(removeElement);
        } else if (removeIsotope != null) {
            currentSelectedIsotopeMap.get(element).remove(removeIsotope);
        }
    }

    public static void clearSelectedIsotopes() {
        if (currentSelectedIsotopeMap == null) currentSelectedIsotopeMap = new HashMap<>();
        currentSelectedIsotopeMap.clear();
    }

    public static void addMass(IsotopeMass mass) {
        if (savedMasses == null) savedMasses = new TreeMap<>();
        savedMasses.get(currentMaterialSettingKey).getMasses().add(mass);
    }

    public static void addMaterial(String key, MaterialMakeup material) {
        if (savedMasses == null) savedMasses = new TreeMap<>();
        savedMasses.put(key, material);
    }

    public static void removeMassEntryFromMap(String key) {
        if (Objects.equals(key, selectedMaterialSettingKey)) setSelectedMaterialSettingKey(null);
        savedMasses.remove(key);
    }

    public static void removeMass(Element element, Integer isotope) {
        for (IsotopeMass mass : savedMasses.get(currentMaterialSettingKey).getMasses()) {
            if (mass.getIsotope().equals(isotope) && mass.getElement() == element) {
                savedMasses.get(getCurrentMaterialSettingKey()).getMasses().remove(mass);
            }
        }
    }

    public static void clearMasses() {
        if (getSavedMasses().get(getCurrentMaterialSettingKey()) == null) return;
        getSavedMasses().get(getCurrentMaterialSettingKey()).getMasses().clear();
    }

    public static Map<Element, ArrayList<Integer>> getCurrentSelectedIsotopes() {
        if (currentSelectedIsotopeMap == null) {
            resetCurrentIsotopes();
        }
        return currentSelectedIsotopeMap;
    }

    public static void resetCurrentIsotopes() {
        if (getSavedMasses().get(currentMaterialSettingKey).getMasses().size() > 0) {
            setCurrentSelectedIsotopesFromMasses();
        } else {
            currentSelectedIsotopeMap = new HashMap<>();
        }
    }

    private static void setCurrentSelectedIsotopesFromMasses() {
        currentSelectedIsotopeMap = new HashMap<>();
        for (IsotopeMass isotopeMass : getSavedMasses().get(currentMaterialSettingKey).getMasses()) {
            currentSelectedIsotopeMap.computeIfAbsent(isotopeMass.getElement(), k -> new ArrayList<>());
            currentSelectedIsotopeMap.get(isotopeMass.getElement()).add(isotopeMass.getIsotope());
        }
    }

    public static TreeMap<String, MaterialMakeup> getSavedMasses() {
        if (savedMasses == null) savedMasses = new TreeMap<>();
        return savedMasses;
    }

    public static void setSelectedRadiationSettingKey(String object) {
        selectedRadiationSettingKey = object;
    }

    public static String getSelectedRadiationSettingKey() {
        return selectedRadiationSettingKey;
    }

    public static void testMasses() {
        if (savedMasses == null) {
            System.out.println("masses were null");
            return;
        }

        for (IsotopeMass mass : savedMasses.get(currentMaterialSettingKey).getMasses()) {
            System.out.println(mass.getElement().getName() + "-" + mass.getIsotope() + ": " + mass.getMass());
        }
    }

    public static BigDecimal getTotalMass() {
        return getSavedMasses().get(getCurrentMaterialSettingKey()).getTotalMass();
    }

    public static void setTotalMass(String key, BigDecimal totalMass) {
        getSavedMasses().get(key).setTotalMass(totalMass);
    }

    public static Map<String, RadiationSettingObject> getSavedRadiationSettingList() {
        if (radiationSettingMap == null) radiationSettingMap = new RadiationSettingObjectMap();
        return radiationSettingMap.getSavedRadiationSettingList();
    }

    public static Map<String, RadiationSettingObject> getSavedRadiationSettingListDescending() {
        if (radiationSettingMap == null) radiationSettingMap = new RadiationSettingObjectMap();
        return  radiationSettingMap.getSavedRadiationSettingListDescending();
    }

    public static void setSavedRadiationSettingList(TreeMap<String, RadiationSettingObject> savedRadiationSettingList) {
        if (radiationSettingMap == null) radiationSettingMap = new RadiationSettingObjectMap();
        radiationSettingMap.setSavedRadiationSettingList(savedRadiationSettingList);
    }

    public static void setRadiationSettingMap(RadiationSettingObjectMap map) {
        radiationSettingMap = map;
    }

    public static boolean addRadiationSettingToMap(String key, RadiationSettingObject setting) {
        if (radiationSettingMap == null) radiationSettingMap = new RadiationSettingObjectMap();
        return radiationSettingMap.addRadiationSettingToMap(key, setting);
    }

    public static void removeRadiationSettingFromMap(String key) {
        if (radiationSettingMap == null) radiationSettingMap = new RadiationSettingObjectMap();
        radiationSettingMap.removeRadiationSettingFromMap(key);
    }

    public static RadiationSettingObject getRadiationSettingFromMap(String key) {
        if (radiationSettingMap == null) radiationSettingMap = new RadiationSettingObjectMap();
        return radiationSettingMap.getRadiationSettingFromMap(key);
    }

    public static boolean hasRadiationSettingKey(String key) {
        if (radiationSettingMap == null) radiationSettingMap = new RadiationSettingObjectMap();
        return radiationSettingMap.hasRadiationSettingKey(key);
    }

    public static void loadSavedPaths() {
        FileManager<PathStructure> fileManager = new FileManager<>("saved_paths.json");
        PathStructure structure = fileManager.loadFile(new TypeToken<PathStructure>(){}.getType());
        if (structure == null) return;
        setSavedIrradiationPath(structure.getIrradiation());
        setSavedDecayPath(structure.getDecay());
    }

    public static void loadSavedRadiationSettingMap() {
        FileManager<RadiationSettingObjectMap> fileManager = new FileManager<>("radiation_settings.json");
        setRadiationSettingMap(fileManager.loadFile(new TypeToken<RadiationSettingObjectMap>(){}.getType()));
    }

    public static void loadSavedMasses() {
        FileManager<TreeMap<String, MaterialMakeup>> fileManager = new FileManager<>("materials.json");
        setSavedIsotopeMassMap(fileManager.loadFile(new TypeToken<TreeMap<String, MaterialMakeup>>(){}.getType()));
    }

    public static void savePaths() {
        FileManager<PathStructure> fileManager = new FileManager<>("saved_paths.json");
        fileManager.saveObject(new PathStructure(getSavedIrradiationPath(), getSavedDecayPath()));
    }

    public static void saveRadiationSettingMap() {
        FileManager<RadiationSettingObjectMap> fileManager = new FileManager<>("radiation_settings.json");
        fileManager.saveObject(radiationSettingMap);
    }

    public static void saveMasses() {
        FileManager<TreeMap<String, MaterialMakeup>> fileManager = new FileManager<>("materials.json");
        fileManager.saveObject(getSavedMasses());
    }

    public static Element getSavedContainerElement() {
        return savedContainerElement;
    }

    public static void setSavedContainerElement(Element savedContainerElement) {
        DataMemory.savedContainerElement = savedContainerElement;
    }

    public static BigDecimal getSavedIrradiationTime() {
        return savedIrradiationTime;
    }

    public static void setSavedIrradiationTime(BigDecimal savedIrradiationTime) {
        DataMemory.savedIrradiationTime = savedIrradiationTime;
    }

    public static Integer getSavedCycleCount() {
        return savedCycleCount;
    }

    public static void setSavedCycleCount(Integer savedCycleCount) {
        DataMemory.savedCycleCount = savedCycleCount;
    }

    public static BigDecimal getSavedDecayTime() {
        return savedDecayTime;
    }

    public static void setSavedDecayTime(BigDecimal savedDecayTime) {
        DataMemory.savedDecayTime = savedDecayTime;
    }

    public static BigDecimal getSavedContainerThickness() {
        return savedContainerThickness;
    }

    public static void setSavedContainerThickness(BigDecimal savedContainerThickness) {
        DataMemory.savedContainerThickness = savedContainerThickness;
    }

    public static String getSavedIrradiationPath() {
        return savedIrradiationPath;
    }

    public static void setSavedIrradiationPath(String savedIrradiationPath) {
        DataMemory.savedIrradiationPath = savedIrradiationPath;
    }

    public static String getSavedDecayPath() {
        return savedDecayPath;
    }

    public static void setSavedDecayPath(String savedDecayPath) {
        DataMemory.savedDecayPath = savedDecayPath;
    }

    public static String getCurrentMaterialSettingKey() {
        return currentMaterialSettingKey;
    }

    public static void setCurrentMaterialSettingKey(String currentMaterialSettingKey) {
        DataMemory.currentMaterialSettingKey = currentMaterialSettingKey;
        if (getSavedMasses().get(getCurrentMaterialSettingKey()) == null) getSavedMasses().put(getCurrentMaterialSettingKey(), new MaterialMakeup());
    }

    public static String getSelectedMaterialSettingKey() {
        return selectedMaterialSettingKey;
    }

    public static void setSelectedMaterialSettingKey(String selectedMaterialSettingKey) {
        DataMemory.selectedMaterialSettingKey = selectedMaterialSettingKey;
    }

    public static void setSavedIsotopeMassMap(TreeMap<String, MaterialMakeup> savedMasses) {
        DataMemory.savedMasses = savedMasses;
    }
}

