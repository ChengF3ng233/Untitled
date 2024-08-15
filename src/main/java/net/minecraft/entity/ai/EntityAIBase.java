package net.minecraft.entity.ai;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class EntityAIBase {
    /**
     * A bitmask telling which other tasks may not run concurrently. The test is a simple bitwise AND - if it yields
     * zero, the two tasks may run concurrently, if not - they must run exclusively from each other.
     * -- SETTER --
     *  Sets a bitmask telling which other tasks may not run concurrently. The test is a simple bitwise AND - if it
     *  yields zero, the two tasks may run concurrently, if not - they must run exclusively from each other.
     * -- GETTER --
     *  Get a bitmask telling which other tasks may not run concurrently. The test is a simple bitwise AND - if it yields
     *  zero, the two tasks may run concurrently, if not - they must run exclusively from each other.


     */
    private int mutexBits;

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public abstract boolean shouldExecute();

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting() {
        return this.shouldExecute();
    }

    /**
     * Determine if this AI Task is interruptible by a higher (= lower value) priority task. All vanilla AITask have
     * this value set to true.
     */
    public boolean isInterruptible() {
        return true;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
    }

    /**
     * Resets the task
     */
    public void resetTask() {
    }

    /**
     * Updates the task
     */
    public void updateTask() {
    }

}
