import rts.PhysicalGameState;
import rts.UnitAction;
import rts.units.Unit;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Heavy extends BaseUnit {

    final int MAX_RADIUS = 10;

    public Heavy(Unit unit, List<UnitAction> actions, PhysicalGameState pgs, int player) {
        super(unit, actions, pgs, player);
    }


    public UnitAction getNextUnitAction() {

        int[] positionEnemy = getEnemyUnitPosition(UNIT_BASE);
        if (positionEnemy == null) {
            positionEnemy = getEnemyUnitPosition(UNIT_WORKER);
        }
        Random rand = new Random();
        int takeX = rand.nextInt(2);
        int enemyX = positionEnemy[0];
        int enemyY = positionEnemy[1];

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

    private boolean isEnemyWithinBaseRadius() {
        int[] basePosition = getUnitPosition(UNIT_BASE);
        System.out.println("Base Position: " + basePosition[0] + ", " + basePosition[1] + "\n");
        int baseX = basePosition[0];
        int baseY = basePosition[1];

        List<Unit> units = this.pgs.getUnits();
        for (Unit unit : units) {
            if (unit.getPlayer() != this.player) {
                int unitX = unit.getX();
                int unitY = unit.getY();
                if (unitX >= baseX - MAX_RADIUS && unitX <= baseX + MAX_RADIUS && unitY >= baseY - MAX_RADIUS && unitY <= baseY + MAX_RADIUS) {
                    return true;
                }
            }
        }
        return false;
    }


    private int[] getClosestEnemyWithinProtectionArea() {
        int[] basePosition = getUnitPosition(UNIT_BASE);
        System.out.println("Base Position: " + basePosition[0] + ", " + basePosition[1] + "\n");
        int baseX = basePosition[0];
        int baseY = basePosition[1];

        List<Unit> units = this.pgs.getUnits();
        for (Unit unit : units) {
            if (unit.getPlayer() != this.player) {
                int unitX = unit.getX();
                int unitY = unit.getY();
                if (unitX >= baseX - MAX_RADIUS && unitX <= baseX + MAX_RADIUS && unitY >= baseY - MAX_RADIUS && unitY <= baseY + MAX_RADIUS) {
                    return new int[]{unitX, unitY};
                }
            }
        }
        return null;
    }

}