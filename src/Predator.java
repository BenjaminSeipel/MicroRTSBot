import ai.core.AI;
import ai.core.AIWithComputationBudget;
import ai.core.ParameterSpecification;
import rts.*;
import rts.units.Unit;
import rts.units.UnitType;
import rts.units.UnitTypeTable;
import util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Predator extends AIWithComputationBudget {

    UnitTypeTable m_utt;
    //int counter = 0;
    //int counter1 = 0;
    //This is the default constructor that microRTS will call:

    public Predator(UnitTypeTable utt) {

        super(100, 100);

        m_utt = utt;

    }

    // This will be called by microRTS when it wants to create new instances of this bot (e.g., to play multiple games).

    public AI clone() {

        return new Predator(m_utt);

    }



    // This will be called once at the beginning of each new game:    

    public void reset() {

    }

    // Called by microRTS at each game cycle.

    // Returns the action the bot wants to execute.

    public PlayerAction getAction(int player, GameState gs) {
        try {

            if (!gs.canExecuteAnyAction(player)) return new PlayerAction();

            PhysicalGameState pgs = gs.getPhysicalGameState();
            PlayerActionGenerator pag = new PlayerActionGenerator(gs, player);
            PlayerAction pa = new PlayerAction();

            // Gets all Choices from Generator
            List<Pair<Unit,List<UnitAction>>> unitChoices = pag.getChoices();

            boolean consistent = false;
            do {
                // Iterates through all units from the player that can perform an action
                for(Pair<Unit,List<UnitAction>> selectedUnitWithActions: unitChoices){
                    Unit selectedUnit = selectedUnitWithActions.m_a;
                    List<UnitAction>  actionsOfSelectedUnit = selectedUnitWithActions.m_b;
                    UnitType type = selectedUnit.getType();
                    {
                    switch(type.name){
                            case "Base":
                                // 3 is just an index, should be replaced
                               UnitAction unitAction = actionsOfSelectedUnit.get(3);
                                break;
                            case "Barracks":
                                break;
                            case "Worker":
                                break;
                            case "Light":
                                break;
                            case "Heavy":
                                break;
                            case "Ranged":
                                break;
                        }
                    }
                }

                Unit base = unitChoices.get(0).m_a;
                UnitAction unitAction = unitChoices.get(0).m_b.get(3);

                ResourceUsage r2 = unitAction.resourceUsage(base, pgs);

                if (pa.getResourceUsage().consistentWith(r2, gs)) {

                    pa.getResourceUsage().merge(r2);
                    pa.addUnitAction(base, unitAction);

                    consistent = true;
                }

            } while (!consistent);

            return pa;
        }catch(Exception e) {
            return new PlayerAction();
        }
    }



    // This will be called by the microRTS GUI to get the

    // list of parameters that this bot wants exposed

    // in the GUI.

    public List<ParameterSpecification> getParameters()

    {

        return new ArrayList<>();

    }

}