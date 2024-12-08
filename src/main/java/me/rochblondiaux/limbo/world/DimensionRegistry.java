package me.rochblondiaux.limbo.world;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import net.kyori.adventure.nbt.TagStringIO;

@Getter
@Log4j2
public final class DimensionRegistry {

    private Dimension defaultDimension_1_16;
    private Dimension defaultDimension_1_18_2;
    private Dimension dimension_1_20_5;
    private Dimension dimension_1_21;

    private CompoundBinaryTag codec_1_16;
    private CompoundBinaryTag codec_1_18_2;
    private CompoundBinaryTag codec_1_19;
    private CompoundBinaryTag codec_1_19_1;
    private CompoundBinaryTag codec_1_19_4;
    private CompoundBinaryTag codec_1_20;
    private CompoundBinaryTag codec_1_21;
    private CompoundBinaryTag oldCodec;

    public void load(String def) throws IOException {
        // On 1.16-1.16.1 different codec format
        oldCodec = readCodecFile("dimensions/codec_old.snbt");
        codec_1_16 = readCodecFile("dimensions/codec_1_16.snbt");
        codec_1_18_2 = readCodecFile("dimensions/codec_1_18_2.snbt");
        codec_1_19 = readCodecFile("dimensions/codec_1_19.snbt");
        codec_1_19_1 = readCodecFile("dimensions/codec_1_19_1.snbt");
        codec_1_19_4 = readCodecFile("dimensions/codec_1_19_4.snbt");
        codec_1_20 = readCodecFile("dimensions/codec_1_20.snbt");
        codec_1_21 = readCodecFile("dimensions/codec_1_21.snbt");

        defaultDimension_1_16 = getDefaultDimension(def, codec_1_16);
        defaultDimension_1_18_2 = getDefaultDimension(def, codec_1_18_2);

        dimension_1_20_5 = getModernDimension(def, codec_1_20);
        dimension_1_21 = getModernDimension(def, codec_1_21);
    }

    private Dimension getDefaultDimension(String def, CompoundBinaryTag tag) {
        ListBinaryTag dimensions = tag.getCompound("minecraft:dimension_type").getList("value");

        CompoundBinaryTag overWorld = (CompoundBinaryTag) ((CompoundBinaryTag) dimensions.get(0)).get("element");
        CompoundBinaryTag nether = (CompoundBinaryTag) ((CompoundBinaryTag) dimensions.get(2)).get("element");
        CompoundBinaryTag theEnd = (CompoundBinaryTag) ((CompoundBinaryTag) dimensions.get(3)).get("element");

        return switch (def.toLowerCase()) {
            case "overworld" -> new Dimension(0, "minecraft:overworld", overWorld);
            case "the_nether" -> new Dimension(-1, "minecraft:nether", nether);
            case "the_end" -> new Dimension(1, "minecraft:the_end", theEnd);
            default -> {
                log.warn("Undefined dimension type: '{}'. Using THE_END as default", def);
                yield new Dimension(1, "minecraft:the_end", theEnd);
            }
        };
    }

    private Dimension getModernDimension(String def, CompoundBinaryTag tag) {
        return switch (def.toLowerCase()) {
            case "overworld" -> new Dimension(0, "minecraft:overworld", tag);
            case "the_nether" -> new Dimension(2, "minecraft:nether", tag);
            case "the_end" -> new Dimension(3, "minecraft:the_end", tag);
            default -> {
                log.warn("Undefined dimension type: '{}'. Using THE_END as default", def);
                yield new Dimension(3, "minecraft:the_end", tag);
            }
        };
    }

    private CompoundBinaryTag readCodecFile(String resPath) throws IOException {
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(resPath)) {
            if (inputStream == null)
                throw new FileNotFoundException("Cannot find dimension codec file");
            return TagStringIO.get().asCompound(streamToString(inputStream));
        }
    }

    private String streamToString(InputStream in) throws IOException {
        try (BufferedReader bufReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            return bufReader.lines().collect(Collectors.joining("\n"));
        }
    }
}