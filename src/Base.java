import rts.PhysicalGameState;
import rts.UnitAction;
import rts.units.Unit;

import java.util.Iterator;
import java.util.List;

public class Base extends BaseUnit {
    public Base(Unit unit, List<UnitAction> actions, PhysicalGameState pgs, int player) {
        super(unit, actions, pgs, player);
    }

    public UnitAction getNextUnitAction() {
        Iterator iter = this.actions.iterator();
        UnitAction action = null;
        int currentAmountOfWorkers = this.getAmountOfWorkers();
        int currentAmountOfWorkersInRessourceZone = this.getAmountOfWorkersInRessourceZone();

        if (MAXIMUM_WORKERS > currentAmountOfWorkers) {
            if (MAXIMUM_WORKES_IN_RESSOURCE_ZONE > currentAmountOfWorkersInRessourceZone) {
                action = (UnitAction) iter.next();
                if (this.player == 0) {
                    do {
                        if (action.getType() == UnitAction.TYPE_PRODUCE && action.getDirection() == UnitAction.DIRECTION_LEFT) {
                            return action;
                        }
                        action = (UnitAction) iter.next();
                    } while (iter.hasNext());
                }
                return action;
            } else {
                while (iter.hasNext()) {
                    action = (UnitAction) iter.next();
                    if (!this.getProduceOutsideOfResourceZone().isEmpty()) {
                        return this.getProduceOutsideOfResourceZone().get(0);
                    }

                }
            }
        } else {
            while (iter.hasNext()) {
                action = (UnitAction) iter.next();
                if (action.getType() == 0) {
                    return action;
                }
            }
        }
        return action;
    }

    public int getAmountOfWorkers() {
        List<Unit> allUnitsInGame = this.pgs.getUnits();
        Iterator iter = allUnitsInGame.iterator();
        int counter = 0;
        Unit unit;
        while (iter.hasNext()) {
            unit = (Unit) iter.next();
            if (unit.getPlayer() == this.player && unit.getType().name == "Worker") {
                counter++;
            }
        }
        return counter;
    }

    public int getAmountOfWorkersInRessourceZone() {
        List<Unit> allUnitsInGame = this.pgs.getUnits();
        Iterator iter = allUnitsInGame.iterator();
        int counter = 0;
        Unit unit;
        while (iter.hasNext()) {
            unit = (Unit) iter.next();
            Worker w = new Worker(unit, this.actions, this.pgs, this.player);
            if (unit.getPlayer() == this.player && unit.getType().name == "Worker" && w.isUnitInRessurceZone()) {
                counter++;
            }
        }
        return counter;
    }

    public List<UnitAction> getProduceOutsideOfResourceZone() {

        return (this.actions.stream()
                .filter(action -> (this.player == 0) ?
                        (action.getType() == UnitAction.TYPE_PRODUCE && (action.getDirection() == UnitAction.DIRECTION_DOWN || action.getDirection() == UnitAction.DIRECTION_RIGHT)) :
                        (action.getType() == UnitAction.TYPE_PRODUCE && (action.getDirection() == UnitAction.DIRECTION_UP || action.getDirection() == UnitAction.DIRECTION_LEFT)))
                .toList());
    }

}
