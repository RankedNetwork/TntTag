package ga.justreddy.wiki.tnttag.api.events;

import ga.justreddy.wiki.tnttag.api.entity.TagPlayer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @author JustReddy
 */
@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class PlayerGameJoinEvent extends TagEvent {

    TagPlayer player;

}
