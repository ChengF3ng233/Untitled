package net.optifine.expr;

public enum TokenType {
    IDENTIFIER("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_:."),
    NUMBER("0123456789", "0123456789."),
    OPERATOR("+-*/%!&|<>=", "&|="),
    COMMA(","),
    BRACKET_OPEN("("),
    BRACKET_CLOSE(")");

    public static final TokenType[] VALUES = values();
    private final String charsFirst;
    private final String charsNext;

    TokenType(String charsFirst) {
        this(charsFirst, "");
    }

    TokenType(String charsFirst, String charsNext) {
        this.charsFirst = charsFirst;
        this.charsNext = charsNext;
    }

    public static TokenType getTypeByFirstChar(char ch) {
        for (int i = 0; i < VALUES.length; ++i) {
            TokenType tokentype = VALUES[i];

            if (tokentype.getCharsFirst().indexOf(ch) >= 0) {
                return tokentype;
            }
        }

        return null;
    }

    public String getCharsFirst() {
        return this.charsFirst;
    }

    public String getCharsNext() {
        return this.charsNext;
    }

    public boolean hasCharNext(char ch) {
        return this.charsNext.indexOf(ch) >= 0;
    }

    private static class Const {
        static final String ALPHAS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        static final String DIGITS = "0123456789";
    }
}
