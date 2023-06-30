import rts.PhysicalGameState;
import rts.ResourceUsage;
import rts.UnitAction;
import rts.units.Unit;
import rts.units.UnitTypeTable;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Barracks extends BaseUnit {

    private static String lastUnitProduced = null;

    public Barracks(Unit unit, List<UnitAction> actions, PhysicalGameState pgs, int player) {
        super(unit, actions, pgs, player);
        int needsAdditionalUnit = (this.pgs.getWidth() % 2 == 0) ? -1 : 0;
        MAX_AMOUNT_OF_HEAVY_UNITS = this.pgs.getWidth() / 4 + needsAdditionalUnit;
    }

    //Generally just produce light units
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

    public String getNextUnitToProduce() {
        Random random = new Random();
        int randomInt = random.nextInt(6);

        if (getAmountOfUnits(UNIT_HEAVY) < MAX_AMOUNT_OF_HEAVY_UNITS) {
            if (randomInt < 2) {
                lastUnitProduced = UNIT_LIGHT;
                return UNIT_LIGHT;
            } else if (randomInt == 2) {
                lastUnitProduced = UNIT_RANGED;
                return UNIT_RANGED;
            } else {
                lastUnitProduced = UNIT_HEAVY;
                return UNIT_HEAVY;
            }
        } else {
            if (randomInt < 3) {
                lastUnitProduced = UNIT_LIGHT;
                return UNIT_LIGHT;
            } else {
                lastUnitProduced = UNIT_RANGED;
                return UNIT_RANGED;
            }
        }
    }

}
