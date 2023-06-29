import rts.PhysicalGameState;
import rts.UnitAction;
import rts.units.Unit;

import java.util.List;
import java.util.Random;

public class MoveController {
    Unit unit;
    List<UnitAction> actions;
    PhysicalGameState pgs;
    int player;

    public MoveController(Unit unit, List<UnitAction> actions, PhysicalGameState pgs, int player) {
        this.pgs = pgs;
        this.unit = unit;
        this.actions = actions;
        this.player = player;
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

    public List<UnitAction> waitAction() {
        return (this.actions.stream()
                .filter(action -> (action.getType() == UnitAction.TYPE_NONE))
                .toList());

    }

    public UnitAction moveToPosition(int x, int y) {
        Random rand = new Random();
        int takeX = rand.nextInt(2);
        if (takeX == 1) {
            if (this.unit.getX() < x && !this.getMoveRight().isEmpty()) {
                return this.getMoveRight().get(0);
            } else if (this.unit.getX() > x && !this.getMoveLeft().isEmpty()) {
                return this.getMoveLeft().get(0);
            }
        }

        if (this.unit.getY() < y && !this.getMoveDown().isEmpty()) {
            return this.getMoveDown().get(0);
        } else if (this.unit.getY() > y && !this.getMoveUp().isEmpty()) {
            return this.getMoveUp().get(0);
        } else if (this.unit.getX() < x && !this.getMoveRight().isEmpty()) {
            return this.getMoveRight().get(0);
        } else if (this.unit.getX() > x && !this.getMoveLeft().isEmpty()) {
            return this.getMoveLeft().get(0);
        } else {
            return this.waitAction().get(0);
        }
    }

}
