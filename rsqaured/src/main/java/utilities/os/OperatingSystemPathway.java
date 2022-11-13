package utilities.os;

import javax.swing.filechooser.FileSystemView;

public abstract class OperatingSystemPathway {
    //mac os catalina and above cannot write to root directory(read only), must change directory to write
    // and also need to read from that changed directory
    public static String macOsFilePathSinceRootUnwritable(String fileSystemRoot){
        String osName = getOperatingSystem();
        if(osName.equals("Mac OS X")) {
            // by removing the / prior to /data/grassp it will not longer write to the root directory
            Double osVersionDouble = Double.valueOf(getOperatingSystemVersion());
            //big sur is 10.16, but appears to be referred to as 11
            if (osVersionDouble >= 10.16) fileSystemRoot = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
        }
        return fileSystemRoot;
    }
    private static String getOperatingSystem() {
        String os = System.getProperty("os.name");
        return os;
    }
    private static String getOperatingSystemVersion() {
        String os = System.getProperty("os.version");
        return os;
    }
}
