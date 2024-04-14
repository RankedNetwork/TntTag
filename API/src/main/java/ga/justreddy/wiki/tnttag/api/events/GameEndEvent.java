package ga.justreddy.wiki.tnttag.api.events;

import ga.justreddy.wiki.tnttag.api.entity.TagPlayer;
import ga.justreddy.wiki.tnttag.api.game.Game;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Optional;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class GameEndEvent extends TagEvent {

    Game game;
    TagPlayer winner;

    public Game getGame() {
        return game;
    }

    public Optional<TagPlayer> getWinner() {
        return Optional.ofNullable(winner);
    }

}
