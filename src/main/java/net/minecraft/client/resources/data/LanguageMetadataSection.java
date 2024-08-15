package net.minecraft.client.resources.data;

import lombok.Getter;
import net.minecraft.client.resources.Language;

import java.util.Collection;

@Getter
public class LanguageMetadataSection implements IMetadataSection {
    private final Collection<Language> languages;

    public LanguageMetadataSection(Collection<Language> p_i1311_1_) {
        this.languages = p_i1311_1_;
    }

}
