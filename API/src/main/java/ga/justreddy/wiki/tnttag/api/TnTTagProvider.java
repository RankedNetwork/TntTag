package ga.justreddy.wiki.tnttag.api;

public final class TnTTagProvider {

    private static TntTagAPI api;

    public static TntTagAPI get() {
        if (api == null) throw new IllegalStateException("TnTTag plugin not found!");
        return api;
    }

    public static void setApi(TntTagAPI api) {
        TnTTagProvider.api = api;
    }
}
