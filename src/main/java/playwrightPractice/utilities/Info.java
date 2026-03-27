package playwrightPractice.utilities;

import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

public class Info {

    private static final Properties props = System.getProperties();

    /*
     * Get all System properties
     *
     * <pre>
     * {
     * 	&#64;code
     * 	TreeMap<String, String> props = FwInfo.getProperties();
     * }
     * </pre>
     *
     * @return a TreeMap of all System properties
     */
    public static TreeMap<String, String> getAll() {
        TreeMap<String, String> tree = new TreeMap<String, String>();
        for (Object pkey : props.keySet()) {
            String key = (String) pkey;
            tree.put(key, props.getProperty(key));
        }
        return tree;
    }

    /*
     * Get the computer name from the System Environment
     *
     * <pre>
     * {
     * 	&#64;code
     * 	String c = FwInfo.getComputerName();
     * }
     * </pre>
     *
     * @return the computer name or "Unknown Computer Name" if not found
     */
    public static String getComputerName() {
        Map<String, String> env = System.getenv();

        // Windows
        if (env.containsKey("COMPUTERNAME")) {
            return env.get("COMPUTERNAME");

            // Linux
        } else if (env.containsKey("HOSTNAME")) {
            return env.get("HOSTNAME");

        } else {
            return "Unknown Computer Name";
        }
    }

    /*
     * Get the system architecture (32 or 64 bit) from sun.arch.data.model
     *
     * <pre>
     * {
     * 	&#64;code
     * 	String a = FwInfo.getOsArch();
     * }
     * </pre>
     *
     * @return the OS architecture
     */
    public static String getOsArch() {
        return props.getProperty("sun.arch.data.model");
    }

    /*
     * Get the operating system name
     *
     * <pre>
     * {
     * 	&#64;code
     * 	String o = FwInfo.getOsName();
     * }
     * </pre>
     *
     * @return the Operating system name from os.name
     */
    public static String getOsName() {
        return props.getProperty("os.name");
    }

    /*
     * Get the current system timezone from user.timezone
     *
     * <pre>
     * {
     * 	&#64;code
     * 	String t = FwInfo.getTimezone();
     * }
     * </pre>
     *
     * @return the system timezone
     */
    public static String getTimezone() {
        return props.getProperty("user.timezone");
    }

    /*
     * Get the current user's name from user.name
     *
     * <pre>
     * {
     * 	&#64;code
     * 	String u = FwInfo.getUsername();
     * }
     * </pre>
     *
     * @return the user name
     */
    public static String getUsername() {
        return props.getProperty("user.name");
    }

    /*
     * Test if this is a 32 bit system
     *
     * <pre>
     * {
     * 	&#64;code
     * 	boolean b = FwInfo.isOs32bit();
     * }
     * </pre>
     *
     * @return true if the OS is 32 bit, false if not
     */
    public static Boolean isOs32bit() {
        return getOsArch().equals("32");
    }

    /*
     * Test if this is a 64 bit system
     *
     * <pre>
     * {
     * 	&#64;code
     * 	boolean b = FwInfo.isOs64bit();
     * }
     * </pre>
     *
     * @return true if the OS is 64 bit, false if not
     */
    public static Boolean isOs64bit() {
        return getOsArch().equals("64");
    }

    /*
     * Test if this is this a Windows system
     *
     * <pre>
     * {
     * 	&#64;code
     * 	boolean b = FwInfo.isOsWindows();
     * }
     * </pre>
     *
     * @return true if the OS is Windows, false if not
     */
    public static Boolean isOsWindows() {
        return getOsName().startsWith("Windows");
    }

}
