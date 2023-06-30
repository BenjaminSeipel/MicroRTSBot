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

        int[] board = {this.pgs.getWidth(), this.pgs.getHeight()};
        int[] basePosition = getUnitPosition(UNIT_BASE);
        int[] position = new int[2];
        int[] defendingPos = new int[2];
        int[] enemyBase = getEnemyUnitPosition(UNIT_BASE);

        while (iter.hasNext()) {
            action = iter.next();
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
            if (basePosition[0] < enemyBase[0] && basePosition[1] < enemyBase[1]) {
                if (board[0] <= 16 && board[1] <= 16) {
                    position[0] = board[0] / 2 + 1;
                    position[1] = board[1] / 2 + 1;
                } else {
                    position[0] = board[0] / 3;
                    position[1] = board[1] / 3;
                }
                for (int width = 0; width < position[0]; width += 2) {
                    int height = position[1] - width - 1;
                    if (this.unit.getX() == width && this.unit.getY() == height) {
                        return this.waitAction().get(0);
                    }
                    if (this.pgs.getUnitAt(width, height) == null) {
                        defendingPos[0] = width;
                        defendingPos[1] = height;
                        break;
                    }
                }

            } else {
                //TODO heavies dont set up the right way when bot in lower right corner
                if (board[0] > 16 && board[1] > 16) {
                    position[0] = board[0] - (board[0] / 2 + 1);
                    position[1] = board[1] - (board[1] / 2 + 1);
                } else {
                    position[0] = board[0] - (board[0] / 3);
                    position[1] = board[1] - (board[1] / 3);
                }
                for (int width = position[0]; width > 0; width -= 2) {
                    int height = position[1];
                    if (this.unit.getX() == width && this.unit.getY() == height) {
                        return this.waitAction().get(0);
                    }
                    if (this.pgs.getUnitAt(width, height) == null) {
                        defendingPos[0] = width;
                        defendingPos[1] = height;
                        break;
                    }
                }
            }
            //if enemy unit in close proximity of defendingPos -> attack
            int[] closestEnemy = getClosestEnemyToPos(defendingPos);
            if(Math.abs(closestEnemy[0]-defendingPos[0]) + Math.abs(closestEnemy[1])-defendingPos[1] <= this.unit.getAttackRange()) {
                return this.getAttack().get(0);
            }else if(Math.abs(closestEnemy[0]-defendingPos[0]) + Math.abs(closestEnemy[1])-defendingPos[1] <= 2) {
                int attackX = Math.abs(closestEnemy[0]-defendingPos[0]);
                int attackY = Math.abs(closestEnemy[1]-defendingPos[1]);
                return this.moveToPosition(attackX, attackY);
            }

            return this.moveToPosition(defendingPos[0], defendingPos[1]);

            /*if (this.unit.getX() > baseX && !this.getMoveLeft().isEmpty()) {
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
            }*/
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

    private int[] getClosestEnemyToPos(int[] pos) {
        int[] enemyPos = new int[2];
        List<Unit> units = this.pgs.getUnits();
        for(Unit u : units) {
            if(u.getPlayer() != this.player) {
                int uX = u.getX();
                int uY = u.getY();
                if(Math.abs(uX-pos[0]) + Math.abs(uY-pos[1]) <= Math.abs(enemyPos[0]-pos[0]) + Math.abs(enemyPos[1]-pos[1])) {
                    enemyPos[0] = uX;
                    enemyPos[1] = uY;
                }
            }
        }
        return enemyPos;
    }

    /*
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
     */
}