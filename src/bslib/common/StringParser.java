/*
 *  "BSLib", Brainstorm Library.
 *  Copyright (C) 2015 by Serg V. Zhdanovskih (aka Alchemist, aka Norseman).
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package bslib.common;

/**
 *
 * @author Serg V. Zhdanovskih
 */
public class StringParser
{
    public static final char cptEOF = (char) (0);
    public static final char cptSymbol = (char) (1);
    public static final char cptString = (char) (2);
    public static final char cptInteger = (char) (3);
    public static final char cptFloat = (char) (4);

    private final char[] fBuffer;
    private final int fBufSize;
    private int fBufPos;
    private String fTemp;

    public char Token;

    public StringParser(String str)
    {
        this.fBufPos = 0;
        this.fBufSize = str.length();
        this.fBuffer = new char[this.fBufSize];
        str.getChars(0, this.fBufSize, this.fBuffer, 0);
    }

    private void throwException(String message)
    {
        throw new ParserException(message);
    }

    private boolean nextChar()
    {
        if (this.fBufPos == this.fBufSize - 1) {
            this.fBufPos = 0;
            return false;
        } else {
            this.fBufPos++;
            return true;
        }
    }

    public final void checkToken(char T)
    {
        if (this.Token != T) {
            switch (T) {
                case cptSymbol:
                    this.throwException("Symbol expected");
                    break;
                case cptString:
                    this.throwException("String expected");
                    break;
                case cptInteger:
                    this.throwException("Integer expected");
                    break;
                case cptFloat:
                    this.throwException("Float expected");
                    break;
                default:
                    this.throwException(String.format("\"%s\" expected", new Object[]{T}));
                    break;
            }
        }
    }

    public final char nextToken()
    {
        try {
            this.fTemp = "";
            char result;

            // skip blanks
            while (true) {
                if (this.fBufPos >= this.fBufSize) {
                    result = cptEOF;
                    this.Token = result;
                    return result;
                } else {
                    char c = this.fBuffer[this.fBufPos];
                    if (!Character.isWhitespace(c)) {
                        break;
                    }
                    this.nextChar();
                }
            }

            char c = this.fBuffer[this.fBufPos];
            if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
                result = cptString;

                this.fTemp += c;
                while (this.nextChar()) {
                    char nc = this.fBuffer[this.fBufPos];
                    if ((nc >= 'A' && nc <= 'Z') || (nc >= 'a' && nc <= 'z')) {
                        this.fTemp += nc;
                    } else {
                        break;
                    }
                }
            } else if (c >= '0' && c <= '9') {
                result = cptInteger;

                this.fTemp += c;
                while (this.nextChar()) {
                    char nc = this.fBuffer[this.fBufPos];
                    if ((nc >= '0' && nc <= '9') || (nc == '.')) {
                        if (c == '.') {
                            result = cptFloat;
                        }
                        this.fTemp += nc;
                    } else {
                        break;
                    }
                }
            } else {
                result = cptSymbol;
                this.fTemp += c;
                this.nextChar();
            }

            this.Token = result;
            return result;
        } catch (Exception ex) {
            return cptEOF;
        }
    }

    public final double getFloatToken()
    {
        return Double.parseDouble(this.getStringToken());
    }

    public final int getIntToken()
    {
        return Integer.parseInt(this.getStringToken());
    }

    public final String getStringToken()
    {
        return this.fTemp;
    }
}
