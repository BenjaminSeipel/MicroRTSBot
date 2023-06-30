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

    //Produce only heavy units for now, maybe produce to a maximum cap of heavy units?
    public String getNextUnitToProduce() {
        //return UNIT_HEAVY;
        //calc amount of heavies needed to build diagonal line
        int maxAmountOfHeavy;
        if(this.pgs.getHeight() <= 16) {
            maxAmountOfHeavy = this.pgs.getHeight() / 2;
        } else {
            maxAmountOfHeavy = (this.pgs.getHeight() / 3) + 1;
        }
        Random random = new Random();
        int randomInt = random.nextInt(6);

        if(getAmountOfHeavies() < maxAmountOfHeavy) {
            if(randomInt < 2) {
                lastUnitProduced = UNIT_LIGHT;
                return UNIT_LIGHT;
            } else if(randomInt == 2) {
                lastUnitProduced = UNIT_RANGED;
                return UNIT_RANGED;
            } else {
                lastUnitProduced = UNIT_HEAVY;
                return UNIT_HEAVY;
            }
        } else {
            if(randomInt < 3) {
                lastUnitProduced = UNIT_LIGHT;
                return UNIT_LIGHT;
            } else {
                lastUnitProduced = UNIT_RANGED;
                return UNIT_RANGED;
            }
        }
/*
        if(randomInt == 0){
            lastUnitProduced = UNIT_HEAVY;
            return UNIT_HEAVY;
        }else if(randomInt == 1){
            lastUnitProduced = UNIT_LIGHT;
            return UNIT_LIGHT;
        }else{
            lastUnitProduced = UNIT_RANGED;
            return UNIT_RANGED;
        }
 */
    }

    public int getAmountOfHeavies() {
        List<Unit> allUnitsInGame = this.pgs.getUnits();
        Iterator iter = allUnitsInGame.iterator();
        int counter = 0;
        Unit unit;
        while (iter.hasNext()) {
            unit = (Unit) iter.next();
            if (unit.getPlayer() == this.player && unit.getType().name == UNIT_HEAVY) {
                counter++;
            }
        }
        return counter;
    }

}
