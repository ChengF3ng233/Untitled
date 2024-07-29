package net.minecraft.world.storage;

import net.minecraft.client.AnvilConverterException;
import net.minecraft.util.IProgressUpdate;

import java.util.List;

public interface ISaveFormat {
    /**
     * Returns the name of the save format.
     */
    String getName();

    /**
     * Returns back a loader for the specified save directory
     */
    ISaveHandler getSaveLoader(String saveName, boolean storePlayerdata);

    List<SaveFormatComparator> getSaveList() throws AnvilConverterException;

    void flushCache();

    /**
     * Returns the world's WorldInfo object
     */
    WorldInfo getWorldInfo(String saveName);

    boolean isNewLevelIdAcceptable(String saveName);

    /**
     * @param saveName The current save's name
     * @args: Takes one argument - the name of the directory of the world to delete. @desc: Delete the world by deleting
     * the associated directory recursively.
     */
    boolean deleteWorldDirectory(String saveName);

    /**
     * Renames the world by storing the new name in level.dat. It does *not* rename the directory containing the world
     * data.
     */
    void renameWorld(String dirName, String newName);

    boolean isConvertible(String saveName);

    /**
     * gets if the map is old chunk saving (true) or McRegion (false)
     */
    boolean isOldMapFormat(String saveName);

    /**
     * converts the map to mcRegion
     */
    boolean convertMapFormat(String filename, IProgressUpdate progressCallback);

    /**
     * Return whether the given world can be loaded.
     *
     * @param saveName The current save's name
     */
    boolean canLoadWorld(String saveName);
}
