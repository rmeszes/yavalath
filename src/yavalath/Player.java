package yavalath;

import java.awt.*;

public class Player {
    private String name;
    private Color color;
    public enum Type {
        HUMAN("Ember"),
        NONE("Nem játszik"),
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
    private Type type;
    public Player(String name, Color color, Type type) {
        this.name = name;
        this.color = color;
        this.type = type;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
