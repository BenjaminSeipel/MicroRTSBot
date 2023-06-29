import rts.PhysicalGameState;
import rts.ResourceUsage;
import rts.UnitAction;
import rts.units.Unit;
import rts.units.UnitTypeTable;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Barracks extends BaseUnit {


    public static final String UNIT_HEAVY = "Heavy";
    public static final String UNIT_LIGHT = "Light";
    public static final String UNIT_RANGED = "Ranged";

    private static String lastUnitProduced = null;

    public Barracks(Unit unit, List<UnitAction> actions, PhysicalGameState pgs, int player) {
        super(unit, actions, pgs, player);
    }

    //Generally just procuce light units
    public UnitAction getNextUnitAction() {
        Iterator<UnitAction> iter = this.actions.iterator();
        UnitAction action = null;
        String unitProductionTarget = getNextUnitToProduce();
        while (iter.hasNext()) {
            action = iter.next();
            if (action.getType() == UnitAction.TYPE_PRODUCE) {
                String unitType = action.getUnitType().name;
                if (unitType.equals(unitProductionTarget)) {
                    return action;
                }
            }
        }
        if (action == null) {
            return new UnitAction(UnitAction.TYPE_NONE);
        }
        ResourceUsage barrackUsage = action.resourceUsage(unit, pgs);
        if (barrackUsage.getResourcesUsed(player) > 0) {
            return new UnitAction(UnitAction.TYPE_NONE);
        } else {
            return action;
        }
    }

    //Produce only heavy units for now, maybe produce to a maximum cap of heavy units?
    public static String getNextUnitToProduce() {
        return UNIT_LIGHT;
        /*
        Random random = new Random();
        int randomInt = random.nextInt(3);
        if(randomInt == 0){
            lastUnitProduced = UNIT_HEAVY;
            return UNIT_HEAVY;
        }else if(randomInt == 1){
            lastUnitProduced = UNIT_LIGHT;
            return UNIT_LIGHT;
        }else{
            lastUnitProduced = UNIT_RANGED;
            return UNIT_RANGED;
        }*/
    }

    //Probably unnecessary, as barracks can only produce
    public List<UnitAction> getProduce() {
        return this.actions.stream()
                .filter(action -> (action.getType() == UnitAction.TYPE_PRODUCE))
                .toList();
    }


}
