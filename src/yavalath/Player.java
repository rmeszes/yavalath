package yavalath;

import java.awt.*;
import java.io.Serializable;
import java.util.EnumSet;
import java.util.Set;

public class Player implements Serializable {
    private final String name;
    private final Color color;
    private boolean inGame;
    public enum Type {
        NONE("Nem játszik"),
        HUMAN("Ember"),
        BOT("Bot");
        private final String value;
        Type(String s) {
            value = s;
        }

        @Override
        public String toString() {
            return value;
        }
    }
    private final Type type;
    public Player(String name, Color color, Type type) {
        this.name = name;
        this.color = color;
        this.type = type;
        inGame = type != Type.NONE;
    }

    public Color getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }
    public static Set<Type> getAllTypes() {
        return EnumSet.allOf(Type.class);
    }

    public boolean isInGame() {
        return inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public String toString() {
        return '{' + "name=" + name + "color=" + color.toString() + "type=" + type;
    }
}
