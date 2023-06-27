package ga.justreddy.wiki.tnttag.api.entity.data;

public interface PlayerStats {

    int getGamesWon();

    int getGamesLost();

    void addGamesWon();

    void addGamesLost();

    void addGamesWon(int amount);

    void addGamesLost(int amount);

}
