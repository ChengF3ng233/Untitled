package net.optifine.expr;

import lombok.Getter;

@Getter
public class Token {
    private final TokenType type;
    private final String text;

    public Token(TokenType type, String text) {
        this.type = type;
        this.text = text;
    }

    public String toString() {
        return this.text;
    }
}
