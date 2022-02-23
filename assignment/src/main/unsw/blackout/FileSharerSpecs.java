package unsw.blackout;

public class FileSharerSpecs {

    // CLASS ATTRIBUTES
    private int maxFileCapacity;
    private int maxStorageCapacity;
    private int fileReceiveSpeed;
    private int fileSendSpeed;

    /**
     * CLASS CONSTRUCTOR - creates an instance of FileSharerSpecs
     * 
     * @param maxFileCapacity - maximum number of files that can be stored
     * @param maxStorageCapacity - maximum number of files that can be stored
     * @param fileReceiveSpeed - download bandwidth (bytes/min)
     * @param fileSendSpeed - upload bandwidth (bytes/min)
     */
    public FileSharerSpecs(int maxFileCapacity, int maxStorageCapacity, int fileReceiveSpeed, int fileSendSpeed) {
        this.maxFileCapacity = maxFileCapacity;
        this.maxStorageCapacity = maxStorageCapacity;
        this.fileReceiveSpeed = fileReceiveSpeed;
        this.fileSendSpeed = fileSendSpeed;
    }

    /**
     * GETTER AND SETTER METHODS
     */
    
    /**
    * @return maximum number of files that can be stored
    */
    public int getMaxFileCapacity() {
        return maxFileCapacity;
    }
    
   
    /**
    * @return maximum number of files that can be stored
    */
    public int getMaxStorageCapacity() {
        return maxStorageCapacity;
    }
       
    /**
    * @return download bandwidth (bytes/min)
    */
    public int getFileReceiveSpeed() {
        return fileReceiveSpeed;
    }
   
    /**
    * @return upload bandwidth (bytes/min)
    */
    public int getFileSendSpeed() {
        return fileSendSpeed;
    }
    
}
