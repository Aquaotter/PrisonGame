package prisongame.prisongame.lib;

import prisongame.prisongame.features.Feature;

import java.util.HashMap;

public class FeatureManager {
    public static FeatureManager instance;
    private HashMap<Class<?>, Feature> features = new HashMap<>();

    public FeatureManager() {
        instance = this;
    }

    public void initializeFeatures() {

    }

    public <T implements Feature> T getFeature(Class<T> clazz) {
        return (T) features.get(clazz);
    }
}
