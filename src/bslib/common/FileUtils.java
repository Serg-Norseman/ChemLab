/*
 *  "BSLib", Brainstorm Library.
 *  Copyright (C) 2017 by Sergey V. Zhdanovskih.
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

import bslib.components.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

/**
 *
 * @author Sergey V. Zhdanovskih
 */
public class FileUtils
{
    public static String getFileNameWithoutExtension(String filename)
    {
        return removeExtension(getName(filename));
    }

    public static String getFileExtension(String filename)
    {
        if (StringHelper.isNullOrEmpty(filename)) {
            return "";
        } else {
            int index = indexOfExtension(filename);
            return (index < 0) ? "" : filename.substring(index).toLowerCase();
        }
    }

    public static int indexOfLastSeparator(String filename)
    {
        if (filename == null) {
            return -1;
        } else {
            int lastUnixPos = filename.lastIndexOf(47);
            int lastWindowsPos = filename.lastIndexOf(92);
            return Math.max(lastUnixPos, lastWindowsPos);
        }
    }

    public static String getName(String filename)
    {
        if (filename == null) {
            return null;
        } else {
            int index = indexOfLastSeparator(filename);
            return filename.substring(index + 1);
        }
    }

    public static String removeExtension(String filename)
    {
        if (filename == null) {
            return null;
        } else {
            int index = indexOfExtension(filename);
            return index == -1 ? filename : filename.substring(0, index);
        }
    }

    public static int indexOfExtension(String filename)
    {
        if (filename == null) {
            return -1;
        } else {
            int extensionPos = filename.lastIndexOf('.');
            int lastSeparator = indexOfLastSeparator(filename);
            return lastSeparator > extensionPos ? -1 : extensionPos;
        }
    }

    private static void listFilesForFolder(final File folderFile, FileFilter filter,
            boolean onlyTopDirectory, ArrayList<String> files)
    {
        if (!folderFile.exists()) {
            return;
        }

        for (final File fileEntry : folderFile.listFiles(filter)) {
            if (fileEntry.isDirectory() && !onlyTopDirectory) {
                listFilesForFolder(fileEntry, filter, false, files);
            } else {
                files.add(fileEntry.getPath());
            }
        }
    }

    public static List<String> getDirectoryFiles(final String folder, String ext, boolean onlyTopDirectory)
    {
        // FIXME: mask!
        File folderFile = new File(folder);
        ArrayList<String> files = new ArrayList<>();

        FileFilter filter = new FileFilter()
        {
            @Override
            public boolean accept(File f)
            {
                return f.getName().toLowerCase().endsWith(ext);
            }
        };

        listFilesForFolder(folderFile, filter, onlyTopDirectory, files);
        return files;
    }

    public static BufferedReader createBufferedReader(String fileName, String charsetName)
            throws UnsupportedEncodingException, FileNotFoundException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), charsetName));
        return reader;
    }

    public static BufferedWriter createBufferedWriter(String fileName, String charsetName)
            throws UnsupportedEncodingException, FileNotFoundException
    {
        BufferedWriter reader = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, false), charsetName));
        return reader;
    }

    public static String readAll(BufferedReader reader) throws IOException
    {
        String text = "";
        String line;
        while ((line = reader.readLine()) != null) {
            text += line + "\n";
        }
        return text;
    }

    public static JFileChooser createOpenFileDialog(String title, String context, String filter, int filterIndex, String defaultExt, boolean multiSelect)
    {
        JFileChooser ofd = new JFileChooser();
        ofd.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if (!StringHelper.isNullOrEmpty(title)) {
            ofd.setDialogTitle(title);
        }

        if (!StringHelper.isNullOrEmpty(context)) {
            ofd.setCurrentDirectory(new File(context));
        }

        if (!StringHelper.isNullOrEmpty(filter)) {
            UIHelperEx.setFileChooserFilters(ofd, filter, filterIndex);
        }

        ofd.setMultiSelectionEnabled(multiSelect);

        return ofd;
    }

    // FIXME: localize?!
    public static JFileChooser createSaveFileDialog(String title, String context,
            String filter, int filterIndex, String defaultExt,
            String suggestedFileName, boolean overwritePrompt)
    {
        JFileChooser sfd = new JFileChooser()
        {
            @Override
            public void approveSelection()
            {
                File f = getSelectedFile();
                if (overwritePrompt && f.exists() && getDialogType() == SAVE_DIALOG) {
                    int result = JOptionPane.showConfirmDialog(this, "The file exists, overwrite?", "Existing file", JOptionPane.YES_NO_CANCEL_OPTION);
                    switch (result) {
                        case JOptionPane.YES_OPTION:
                            super.approveSelection();
                            return;
                        case JOptionPane.NO_OPTION:
                            return;
                        case JOptionPane.CLOSED_OPTION:
                            return;
                        case JOptionPane.CANCEL_OPTION:
                            cancelSelection();
                            return;
                    }
                }
                super.approveSelection();
            }
        };

        sfd.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if (!StringHelper.isNullOrEmpty(title)) {
            sfd.setDialogTitle(title);
        }

        if (!StringHelper.isNullOrEmpty(context)) {
            sfd.setCurrentDirectory(new File(context));
        }

        if (!StringHelper.isNullOrEmpty(filter)) {
            UIHelperEx.setFileChooserFilters(sfd, filter, filterIndex);
        }

        if (!StringHelper.isNullOrEmpty(suggestedFileName)) {
            sfd.setSelectedFile(new File(suggestedFileName));
        }

        return sfd;
    }

    public static String changeExtension(String oldFileName, String newExtension)
    {
        File file = new File(oldFileName);
        String path = file.getParent();
        String name = FileUtils.getFileNameWithoutExtension(oldFileName) + newExtension;
        return path + File.separatorChar + name;
    }

    public static void copy(InputStream is, OutputStream out) throws IOException
    {
        if (is != null && is.available() > 0) {
            byte buffer[] = new byte[8192];

            int read;
            while (-1 != (read = is.read(buffer))) {
                out.write(buffer, 0, read);
                out.flush();
            }
        }
    }
}
