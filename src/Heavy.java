import rts.PhysicalGameState;
import rts.UnitAction;
import rts.units.Unit;

import java.util.List;

public class Heavy extends BaseUnit {

    public Heavy(Unit unit, List<UnitAction> actions, PhysicalGameState pgs, int player) {
        super(unit, actions, pgs, player);
    }


    public UnitAction getNextUnitAction() {
        if (isBaseDown()) {
            return attackEnemy();
        }
        int[] freeDefensePosition = getEnemyUnitPosition(UNIT_BASE);
        int gameHeight;
        int gameWidth;

        gameWidth = this.pgs.getWidth();
        gameHeight = this.pgs.getHeight();
        int n = (this.player == 0) ? 0 : gameWidth / 2;

        for (int width = n; width < gameWidth; width = width + 2) {
            int add = (this.player == 0) ? (gameHeight / 2) * -1 : gameHeight / 2;
            int height = gameHeight - width - 1 + add;
            if (this.unit.getX() == width && this.unit.getY() == height) {
                return this.waitAction().get(0);
            }
            if (this.pgs.getUnitAt(width, height) == null) {

                freeDefensePosition[0] = width;
                freeDefensePosition[1] = height;
                break;
            }
        }

        int defensePositionX = freeDefensePosition[0];
        int defensePositionY = freeDefensePosition[1];

        return this.moveToPosition(defensePositionX, defensePositionY);

    }

    public boolean isBaseDown() {
        return this.getAmountOfUnits(UNIT_BASE) == 0;
    }

}