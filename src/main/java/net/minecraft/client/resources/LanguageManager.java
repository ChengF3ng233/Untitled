package net.minecraft.client.resources;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.client.resources.data.LanguageMetadataSection;
import net.minecraft.util.StringTranslate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

public class LanguageManager implements IResourceManagerReloadListener {
    protected static final Locale currentLocale = new Locale();
    private static final Logger logger = LogManager.getLogger();
    private final IMetadataSerializer theMetadataSerializer;
    private String currentLanguage;
    private final Map<String, Language> languageMap = Maps.newHashMap();

    public LanguageManager(IMetadataSerializer theMetadataSerializerIn, String currentLanguageIn) {
        this.theMetadataSerializer = theMetadataSerializerIn;
        this.currentLanguage = currentLanguageIn;
        I18n.setLocale(currentLocale);
    }

    public void parseLanguageMetadata(List<IResourcePack> resourcesPacks) {
        this.languageMap.clear();

        for (IResourcePack iresourcepack : resourcesPacks) {
            try {
                LanguageMetadataSection languagemetadatasection = iresourcepack.getPackMetadata(this.theMetadataSerializer, "language");

                if (languagemetadatasection != null) {
                    for (Language language : languagemetadatasection.getLanguages()) {
                        if (!this.languageMap.containsKey(language.getLanguageCode())) {
                            this.languageMap.put(language.getLanguageCode(), language);
                        }
                    }
                }
            } catch (RuntimeException runtimeexception) {
                logger.warn("Unable to parse metadata section of resourcepack: {}", iresourcepack.getPackName(), runtimeexception);
            } catch (IOException ioexception) {
                logger.warn("Unable to parse metadata section of resourcepack: {}", iresourcepack.getPackName(), ioexception);
            }
        }
    }

    public void onResourceManagerReload(IResourceManager resourceManager) {
        List<String> list = Lists.newArrayList("en_US");

        if (!"en_US".equals(this.currentLanguage)) {
            list.add(this.currentLanguage);
        }

        currentLocale.loadLocaleDataFiles(resourceManager, list);
        StringTranslate.replaceWith(currentLocale.properties);
    }

    public boolean isCurrentLocaleUnicode() {
        return currentLocale.isUnicode();
    }

    public boolean isCurrentLanguageBidirectional() {
        return this.getCurrentLanguage() != null && this.getCurrentLanguage().isBidirectional();
    }

    public Language getCurrentLanguage() {
        return this.languageMap.containsKey(this.currentLanguage) ? this.languageMap.get(this.currentLanguage) : this.languageMap.get("en_US");
    }

    public void setCurrentLanguage(Language currentLanguageIn) {
        this.currentLanguage = currentLanguageIn.getLanguageCode();
    }

    public SortedSet<Language> getLanguages() {
        return Sets.newTreeSet(this.languageMap.values());
    }
}
