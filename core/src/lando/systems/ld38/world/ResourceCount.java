package lando.systems.ld38.world;

/**
 * Created by Brian on 4/23/2017.
 */
public class ResourceCount {
    public int food = 0;
    public int sand = 0;
    public int iron = 0;
    public int gold = 0;
    public int wood = 0;

    public ResourceCount() {}

    public ResourceCount(int food, int sand, int iron, int gold, int wood) {
        this.food = food;
        this.sand = sand;
        this.iron = iron;
        this.gold = gold;
        this.wood = wood;
    }

    public boolean hasEnough(ResourceCount required) {
        return food >= required.food && sand >= required.sand && iron >= required.iron
                && gold >= required.gold && wood >= required.wood;
    }

    public String getRequired(ResourceCount required) {
        if (food < required.food) {
            return "Need " + required.food + " food";
        }

        if (sand < required.sand) {
            return "Need " + required.sand + " sand";
        }

        if (iron < required.iron) {
            return "Need " + required.iron + " iron";
        }

        if (gold < required.gold) {
            return "Need " + required.gold + " gold";
        }

        if (wood < required.wood) {
            return "Need " + required.wood + " wood";
        }

        return "You need more resources";
    }

    public void remove(ResourceCount resources) {
        food -= resources.food;
        sand -= resources.sand;
        iron -= resources.iron;
        gold -= resources.gold;
        wood -= resources.wood;
    }

    public void add(ResourceCount resources) {
        food += resources.food;
        sand += resources.sand;
        iron += resources.iron;
        gold += resources.gold;
        wood += resources.wood;
    }
}
