package ga.justreddy.wiki.tnttag.api.events;

import ga.justreddy.wiki.tnttag.api.game.Game;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Getter
public class GameStartEvent extends TagEvent {

    Game game;

}
