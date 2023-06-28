import rts.PhysicalGameState;
import rts.UnitAction;
import rts.units.Unit;

import java.util.Iterator;
import java.util.List;

public class BaseUnit {

    int MAXIMUM_WORKERS = 10;
    int MAXIMUM_WORKES_IN_RESSOURCE_ZONE = 2;
    int MAX_AMOUNT_OF_BARRACK = 1;
    int MAX_AMOUNT_OF_HEAVY_UNITS = 2;

    Unit unit;
    List<UnitAction> actions;
    PhysicalGameState pgs;
    int player;

    public BaseUnit(Unit unit, List<UnitAction> actions, PhysicalGameState pgs, int player) {
        this.pgs = pgs;
        this.unit = unit;
        this.actions = actions;
        this.player = player;
    }



    int[] getEnemyNextToMe(){
        List<Unit> units = this.pgs.getUnits();
        for (Unit unit : units) {
            if (unit.getPlayer() != this.player) {
                int unitX = unit.getX();
                int unitY = unit.getY();
                if (unitX == this.unit.getX() && unitY == this.unit.getY()) {
                    return new int[]{unitX, unitY};
                }
            }
        }
        return null;
    }

    public int[] getBasePosition() {
        List<Unit> units = this.pgs.getUnits();
        Unit Base = null;
        Unit unit = null;
        Iterator<Unit> iter = units.iterator();
        while (iter.hasNext()) {
            unit = iter.next();
            if (unit.getType().name == "Base" && unit.getPlayer() == this.player) {
                Base = unit;
                break;
            }
        }

        if (Base == null) {
            // need new base, old one is destroyed(if possible)
            return null;
        }


        int[] position = new int[2];
        position[0] = Base.getX();
        position[1] = Base.getY();
        return position;
    }
    public List<UnitAction> getMoveUp() {
        return this.actions.stream()
                .filter(action -> (action.getType() == UnitAction.TYPE_MOVE && action.getDirection() == UnitAction.DIRECTION_UP))
                .toList();
    }

    public List<UnitAction> getMoveDown() {
        return this.actions.stream()
                .filter(action -> (action.getType() == UnitAction.TYPE_MOVE && action.getDirection() == UnitAction.DIRECTION_DOWN))
                .toList();
    }

    public List<UnitAction> getMoveRight() {
        return this.actions.stream()
                .filter(action -> (action.getType() == UnitAction.TYPE_MOVE && action.getDirection() == UnitAction.DIRECTION_RIGHT))
                .toList();
    }

    public List<UnitAction> getMoveLeft() {
        return this.actions.stream()
                .filter(action -> (action.getType() == UnitAction.TYPE_MOVE && action.getDirection() == UnitAction.DIRECTION_LEFT))
                .toList();
    }

    public List<UnitAction> getReturn() {
        return (this.actions.stream()
                .filter(action -> (action.getType() == UnitAction.TYPE_RETURN))
                .toList());
    }

    public List<UnitAction> getAttack() {
        return (this.actions.stream()
                .filter(action -> (action.getType() == UnitAction.TYPE_ATTACK_LOCATION))
                .toList());
    }


    public List<UnitAction> waitAction() {
        return (this.actions.stream()
                .filter(action -> (action.getType() == UnitAction.TYPE_NONE))
                .toList());

    }

    public int[] getEnemyBasePosition() {
        List<Unit> units = this.pgs.getUnits();
        Unit Base = null;
        Unit unit = null;
        Iterator iter = units.iterator();
        while (iter.hasNext()) {
            unit = (Unit) iter.next();
            if (unit.getType().name == "Base" && unit.getPlayer() != this.player) {
                Base = unit;
                break;
            }
        }

        if (Base == null) {
            // need new base, old one is destroyed(if possible)
            return null;
        }


        int[] position = new int[2];
        position[0] = Base.getX();
        position[1] = Base.getY();
        return position;
    }

    public int[] getEnemyWorkerPosition() {
        List<Unit> units = this.pgs.getUnits();
        Unit Base = null;
        Unit unit = null;
        Iterator iter = units.iterator();
        while (iter.hasNext()) {
            unit = (Unit) iter.next();
            if (unit.getType().name == "Worker" && unit.getPlayer() != this.player) {
                Base = unit;
                break;
            }
        }

        if (Base == null) {
            // need new base, old one is destroyed(if possible)
            return null;
        }


        int[] position = new int[2];
        position[0] = Base.getX();
        position[1] = Base.getY();
        return position;
    }
}
