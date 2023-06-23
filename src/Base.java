import rts.PhysicalGameState;
import rts.Player;
import rts.UnitAction;
import rts.units.Unit;

import java.util.Iterator;
import java.util.List;

public class Base {
    private int MAXIMUM_WORKERS = 4;
    private Unit unit;
    private List<UnitAction> actions;
    private PhysicalGameState pgs;
    private int player;
    public Base(Unit unit, List<UnitAction> actions, PhysicalGameState pgs, int player){
        this.pgs = pgs;
        this.unit = unit;
        this.actions = actions;
        this.player = player;
    }

    public UnitAction getNextUnitAction() {
        Iterator iter = this.actions.iterator();
        UnitAction action = null;
        int currentAmountOfWorkers =  this.getAmountOfWorkers();

        if(MAXIMUM_WORKERS> currentAmountOfWorkers){
            action = (UnitAction) iter.next();
            return action;
        }else{
            while(iter.hasNext()){
                action = (UnitAction) iter.next();
                if(action.getType()==0){
                    return action;
                }
            }
        }
        return action;
    }

    public int getAmountOfWorkers(){
        List<Unit> allUnitsInGame = this.pgs.getUnits();
        Iterator iter = allUnitsInGame.iterator();
        int counter = 0;
        Unit unit;
        while(iter.hasNext()){
            unit =(Unit) iter.next();
            if(unit.getPlayer() == this.player && unit.getType().name=="Worker"){
                counter++;
            }
        }
        return counter;
    }


}
