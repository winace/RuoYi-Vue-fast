package com.ruoyi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 特殊字符枚举
 *
 * @author zhaowang
 * @since 2024/7/30 17:04
 */
@Getter
@AllArgsConstructor
public enum SpecialCharacter {
    SPACE(' ', "空格"),
    TAB('\t', "制表符"),
    NEW_LINE('\n', "换行符"),
    CARRIAGE_RETURN('\r', "回车符"),
    HYPHEN('-', "中横线"),
    COMMA(',', "逗号"),
    PERIOD('.', "句号"),
    QUESTION_MARK('?', "问号"),
    COLON(':', "冒号"),
    SEMICOLON(';', "分号"),
    QUOTATION_MARK('"', "引号"),
    APOSTROPHE('\'', "撇号"),
    VERTICAL_SLASH('|', "竖线"),
    LEFT_SLASH('/', "反斜线"),
    BACKSLASH('\\', "斜线"),
    TILDE('~', "波浪线"),
    LEFT_TURN('`', "反引号"),
    EXCLAMATION_MARK('!', "感叹号"),
    AT_SIGN('@', "at符号"),
    HASH('#', "井号"),
    DOLLAR_SIGN('$', "美元符号"),
    PERCENT('%', "百分号"),
    CARET('^', "脱字符"),
    AMPERSAND('&', "与号"),
    ASTERISK('*', "星号"),
    MINUS('-', "减号"),
    UNDERSCORE('_', "下划线"),
    PLUS('+', "加号"),
    EQUALS('=', "等号"),
    LEFT_PARENTHESIS('(', "左括号"),
    RIGHT_PARENTHESIS(')', "右括号"),
    LEFT_BRACKET('[', "左中括号"),
    RIGHT_BRACKET(']', "右中括号"),
    LEFT_BRACE('{', "左大括号"),
    RIGHT_BRACE('}', "右大括号"),
    LEFT_ANGLE('<', "左尖括号"),
    RIGHT_ANGLE('>', "右尖括号"),
    DOWN_ANGLE('ˇ', "下尖括号"),
    LEFT_DOUBLE_QUOTATION('“', "左双引号"),
    RIGHT_DOUBLE_QUOTATION('”', "右双引号"),
    LEFT_SINGLE_QUOTATION('‘', "左单引号"),
    RIGHT_SINGLE_QUOTATION('’', "右单引号"),
    ;

    private final Character value;
    private final String description;
}
