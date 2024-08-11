package net.minecraft.world;

public enum EnumDifficulty {
    PEACEFUL(0, "options.difficulty.peaceful"),
    EASY(1, "options.difficulty.easy"),
    NORMAL(2, "options.difficulty.normal"),
    HARD(3, "options.difficulty.hard");

    private static final EnumDifficulty[] difficultyEnums = new EnumDifficulty[values().length];

    static {
        for (EnumDifficulty enumdifficulty : values()) {
            difficultyEnums[enumdifficulty.difficultyId] = enumdifficulty;
        }
    }

    private final int difficultyId;
    private final String difficultyResourceKey;

    EnumDifficulty(int difficultyIdIn, String difficultyResourceKeyIn) {
        this.difficultyId = difficultyIdIn;
        this.difficultyResourceKey = difficultyResourceKeyIn;
    }

    public static EnumDifficulty getDifficultyEnum(int p_151523_0_) {
        return difficultyEnums[p_151523_0_ % difficultyEnums.length];
    }

    public int getDifficultyId() {
        return this.difficultyId;
    }

    public String getDifficultyResourceKey() {
        return this.difficultyResourceKey;
    }
}
