import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    private static final String ARMOR_CSV_PATH = "C:\\Users\\1N-V4D3R\\Documents\\destinyArmor.csv";

    private static List<ArmorVO> armorVOS;

    private static Set<Map<String, Object>> sortedArmorSet;

    public static void main(String[] args) throws Exception {
        csvToArmorVO();
        clearNecessary();
        sortedArmorSet = new HashSet<>();
        for (ArmorVO armorVO1 : armorVOS) {
            List<ArmorVO> armorVOS2 = getArmorVOSByTypeAndEquip(armorVO1.getEquippable(), armorVO1.getType());
            for (ArmorVO armorVO2 : armorVOS2) {
                compareArmor(armorVO1, armorVO2);
            }
        }
        if (sortedArmorSet.size() == 0) {
            System.out.println("Empty result");
        } else {
            for (Map<String, Object> map : sortedArmorSet) {
                ArmorVO lowerVO = (ArmorVO) map.get("lowerVO");
                ArmorVO higherVO = (ArmorVO) map.get("higherVO");
                int disparity = (int) map.get("disparity");
                if (disparity == 0) {
                    System.out.println(lowerVO.getId() + " Equals to " + higherVO.getId());
                } else {
                    System.out.println(lowerVO.getId() + " lower than " + higherVO.getId() + " disparity:" + disparity);
                }
            }
        }
    }

    private static List<ArmorVO> getArmorVOSByTypeAndEquip(String equippable, String Type) {
        List<ArmorVO> filterVOList = new ArrayList<>();
        for (ArmorVO armorVO : armorVOS) {
            if (StringUtils.equals(armorVO.getEquippable(), equippable) && StringUtils.equals(armorVO.getType(), Type)) {
                //System.out.println("FILTERED:"+armorVO);
                filterVOList.add(armorVO);
            }
        }
        return filterVOList;
    }

    private static void compareArmor(ArmorVO armorVO1, ArmorVO armorVO2) {
        //首先排除相同物品比较
        if (!StringUtils.equals(armorVO1.getId(), armorVO2.getId())) {
            boolean isArtifice1 = StringUtils.equals(armorVO1.getSeasonalMod(), "artifice");
            boolean isArtifice2 = StringUtils.equals(armorVO2.getSeasonalMod(), "artifice");
            if (isArtifice1 == !isArtifice2) {
                //判断出一个是诡计甲一个普通甲
                ArmorVO artificeArmor;
                ArmorVO regularArmor;
                if (isArtifice1) {
                    artificeArmor = armorVO1;
                    regularArmor = armorVO2;
                } else {
                    artificeArmor = armorVO2;
                    regularArmor = armorVO1;
                }
                int mobility = artificeArmor.getMobility() - regularArmor.getMobility();
                int resilience = artificeArmor.getResilience() - regularArmor.getResilience();
                int recovery = artificeArmor.getRecovery() - regularArmor.getRecovery();
                int discipline = artificeArmor.getDiscipline() - regularArmor.getDiscipline();
                int intellect = artificeArmor.getIntellect() - regularArmor.getIntellect();
                int strength = artificeArmor.getStrength() - regularArmor.getStrength();

                Map<String, Integer> disparityMap = new HashMap<>(6);
                disparityMap.put("mobility", mobility);
                disparityMap.put("resilience", resilience);
                disparityMap.put("recovery", recovery);
                disparityMap.put("discipline", discipline);
                disparityMap.put("intellect", intellect);
                disparityMap.put("strength", strength);
                if (mobility >= 0) {
                    disparityMap.remove("mobility");
                }
                if (resilience >= 0) {
                    disparityMap.remove("resilience");
                }
                if (recovery >= 0) {
                    disparityMap.remove("recovery");
                }
                if (discipline >= 0) {
                    disparityMap.remove("discipline");
                }
                if (intellect >= 0) {
                    disparityMap.remove("intellect");
                }
                if (strength >= 0) {
                    disparityMap.remove("strength");
                }
                if (disparityMap.size() == 1) {
                    Map.Entry<String, Integer> entry = disparityMap.entrySet().iterator().next();
                    if (entry.getValue() >= -3) {
                        int disparity = artificeArmor.getTotal() + 3 - regularArmor.getTotal();
                        if (disparity == 0) {
                            handleEquals(artificeArmor, regularArmor);
                        } else {
                            Map<String, Object> map = new HashMap<>(3);
                            //诡计甲只有一个属性劣于普通甲，且不超过诡计甲给的3点，也算全面优于普通甲
                            map.put("lowerVO", regularArmor);
                            map.put("higherVO", artificeArmor);
                            map.put("disparity", disparity);
                            sortedArmorSet.add(map);
                        }
                    }
                } else if (disparityMap.size() == 0) {
                    int disparity = artificeArmor.getTotal() + 3 - regularArmor.getTotal();
                    if (disparity == 0) {
                        handleEquals(artificeArmor, regularArmor);
                    } else {
                        //全面优于普通甲
                        Map<String, Object> map = new HashMap<>(3);
                        map.put("lowerVO", regularArmor);
                        map.put("higherVO", artificeArmor);
                        map.put("disparity", disparity);
                        sortedArmorSet.add(map);
                    }
                }
                if (mobility <= -3 && resilience <= -3 && recovery <= -3 && discipline <= -3 && intellect <= -3 && strength <= -3) {
                    //全面优于诡计甲
                    Map<String, Object> map = new HashMap<>(3);
                    map.put("lowerVO", artificeArmor);
                    map.put("higherVO", regularArmor);
                    map.put("disparity", regularArmor.getTotal() - artificeArmor.getTotal() + 3);
                    sortedArmorSet.add(map);
                }
            } else {
                //正常护甲比较
                int mobility = armorVO1.getMobility() - armorVO2.getMobility();
                int resilience = armorVO1.getResilience() - armorVO2.getResilience();
                int recovery = armorVO1.getRecovery() - armorVO2.getRecovery();
                int discipline = armorVO1.getDiscipline() - armorVO2.getDiscipline();
                int intellect = armorVO1.getIntellect() - armorVO2.getIntellect();
                int strength = armorVO1.getStrength() - armorVO2.getStrength();

                if (mobility == 0 && resilience == 0 && recovery == 0
                        && discipline == 0 && intellect == 0 && strength == 0) {
                    //相等护甲排序以去重
                    handleEquals(armorVO1, armorVO2);
                } else if (mobility <= 0 && resilience <= 0 && recovery <= 0
                        && discipline <= 0 && intellect <= 0 && strength <= 0) {
                    Map<String, Object> map = new HashMap<>(3);
                    //1号小于2号
                    map.put("lowerVO", armorVO1);
                    map.put("higherVO", armorVO2);
                    map.put("disparity", armorVO2.getTotal() - armorVO1.getTotal());
                    sortedArmorSet.add(map);
                } else if (mobility >= 0 && resilience >= 0 && recovery >= 0
                        && discipline >= 0 && intellect >= 0 && strength >= 0) {
                    Map<String, Object> map = new HashMap<>(3);
                    //2号小于1号
                    map.put("lowerVO", armorVO2);
                    map.put("higherVO", armorVO1);
                    map.put("disparity", armorVO1.getTotal() - armorVO2.getTotal());
                    sortedArmorSet.add(map);
                }
            }
        }
    }

    private static void handleEquals(ArmorVO armorVO1, ArmorVO armorVO2) {
        Map<String, Object> map = new HashMap<>(3);
        ArmorVO vo1;
        ArmorVO vo2;
        if (StringUtils.compare(armorVO1.getId(),armorVO2.getId()) > 0) {
            vo1 = armorVO1;
            vo2 = armorVO2;
        } else {
            vo1 = armorVO2;
            vo2 = armorVO1;
        }
        //护甲属性一致
        map.put("lowerVO", vo1);
        map.put("higherVO", vo2);
        map.put("disparity", 0);
        sortedArmorSet.add(map);
    }

    private static void clearNecessary() {
        Iterator<ArmorVO> iterator = armorVOS.iterator();
        while (iterator.hasNext()) {
            ArmorVO armorVO = iterator.next();
            String type = armorVO.getType();
            String tier = armorVO.getTier();
            if (StringUtils.equals(type, "Hunter Cloak") || StringUtils.equals(type, "Titan Mark")
                    || StringUtils.equals(type, "Warlock Bond") || !StringUtils.equals(tier, "Legendary")) {
                //System.out.println("REMOVED:"+armorVO);
                iterator.remove();
            }
        }
    }

    private static void csvToArmorVO() throws IOException {
        String fileName = ARMOR_CSV_PATH;
        Reader reader = Files.newBufferedReader(Paths.get(fileName));

        HeaderColumnNameMappingStrategy<ArmorVO> strategy = new HeaderColumnNameMappingStrategy<>();
        strategy.setType(ArmorVO.class);

        CsvToBean<ArmorVO> csvToBean = new CsvToBeanBuilder<ArmorVO>(reader)
                .withMappingStrategy(strategy)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
        armorVOS = csvToBean.parse();
    }
}
