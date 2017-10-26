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
package bslib.services;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * Version identifier for capabilities such as bundles, packages and plugins.
 *
 * <p>
 * Version identifiers have four components.
 * <ol>
 * <li>Major version. A non-negative integer.</li>
 * <li>Minor version. A non-negative integer.</li>
 * <li>Patch version. A non-negative integer.</li>
 * <li>Qualifier. A text string. See {@code Version(String)} for the format of
 * the qualifier string.</li>
 * </ol>
 *
 * <p>
 * {@code Version} objects are immutable.
 *
 * @Immutable
 */
public class Version implements Comparable<Version>
{
    private final int major;
    private final int minor;
    private final int patch;
    private final String qualifier;
    private static final String SEPARATOR = ".";
    private transient String versionString /* default to null */;
    private transient int hash /* default to 0 */;

    /**
     * The empty version "0.0.0".
     */
    public static final Version EMPTY_VERSION = new Version(0, 0, 0);

    /**
     * Creates a version identifier from the specified numerical components.
     *
     * <p>
     * The qualifier is set to the empty string.
     *
     * @param major Major component of the version identifier.
     * @param minor Minor component of the version identifier.
     * @param patch Patch component of the version identifier.
     * @throws IllegalArgumentException If the numerical components are
     * negative.
     */
    public Version(int major, int minor, int patch)
    {
        this(major, minor, patch, null);
    }

    /**
     * Creates a version identifier from the specified components.
     *
     * @param major Major component of the version identifier.
     * @param minor Minor component of the version identifier.
     * @param patch Patch component of the version identifier.
     * @param qualifier Qualifier component of the version identifier. If
     * {@code null} is specified, then the qualifier will be set to the empty
     * string.
     * @throws IllegalArgumentException If the numerical components are negative
     * or the qualifier string is invalid.
     */
    public Version(int major, int minor, int patch, String qualifier)
    {
        if (qualifier == null) {
            qualifier = "";
        }

        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.qualifier = qualifier;
        validate();
    }

    /**
     * Creates a version identifier from the specified string.
     *
     * <p>
     * Version string grammar:
     *
     * <pre>
     * version ::= major('.'minor('.'patch('.'qualifier)?)?)?
     * major ::= digit+
     * minor ::= digit+
     * patch ::= digit+
     * qualifier ::= (alpha|digit|'_'|'-')+
     * digit ::= [0..9]
     * alpha ::= [a..zA..Z]
     * </pre>
     *
     * @param version String representation of the version identifier. There
     * must be no whitespace in the argument.
     * @throws IllegalArgumentException If {@code version} is improperly
     * formatted.
     */
    public Version(String version)
    {
        int maj = 0;
        int min = 0;
        int pat = 0;
        String qual = "";

        try {
            StringTokenizer st = new StringTokenizer(version, SEPARATOR, true);
            maj = parseInt(st.nextToken(), version);

            if (st.hasMoreTokens()) { // minor
                st.nextToken(); // consume delimiter
                min = parseInt(st.nextToken(), version);

                if (st.hasMoreTokens()) { // patch
                    st.nextToken(); // consume delimiter
                    pat = parseInt(st.nextToken(), version);

                    if (st.hasMoreTokens()) { // qualifier separator
                        st.nextToken(); // consume delimiter
                        qual = st.nextToken(""); // remaining string

                        if (st.hasMoreTokens()) { // fail safe
                            throw new IllegalArgumentException("invalid version \"" + version + "\": invalid format");
                        }
                    }
                }
            }
        } catch (NoSuchElementException e) {
            IllegalArgumentException iae = new IllegalArgumentException("invalid version \"" + version + "\": invalid format");
            iae.initCause(e);
            throw iae;
        }

        major = maj;
        minor = min;
        patch = pat;
        qualifier = qual;
        validate();
    }

    /**
     * Parse numeric component into an int.
     *
     * @param value Numeric component
     * @param version Complete version string for exception message, if any
     * @return int value of numeric component
     */
    private static int parseInt(String value, String version)
    {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            IllegalArgumentException iae = new IllegalArgumentException("invalid version \"" + version + "\": non-numeric \"" + value + "\"");
            iae.initCause(e);
            throw iae;
        }
    }

    /**
     * Called by the Version constructors to validate the version components.
     *
     * @throws IllegalArgumentException If the numerical components are negative
     * or the qualifier string is invalid.
     */
    private void validate()
    {
        if (major < 0) {
            throw new IllegalArgumentException("invalid version \"" + toString0() + "\": negative number \"" + major + "\"");
        }
        if (minor < 0) {
            throw new IllegalArgumentException("invalid version \"" + toString0() + "\": negative number \"" + minor + "\"");
        }
        if (patch < 0) {
            throw new IllegalArgumentException("invalid version \"" + toString0() + "\": negative number \"" + patch + "\"");
        }
        for (char ch : qualifier.toCharArray()) {
            if (('A' <= ch) && (ch <= 'Z')) {
                continue;
            }
            if (('a' <= ch) && (ch <= 'z')) {
                continue;
            }
            if (('0' <= ch) && (ch <= '9')) {
                continue;
            }
            if ((ch == '_') || (ch == '-')) {
                continue;
            }
            throw new IllegalArgumentException("invalid version \"" + toString0() + "\": invalid qualifier \"" + qualifier + "\"");
        }
    }

    /**
     * Parses a version identifier from the specified string.
     *
     * <p>
     * See {@link #Version(String)} for the format of the version string.
     *
     * @param version String representation of the version identifier. Leading
     * and trailing whitespace will be ignored.
     * @return A {@code Version} object representing the version identifier. If
     * {@code version} is {@code null} or the empty string then
     * {@link #EMPTY_VERSION} will be returned.
     * @throws IllegalArgumentException If {@code version} is improperly
     * formatted.
     */
    public static Version parseVersion(String version)
    {
        if (version == null) {
            return EMPTY_VERSION;
        }

        return valueOf(version);
    }

    /**
     * Returns a {@code Version} object holding the version identifier in the
     * specified {@code String}.
     *
     * <p>
     * See {@link #Version(String)} for the format of the version string.
     *
     * <p>
     * This method performs a similar function as {@link #parseVersion(String)}
     * but has the static factory {@code valueOf(String)} method signature.
     *
     * @param version String representation of the version identifier. Leading
     * and trailing whitespace will be ignored. Must not be {@code null}.
     * @return A {@code Version} object representing the version identifier. If
     * {@code version} is the empty string then {@link #EMPTY_VERSION} will be
     * returned.
     * @throws IllegalArgumentException If {@code version} is improperly
     * formatted.
     */
    public static Version valueOf(String version)
    {
        version = version.trim();
        if (version.length() == 0) {
            return EMPTY_VERSION;
        }

        return new Version(version);
    }

    /**
     * Returns the major component of this version identifier.
     *
     * @return The major component.
     */
    public int getMajor()
    {
        return major;
    }

    /**
     * Returns the minor component of this version identifier.
     *
     * @return The minor component.
     */
    public int getMinor()
    {
        return minor;
    }

    /**
     * Returns the patch component of this version identifier.
     *
     * @return The patch component.
     */
    public int getPatch()
    {
        return patch;
    }

    /**
     * Returns the qualifier component of this version identifier.
     *
     * @return The qualifier component.
     */
    public String getQualifier()
    {
        return qualifier;
    }

    /**
     * Returns the string representation of this version identifier.
     *
     * <p>
     * The format of the version string will be {@code major.minor.patch} if
     * qualifier is the empty string or {@code major.minor.patch.qualifier}
     * otherwise.
     *
     * @return The string representation of this version identifier.
     */
    @Override
    public String toString()
    {
        return toString0();
    }

    /**
     * Internal toString behavior
     *
     * @return The string representation of this version identifier.
     */
    String toString0()
    {
        String s = versionString;
        if (s != null) {
            return s;
        }
        int q = qualifier.length();
        StringBuffer result = new StringBuffer(20 + q);
        result.append(major);
        result.append(SEPARATOR);
        result.append(minor);
        result.append(SEPARATOR);
        result.append(patch);
        if (q > 0) {
            result.append(SEPARATOR);
            result.append(qualifier);
        }
        return versionString = result.toString();
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return An integer which is a hash code value for this object.
     */
    @Override
    public int hashCode()
    {
        int h = hash;
        if (h != 0) {
            return h;
        }
        h = 31 * 17;
        h = 31 * h + major;
        h = 31 * h + minor;
        h = 31 * h + patch;
        h = 31 * h + qualifier.hashCode();
        return hash = h;
    }

    /**
     * Compares this {@code Version} object to another object.
     *
     * <p>
     * A version is considered to be <b>equal to </b> another version if the
     * major, minor and patch components are equal and the qualifier component
     * is equal (using {@code String.equals}).
     *
     * @param object The {@code Version} object to be compared.
     * @return {@code true} if {@code object} is a {@code Version} and is equal
     * to this object; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object object)
    {
        if (object == this) {
            return true;
        }

        if (!(object instanceof Version)) {
            return false;
        }

        Version other = (Version) object;
        return (major == other.major) && (minor == other.minor) && (patch == other.patch) && qualifier.equals(other.qualifier);
    }

    /**
     * Compares this {@code Version} object to another {@code Version}.
     *
     * <p>
     * A version is considered to be <b>less than</b> another version if its
     * major component is less than the other version's major component, or the
     * major components are equal and its minor component is less than the other
     * version's minor component, or the major and minor components are equal
     * and its patch component is less than the other version's patch component,
     * or the major, minor and patch components are equal and it's qualifier
     * component is less than the other version's qualifier component (using
     * {@code String.compareTo}).
     *
     * <p>
     * A version is considered to be <b>equal to</b> another version if the
     * major, minor and patch components are equal and the qualifier component
     * is equal (using {@code String.compareTo}).
     *
     * @param other The {@code Version} object to be compared.
     * @return A negative integer, zero, or a positive integer if this version
     * is less than, equal to, or greater than the specified {@code Version}
     * object.
     * @throws ClassCastException If the specified object is not a
     * {@code Version} object.
     */
    @Override
    public int compareTo(Version other)
    {
        if (other == this) {
            return 0;
        }

        int result = major - other.major;
        if (result != 0) {
            return result;
        }

        result = minor - other.minor;
        if (result != 0) {
            return result;
        }

        result = patch - other.patch;
        if (result != 0) {
            return result;
        }

        return qualifier.compareTo(other.qualifier);
    }
}
