/*
 *  "BSLib", Brainstorm Library.
 *  Copyright (C) 2015 by Sergey V. Zhdanovskih.
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
 * @author Sergey V. Zhdanovskih
 */
public abstract class BaseObject implements IDisposable
{
    private boolean fDisposed;

    protected void dispose(boolean disposing)
    {
    }

    @Override
    public final void dispose()
    {
        if (!fDisposed) {
            dispose(true); //called by user directly
            fDisposed = true;
        }
    }

    @Override
    public final void close()// throws IOException
    {
        dispose();
    }

    @Override
    protected void finalize() throws Throwable
    {
        dispose(false); //not called by user directly
        super.finalize();
    }
}
