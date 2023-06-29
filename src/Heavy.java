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
        Iterator<UnitAction> iter = this.actions.iterator();
        UnitAction action = null;

        while (iter.hasNext()) {
            action = iter.next();
            int[] positionEnemy = getEnemyUnitPosition(UNIT_BASE);
            if (positionEnemy == null) {
                positionEnemy = getEnemyUnitPosition(UNIT_WORKER);
            }
            Random rand = new Random();
            int takeX = rand.nextInt(2);
            int enemyX = positionEnemy[0];
            int enemyY = positionEnemy[1];

            // Check if there are any enemies within the base radius
/*
            if (isEnemyWithinBaseRadius()) {
                // Attack the enemy
                int[] enemyPos = getEnemyNextToMe();
                //Attack enemyPos here
                if (enemyPos == null && !(this.waitAction().isEmpty())) {
                    return this.waitAction().get(0);
                }
                //Attack enemyPos here
                if (enemyPos != null && !this.getAttack().isEmpty()) {
                    return this.getAttack().get(0);
                }
                if (enemyPos != null) {
                    if (this.unit.getX() < enemyPos[0] && !this.getMoveRight().isEmpty()) {
                        return this.getMoveRight().get(0);
                    } else if (this.unit.getX() > enemyPos[0] && !this.getMoveLeft().isEmpty()) {
                        return this.getMoveLeft().get(0);
                    } else if (this.unit.getY() < enemyPos[1] && !this.getMoveDown().isEmpty()) {
                        return this.getMoveDown().get(0);
                    } else if (this.unit.getY() > enemyPos[1] && !this.getMoveUp().isEmpty()) {
                        return this.getMoveUp().get(0);
                    }
                }
            } else {
                // Move back to the base
                int[] basePosition = getUnitPosition(UNIT_BASE);
                int baseX = basePosition[0];
                int baseY = basePosition[1];

                if (this.unit.getX() > baseX && !this.getMoveLeft().isEmpty()) {
                    return this.getMoveLeft().get(0);
                } else if (this.unit.getX() < baseX && !this.getMoveRight().isEmpty()) {
                    return this.getMoveRight().get(0);
                } else if (this.unit.getY() > baseY && !this.getMoveUp().isEmpty()) {
                    return this.getMoveUp().get(0);
                } else if (this.unit.getY() < baseY && !this.getMoveDown().isEmpty()) {
                    return this.getMoveDown().get(0);
                }
            }
*/

            int[] freeDefensePosition = getEnemyUnitPosition(UNIT_BASE);
            int gameWidth = this.pgs.getWidth();
            int gameHeight = this.pgs.getHeight();

            for (int width = 0; width < gameWidth; width = width + 2) {
                int height = gameHeight - width - 1;
                if (this.unit.getX() == width && this.unit.getY() == height) {
                    return this.waitAction().get(0);
                }
                if (this.pgs.getUnitAt(width, height) == null) {

                    freeDefensePosition[0] = width;
                    freeDefensePosition[1] = height;
                    break;
                }
            }

            int baseX = freeDefensePosition[0];
            int baseY = freeDefensePosition[1];

            if (this.unit.getX() > baseX && !this.getMoveLeft().isEmpty()) {
                return this.getMoveLeft().get(0);
            } else if (this.unit.getX() < baseX && !this.getMoveRight().isEmpty()) {
                return this.getMoveRight().get(0);
            } else if (this.unit.getY() > baseY && !this.getMoveUp().isEmpty()) {
                return this.getMoveUp().get(0);
            } else if (this.unit.getY() < baseY && !this.getMoveDown().isEmpty()) {
                return this.getMoveDown().get(0);
            }
            if (!(this.waitAction().isEmpty())) {
                return this.waitAction().get(0);
            }
        }

        return new UnitAction(UnitAction.TYPE_NONE);
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