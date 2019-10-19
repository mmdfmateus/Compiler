package lexical;

public enum Tag {

    // Reserved words
    START,
    EXIT,
    INT,
    FLOAT,
    STRING,
    IF,
    THEN,
    ELSE,
    END,
    DO,
    WHILE,
    SCAN,
    PRINT,
    NOT,
    OR,
    AND,

    // Operators
    ADD,
    MINUS,
    MULT,
    DIV,
    GREATER_THAN,
    LESS_THAN,
    GREATER_THAN_OR_EQUAL,
    LESS_THAN_OR_EQUAL,
    GREATER_LESS,
    COMPARATOR,

    // Symbols
    OPEN_BRACE,
    CLOSE_BRACE,
    OPEN_BRACKET,
    CLOSE_BRACKET,
    OPEN_PARENTHESIS,
    CLOSE_PARETHESIS,
    PT_PONTOEVIRGULA,
    SEMICOLON,
    COLON,
    DOT,
    DOUBLE_QUOTES,

    // Others
    LITERAL,
    ID,
    COMMENT,
    CONST_INT,
    CONST_CHAR;
}