/*
 *  "BSLib", Brainstorm Library.
 *  Copyright (C) 2015, 2017 by Sergey V. Zhdanovskih.
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

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;

/**
 *
 * @author Sergey V. Zhdanovskih
 */
public final class Logger
{
    private static final Object fLock = new Object();
    private static String fLogFilename;

    private static void writeLine(String message)
    {
        try {
            synchronized (fLock) {
                try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(fLogFilename, true), "UTF-8"))) {
                    writer.write("[" + new Date().toString() + "] -> " + message + AuxUtils.CRLF);
                    writer.flush();
                }
            }
        } catch (IOException ex) {
        }
    }

    public static void write(String msg)
    {
        writeLine(msg);
    }

    public static void init(String fileName)
    {
        fLogFilename = fileName;
        writeLine("log begin");
    }

    public static void done()
    {
        writeLine("log end");
    }
}
