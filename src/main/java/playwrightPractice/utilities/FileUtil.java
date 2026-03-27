package playwrightPractice.utilities;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtil {

    /*
     * Delete a directory
     *
     * <pre>
     * {
     * 	&#64;code
     * 	boolean b = FwFile.deleteDirectory("/path/to/a/folder");
     * }
     * </pre>
     *
     * @param directory the pathname to the directory
     * @return true if successful, false if not
     */
    public static boolean deleteDirectory(String directory) {
        if (!directoryExists(directory)) {

            return false;
        }

        if (!deleteFiles(directory)) {

            return false;
        }

        try {
            File f = Paths.get(directory).toFile();
            f.delete();
        } catch (Exception e) {

            return false;
        }
        return true;
    }

    /*
     * Delete a single file in a directory
     *
     * <pre>
     * {
     * 	&#64;code
     * 	boolean b = FwFile.deleteFiles("/path/to/a/folder/file.txt");
     * }
     * </pre>
     *
     * @param pathname - the absolute pathname to the file
     * @return true if successful, false if not
     */
    public static boolean deleteFile(String pathname) {
        if (!fileExists(pathname))
            return false;

        File f = Paths.get(pathname).toFile();
        return f.delete();
    }

    /*
     * Delete all files in a directory
     *
     * <pre>
     * {
     * 	&#64;code
     * 	boolean b = FwFile.deleteFiles("/path/to/a/folder");
     * }
     * </pre>
     *
     * @param directory the path to the directory
     * @return true if successful, false if not
     */
    public static boolean deleteFiles(String directory) {
        return deleteFiles(directory, ".*");
    }

    /*
     * Delete all files in a directory matching the regex
     *
     * <pre>
     * {
     * 	&#64;code
     * 	boolean b = FwFile.deleteFiles("/path/to/a/folder", "log-.*");
     * }
     * </pre>
     *
     * @param directory the path to the directory
     * @param regex     the regular expression to match
     * @return true if successful, false if not
     */
    public static boolean deleteFiles(String directory, String regex) {
        if (!directoryExists(directory))
            return false;

        List<File> myFiles = listFiles(directory);
        if (myFiles.isEmpty())
            return true;

        boolean result = true;
        File f = null;
        for (int i = 0; i < myFiles.size(); i++) {
            try {
                f = myFiles.get(i);

                f.delete();
            } catch (Exception e) {
                result = false;

            }
        }
        return result;
    }

    /*
     * Test a directory exists
     *
     * <pre>
     * {@code
     *   boolean b = FwFile.fileExists("/path/to/a/folder");
     *           b is true
     *
     *   boolean b = FwFile.fileExists("/path/to/a/folder/data.txt");
     *           b is false
     * }
     * </pre>
     *
     * @param directory the pathname to the directory
     * @return true if a directory and it exists, false if not
     */
    public static boolean directoryExists(String directory) {
        File f = Paths.get(directory).toFile();
        return f.exists() && f.isDirectory();
    }

    /*
     * Test a file exists
     *
     * <pre>
     * {@code
     *   boolean b = FwFile.fileExists("/path/to/a/folder");
     *           b is false
     *
     *   boolean b = FwFile.fileExists("/path/to/a/folder/data.txt");
     *           b is true
     * }
     * </pre>
     *
     * @param pathname the pathname to the file
     * @return true if the file exists, false if not
     */
    public static boolean fileExists(String pathname) {
        File f = Paths.get(pathname).toFile();
        return f.exists() && !f.isDirectory();
    }

    /*
     * Read a file and return it's contents as a string
     *
     * <pre>
     * {
     * 	&#64;code
     * 	String content = FwFile.fileToString("/path/to/a/folder/data.txt");
     * }
     * </pre>
     *
     * @param pathname the pathname to the file
     * @return a String containing the file contents or null on error
     */
    public static String fileToString(String pathname) {
        try {
            return new String(Files.readAllBytes(Paths.get(pathname)));
        } catch (Exception e) {

        }
        return null;
    }

    /*
     * Return the file system pathname to the file.<br>
     * If it is not a full pathname (per isPathname), the file is assumed relative
     * to System Property user.dir
     *
     * <pre>
     * {@code String p=FwFile.getFilePathname("data/file.txt");p is System.getProperty("user.dir")+"/data/file.txt"
     *
     * String p=FwFile.getFilePathname("/data/file.txt");p is"/data/file.txt"
     *
     * String p=FwFile.getFilePathname("C:\\users\\name\\appdata\\local\\temp\\file.txt");p is C:\\users\\name\\appdata\\local\\temp\\file.txt}
     * </pre>
     *
     * @param pathname File system pathname to the file
     * @return a string containing the full file system pathname to the file
     */
    public static String getFilePathname(String pathname) {
        String absolutePath = null;
        if (isPathname(pathname)) {
            absolutePath = Paths.get(pathname).toString();
        } else {
            absolutePath = Paths.get(System.getProperty("user.dir"), pathname).toString();
        }

        return absolutePath;
    }

    /*
     * Return the parent directories for the given pathname
     * <p>
     * Everything before the last "/" or "\"
     *
     * <pre>
     * {@code
     *   String pd = FwFile.getParentDir("/path/to/a/folder/data.txt");
     *          pd is "/path/to/a/folder"
     * }
     * </pre>
     *
     * @param pathname the pathname to process
     * @return the directory part of the pathname
     */
    public static String getParentDir(String pathname) {
        return Paths.get(pathname).getParent().toAbsolutePath().toString();
    }

    /*
     * Verify the filename contains no / or \ characters
     *
     * <pre>
     * {@code
     *   boolean b = FwFile.isFilename("a.txt");
     *           b is true
     *
     *   boolean b = FwFile.isFilename("/a.txt");
     *           b is false
     * }
     * </pre>
     *
     * @param filename the filename to test
     * @return true if no / or \\ found in the name, false otherwise
     */
    public static boolean isFilename(String filename) {
        Pattern p = Pattern.compile("/|\\\\");
        Matcher m = p.matcher(filename);
        return !m.find();
    }

    /*
     * Test pathname starts with any of the following:
     *
     * <pre>
     * {@code
     *         [drive]:\\ - Windows Drive
     *         \\ - Windows UNC Path
     *         / - Unix/Linux Path
     *
     *     boolean b = FwFile.isPathname("a/path");
     *             b is false
     *
     *     boolean b = FwFile.isPathname("/a/path");
     *             b is true
     *
     *     boolean b = FwFile.isPathname("C:\a\path");
     *             b is true
     *
     *     boolean b = FwFile.isPathname("\\server\a\path");
     *             b is true
     * }
     * </pre>
     *
     * @param pathname the "pathname" to the file or directory
     * @return true if a pathname, false if not
     */
    public static boolean isPathname(String pathname) {
        Pattern p = Pattern.compile("^[a-zA-Z]:|^/|^\\\\");
        Matcher m = p.matcher(pathname);
        return m.find();
    }

    /*
     * Return a list of all files in the directory
     *
     * <pre>
     * {
     * 	&#64;code
     * 	List<String> files = FwFile.listFiles("/path/to/a/folder");
     * }
     * </pre>
     *
     * @param directory The path to the directory
     * @return a List of file objects
     */
    public static List<File> listFiles(String directory) {
        return listFiles(directory, ".*");
    }

    /*
     * Return a list of all files matching pattern 'regex' in the directory
     *
     * <pre>
     * {
     * 	&#64;code
     * 	List<String> files = FwFile.listFiles("/path/to/a/folder", "log-");
     * }
     * </pre>
     *
     * @param directory The path to the directory
     * @param regex     The Java regex to match
     * @return a List of file objects
     */
    public static List<File> listFiles(String directory, String regex) {
        List<File> fileList = new ArrayList<File>();
        File f = Paths.get(directory).toFile();
        if (!f.exists() || !f.isDirectory())
            return fileList;

        // Default the regex to all files if ""
        String r = regex;
        if (r == null || r.isEmpty())
            r = ".*";
        Pattern pattern = Pattern.compile(r);

        FilenameFilter textFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                Matcher matcher = pattern.matcher(name);
                return matcher.matches();
            }
        };

        for (File file : f.listFiles(textFilter)) {
            if (!file.isDirectory()) {
                fileList.add(file);
            }
        }
        return fileList;
    }

    /*
     * Create a directory including any parent dirs
     *
     * <pre>
     * {
     * 	&#64;code
     * 	boolean b = FwFile.makeDirectory("/path/to/a/folder");
     * }
     * </pre>
     *
     * @param directory the pathname to the directory
     * @return true if successful, false if not
     */
    public static boolean makeDirectory(String directory) {
        File f = Paths.get(directory).toFile();
        if (f.exists())
            return true;
        return f.mkdirs();
    }

    /*
     * Makes a pathname using the Paths class
     * <p>
     * Paths creates the path with the correct OS pathname separators
     *
     * <pre>
     * {@code
     *   String p = FwFile.makePath("the", "path", "to", "file.txt");
     *          p is "/the/path/to/file.txt"
     * }
     * </pre>
     *
     * @param first first part of the pathname
     * @param parts the rest of the parts
     * @return the pathname as a string
     */
    public static String makePath(String first, String... parts) {
        return Paths.get(first, parts).toString();
    }

    public static String readFile(String fileName) throws ParseException {
        String text = "";
        try {
            text = new String(Files.readAllBytes(Paths.get(fileName)));
        } catch (IOException e) {
            e.fillInStackTrace();
        }
        return text;
    }

    /*
     * Save a string to a file
     *
     * <pre>
     * {
     * 	&#64;code
     * 	boolean b = FwFile.stringToFile("some text in a string", "/path/to/a/folder/data.txt");
     * }
     * </pre>
     *
     * @param text     the text of the string to save
     * @param pathname the file system pathname to save to
     * @return true if successful, false if an error
     */
    public static boolean stringToFile(String text, String pathname) {
        if (text == null || pathname == null)
            return false;

        PrintWriter out = null;
        try {
            String dir = Paths.get(pathname).getParent().toString();
            if (makeDirectory(dir)) {
                out = new PrintWriter(pathname);
                out.print(text);
            }
        } catch (Exception e) {

            return false;
        } finally {
            out.close();
        }
        return true;
    }
}
