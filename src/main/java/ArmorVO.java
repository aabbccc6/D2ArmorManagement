import com.opencsv.bean.CsvBindByName;

public class ArmorVO {
    @CsvBindByName(column = "Id")
    private String id;

    @CsvBindByName(column = "Name")
    private String name;

    @CsvBindByName(column = "Tier")
    private String tier;

    @CsvBindByName(column = "Type")
    private String type;

    @CsvBindByName(column = "Equippable")
    private String equippable;

    @CsvBindByName(column = "Seasonal Mod")
    private String seasonalMod;

    @CsvBindByName(column = "Mobility (Base)")
    private Integer mobility;

    @CsvBindByName(column = "Resilience (Base)")
    private Integer resilience;

    @CsvBindByName(column = "Recovery (Base)")
    private Integer recovery;

    @CsvBindByName(column = "Discipline (Base)")
    private Integer discipline;

    @CsvBindByName(column = "Intellect (Base)")
    private Integer intellect;

    @CsvBindByName(column = "Strength (Base)")
    private Integer strength;

    @CsvBindByName(column = "Total (Base)")
    private Integer total;

    public ArmorVO() {
    }

    @Override
    public String toString() {
        return "ArmorVO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", tier='" + tier + '\'' +
                ", type='" + type + '\'' +
                ", equippable='" + equippable + '\'' +
                ", seasonalMod='" + seasonalMod + '\'' +
                ", Mobility=" + mobility +
                ", Resilience=" + resilience +
                ", Recovery=" + recovery +
                ", Discipline=" + discipline +
                ", Intellect=" + intellect +
                ", Strength=" + strength +
                ", Total=" + total +
                '}';
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEquippable() {
        return equippable;
    }

    public void setEquippable(String equippable) {
        this.equippable = equippable;
    }

    public String getSeasonalMod() {
        return seasonalMod;
    }

    public void setSeasonalMod(String seasonalMod) {
        this.seasonalMod = seasonalMod;
    }

    public Integer getMobility() {
        return mobility;
    }

    public void setMobility(Integer mobility) {
        this.mobility = mobility;
    }

    public Integer getResilience() {
        return resilience;
    }

    public void setResilience(Integer resilience) {
        this.resilience = resilience;
    }

    public Integer getRecovery() {
        return recovery;
    }

    public void setRecovery(Integer recovery) {
        this.recovery = recovery;
    }

    public Integer getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Integer discipline) {
        this.discipline = discipline;
    }

    public Integer getIntellect() {
        return intellect;
    }

    public void setIntellect(Integer intellect) {
        this.intellect = intellect;
    }

    public Integer getStrength() {
        return strength;
    }

    public void setStrength(Integer strength) {
        this.strength = strength;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
