package net.minecraft.scoreboard;

import lombok.Getter;

public class ScoreObjective {
    private final Scoreboard theScoreboard;
    @Getter
    private final String name;

    /**
     * The ScoreObjectiveCriteria for this objetive
     */
    private final IScoreObjectiveCriteria objectiveCriteria;
    @Getter
    private IScoreObjectiveCriteria.EnumRenderType renderType;
    @Getter
    private String displayName;

    public ScoreObjective(Scoreboard theScoreboardIn, String nameIn, IScoreObjectiveCriteria objectiveCriteriaIn) {
        this.theScoreboard = theScoreboardIn;
        this.name = nameIn;
        this.objectiveCriteria = objectiveCriteriaIn;
        this.displayName = nameIn;
        this.renderType = objectiveCriteriaIn.getRenderType();
    }

    public Scoreboard getScoreboard() {
        return this.theScoreboard;
    }

    public IScoreObjectiveCriteria getCriteria() {
        return this.objectiveCriteria;
    }

    public void setDisplayName(String nameIn) {
        this.displayName = nameIn;
        this.theScoreboard.onObjectiveDisplayNameChanged(this);
    }

    public void setRenderType(IScoreObjectiveCriteria.EnumRenderType type) {
        this.renderType = type;
        this.theScoreboard.onObjectiveDisplayNameChanged(this);
    }
}
