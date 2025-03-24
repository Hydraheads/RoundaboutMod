package net.hydra.jojomod.util;

import com.google.gson.Gson;
import net.hydra.jojomod.Roundabout;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BlockBlacklist {
    private static final Gson GSON = new Gson();
    public static BlacklistData data = new BlacklistData();
    private static boolean shouldBlacklistBlocks = true;
    private static final Pattern tagKeyPattern = Pattern.compile("TagKey\\[(\\S+)\\s*/");

    /**
     * Loads the block blacklist for D4C worlds.
     *
     * @param path Defines the Config path.
     * @param filename Defines the Filename without ".json" (i.e. "block_blacklist")
     * @return Returns true if it loads successfully, otherwise returns false and uses the default block blacklist.
     */
    public static boolean load(Path path, String filename)
    {
        try {
            Path blacklistPath = path.resolve(filename+".json");

            if (!Files.exists(blacklistPath))
            {
                // writes the default data to the file if it doesn't already exist
                Files.write(blacklistPath, GSON.toJson(data).getBytes());
            }
            else {
                data = GSON.fromJson(Files.newBufferedReader(blacklistPath), BlacklistData.class);
                if (data.blockTags == null || data.blockIdentifiers == null)
                {
                    shouldBlacklistBlocks = false;
                    throw new IOException("Missing field \"blockTags\" or \"blockIdentifiers\" in \"" + filename + ".json\"");
                }
            }
        }
        catch (IOException e)
        {
            Roundabout.LOGGER.warn("Failed to load blacklist \"{}.json\" (error: [\"{}\"])", filename, e.toString());
            e.printStackTrace();
            return false;
        }

        Roundabout.LOGGER.debug("Loaded blacklist \"{}.json\" successfully.", filename);
        return true;
    }

    public static class BlacklistData
    {
        /** Holds the strings of the tags, identified with #[namespace]:(tag). Remember to serialize!
         * Supports wildcards, such as "#*:*_ore" to block all ores. */
        public Set<String> blockTags = new HashSet<>(List.of("#*:*_ore"));
        /** Holds the strings of the blocks, identified with [namespace]:(name). Remember to serialize!
         * Supports wildcards, such as "*:*_ore" to block all ores. */
        // *:*_block *should* be for stuff like minecraft:diamond_block, but it might be risky.
        public Set<String> blockIdentifiers = new HashSet<>(List.of("*:*_ore", "*:*_block"));
    }

    public static boolean canBlacklistBlocks()
    { return shouldBlacklistBlocks; }

    /**
     * Determines if a specified block is acceptable in the block blacklist.
     * @param block The block to check
     * @return Returns true if the block is acceptable and is allowed given the rules of the blacklist, otherwise returns false.
     */
    public static boolean isBlockAcceptable(BlockState block)
    {
        if (!shouldBlacklistBlocks || (data.blockIdentifiers == null || data.blockTags == null))
            return true;

        for (String s : data.blockIdentifiers)
        {
            if (BuiltInRegistries.BLOCK.getKey(block.getBlock()).toString().matches(wildcardToRegex(s)))
                return false;
        }

        for (String s : data.blockTags)
        {
            for (TagKey<Block> tag : block.getTags().toList())
            {
                Matcher matcher = tagKeyPattern.matcher(tag.toString());
                if (matcher.find())
                {
                    String extractedTag = matcher.group(1);
                    if (extractedTag.matches(wildcardToRegex(s)))
                        return false;
                }
            }
        }

        return true;
    }

    public static String wildcardToRegex(String wildcard) {
        return "^" + wildcard
                .replace(".", "\\.")
                .replace("*", ".*")
                + "$";
    }
}