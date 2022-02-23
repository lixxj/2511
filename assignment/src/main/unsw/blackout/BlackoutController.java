package unsw.blackout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import unsw.blackout.FileTransferException.VirtualFileAlreadyExistsException;
import unsw.blackout.FileTransferException.VirtualFileNoBandwidthException;
import unsw.blackout.FileTransferException.VirtualFileNoStorageSpaceException;
import unsw.blackout.FileTransferException.VirtualFileNotFoundException;
import unsw.response.models.EntityInfoResponse;
import unsw.utils.Angle;
import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;


public class BlackoutController {

    private Map<String, Satellite> allSatellites = new HashMap<>();
    private Map<String, Device> allDevices = new HashMap<>();

    public void createDevice(String deviceId, String type, Angle position) {
        if (Objects.equals(type, "HandheldDevice")) {
            allDevices.put(deviceId, new HandheldDevice(deviceId, type, position));
        } else if (Objects.equals(type, "LaptopDevice")) {
            allDevices.put(deviceId, new LaptopDevice(deviceId, type, position));
        } else if (Objects.equals(type, "DesktopDevice")) {
            allDevices.put(deviceId, new DesktopDevice(deviceId, type, position));
        } else if (Objects.equals(type, "CloudStorageDevice")) {
            allDevices.put(deviceId, new CloudStorageDevice(deviceId, type, position));
        } 
    }

    public void removeDevice(String deviceId) {
        allDevices.remove(deviceId);
    }

    public void createSatellite(String satelliteId, String type, double height, Angle position) {
        if (Objects.equals(type, "StandardSatellite")) {
            allSatellites.put(satelliteId, new StandardSatellite(satelliteId, type, height, position));
        } else if (Objects.equals(type, "ShrinkingSatellite")) {
            allSatellites.put(satelliteId, new ShrinkingSatellite(satelliteId, type, height, position));
        } else if (Objects.equals(type, "RelaySatellite")) {
            allSatellites.put(satelliteId, new RelaySatellite(satelliteId, type, height, position));
        } else if (Objects.equals(type, "ElephantSatellite")) {
            allSatellites.put(satelliteId, new ElephantSatellite(satelliteId, type, height, position));
        } 
    }

    public void removeSatellite(String satelliteId) {
        allSatellites.remove(satelliteId);
    }

    public List<String> listDeviceIds() {
        return new ArrayList<>(allDevices.keySet());
    }

    public List<String> listSatelliteIds() {
        return new ArrayList<>(allSatellites.keySet());
    }

    public void addFileToDevice(String deviceId, String filename, String content) {
        allDevices.get(deviceId).addNewFile(new File(filename, content, content.length()));
    }

    public EntityInfoResponse getInfo(String id) {
        if (allSatellites.containsKey(id)) {
            Satellite entity = allSatellites.get(id);
            if (entity instanceof FileHandler) {
                return new EntityInfoResponse(id, entity.getPosition(), entity.getHeight(), entity.getType(), ((FileHandler) entity).getFiles());
            }
            return new EntityInfoResponse(id, entity.getPosition(), entity.getHeight(), entity.getType());
            
        } else {
            Device entity = allDevices.get(id);
            return new EntityInfoResponse(id, entity.getPosition(), RADIUS_OF_JUPITER, entity.getType(), ((FileHandler) entity).getFiles());
        }
    }

    public void simulate() {
        for (Satellite satellite : allSatellites.values()) {
            satellite.moveSatellite();
            refreshFileTransfers();
        }
    }

    /**
     * Simulate for the specified number of minutes.
     * You shouldn't need to modify this function.
     */
    public void simulate(int numberOfMinutes) {
        for (int i = 0; i < numberOfMinutes; i++) {
            simulate();
        }
    }

    private void refreshFileTransfers() {
        for (Satellite satellite : allSatellites.values()) {
            if (satellite instanceof FileSharer) {
                FileSharer sat = (FileSharer) satellite;
                Map<String, FileHandler> uploadFiles = sat.getUploadQueue();
                Map<String, FileHandler> downloadFiles = sat.getDownloadQueue();

                if (uploadFiles.size() > 0) uploadBytes(sat, uploadFiles);
                if (downloadFiles.size() > 0) downloadBytes(sat, downloadFiles);
            }
        }
    }

    public List<String> communicableEntitiesInRange(String id) {
        List<String> allPossibleConnections = new ArrayList<>();
        Connectable entity = allSatellites.containsKey(id) ? allSatellites.get(id) : allDevices.get(id);
        helperCommunicableEntitiesInRange(entity, allPossibleConnections);
        
        if (allPossibleConnections.size() == 0) return allPossibleConnections;

        Iterator<String> itr = allPossibleConnections.iterator();
        while (itr.hasNext()) {
            String target = itr.next();
            Connectable targetEntity = allSatellites.containsKey(target) ? allSatellites.get(target) : allDevices.get(target);
            // Remove any entities that cannot be added due to entity specifications
            if (!(entity.allowedToConnect(targetEntity) && targetEntity.allowedToConnect(entity))) itr.remove();
        }

        if (allPossibleConnections.contains(id)) allPossibleConnections.remove(id);

        return allPossibleConnections;
    }

    private void helperCommunicableEntitiesInRange(Connectable entity, List<String> allPossibleConnections) {
        if (entity instanceof Satellite) {
            for (Device target : allDevices.values()) {
                if (inRange(entity, (Connectable) target)) {
                    if (!allPossibleConnections.contains(target.getDeviceID())) allPossibleConnections.add(target.getDeviceID());
                }
            }
            for (Satellite target : allSatellites.values()) {
                if (!target.equals(entity) && inRange(entity, (Connectable) target)) {
                    if (!allPossibleConnections.contains(target.getSatelliteId())) {
                        allPossibleConnections.add(target.getSatelliteId());
                        if (target instanceof RelaySatellite) helperCommunicableEntitiesInRange(target, allPossibleConnections);
                    }
                }
            }
        } else {
            for (Satellite target : allSatellites.values()) {
                if (inRange(entity, (Connectable) target)) {
                    if (!allPossibleConnections.contains(target.getSatelliteId())) {
                        allPossibleConnections.add(target.getSatelliteId());
                        if (target instanceof RelaySatellite) helperCommunicableEntitiesInRange(target, allPossibleConnections);
                    }
                }
            }
        }
    }

    private boolean inRange(Connectable a, Connectable b) {
        return a.canConnect(b) && b.canConnect(a);
    }

    public void sendFile(String fileName, String fromId, String toId) throws FileTransferException {
        if ((allSatellites.containsKey(fromId)) 
            || allDevices.containsKey(fromId)) {
            
            sendFileExceptions(fileName, fromId, toId);

            FileHandler sender = getFileHandlerFromId(fromId);
            FileHandler receiver = getFileHandlerFromId(toId);
            
            File newFile = new File(fileName, "", sender.getFile(fileName).getFileSize());

            // Case 1: Device --> Satellite
            if (!(sender instanceof FileSharer) && (receiver instanceof FileSharer)) { 
                // If (sender instanceof CloudStorageDevice) { zip file }             
                FileSharer downloadingEntity = (FileSharer) receiver;
                downloadingEntity.queueFileDownload(fileName, sender);
            } else if ((sender instanceof FileSharer)) { 
                FileSharer uploadingEntity = (FileSharer) sender;
                if (!(receiver instanceof FileSharer)) { // Case 2: Satellite --> Device
                    uploadingEntity.queueFileUpload(fileName, receiver);  
                } else { // Case 3: Satellite --> Satellite
                    FileSharer downloadingEntity = (FileSharer) receiver;
                    if (uploadingEntity.getUploadSpeed() < downloadingEntity.getDownloadSpeed()) {
                        uploadingEntity.queueFileUpload(fileName, receiver);
                    } else {
                        downloadingEntity.queueFileDownload(fileName, sender);
                    }
                }

            }

            receiver.addNewFile(newFile);

        } 
    }

    private FileHandler getFileHandlerFromId(String id) {
        return (allSatellites.containsKey(id)) ? (FileHandler) allSatellites.get(id) : (FileHandler) allDevices.get(id);
    }

    private void sendFileExceptions(String fileName, String fromId, String toId) throws FileTransferException {

        // Cannot send a file to a Satellite that is not a FileHandler (i.e. RelaySatellite)
        if (allSatellites.containsKey(toId) && !(allSatellites.get(toId) instanceof FileHandler)) {
            throw new VirtualFileNoStorageSpaceException("Max Files Reached");
        }

        FileHandler sender = getFileHandlerFromId(fromId);
        FileHandler receiver = getFileHandlerFromId(toId);

        if (!sender.containsFile(fileName) || !sender.getFile(fileName).transferComplete()) { // Check if sender can upload file
            throw new VirtualFileNotFoundException(fileName);
        } else if (receiver.containsFile(fileName)) { // Check if receiver is already downloading file
            throw new VirtualFileAlreadyExistsException(fileName);
        }
                    
        if (sender instanceof FileSharer) { // Satellite is sending file
            FileSharer fileSender = (FileSharer) sender;
            if (!fileSender.canUpload()) {
                throw new VirtualFileNoBandwidthException(fromId);
            }
        }

        if (receiver instanceof FileSharer) { // Satellite is receiving file
            FileSharer fileReceiver = (FileSharer) receiver;
            if (!fileReceiver.canDownload()) {
                throw new VirtualFileNoBandwidthException(fromId);
            } else if (fileReceiver.availableFileSpace() == 0) {
                throw new VirtualFileNoStorageSpaceException("Max Files Reached");
            } else if (fileReceiver.availableStorage() < sender.getFile(fileName).getFileSize()) {
                int requiredSpace = sender.getFile(fileName).getFileSize() - fileReceiver.availableStorage();
                if (receiver instanceof ElephantSatellite && ((ElephantSatellite) receiver).canFreeSpace(requiredSpace)) {
                    ((ElephantSatellite) receiver).freeSpace(requiredSpace);
                } else {
                    throw new VirtualFileNoStorageSpaceException("Max Storage Reached");
                }
            } 
        }
    }

    private void uploadBytes(FileSharer sender, Map<String, FileHandler> uploadFiles) {
        int uploadBandwidth = Math.floorDiv(sender.getUploadSpeed(), uploadFiles.size());

        for (Map.Entry<String, FileHandler> fileEntry : uploadFiles.entrySet()) {
            File file = sender.getFile(fileEntry.getKey());
            String fileName = file.getFileName();
            FileHandler receipient = fileEntry.getValue();

            // Connection lost with Connectable Object - Delete File
            if (!inRange((Connectable) sender, (Connectable) receipient)) {
                uploadFiles.remove(fileName);
                receipient.removeFile(fileName);
                continue;
            } 

            int originalFileSize = file.getFileSize();
            int currentSize = receipient.getFile(fileName).getFileSize();
            int upToIndex = ((currentSize + uploadBandwidth) <= originalFileSize) ? 
                            (currentSize + uploadBandwidth) : originalFileSize;

            receipient.addToExistingFile(fileName, file.getFileContent().substring(currentSize, upToIndex));

            if (receipient.getFile(fileName).transferComplete()) {
                // if (receipient.getFile(fileName).isCompressed() && receipient instanceof Device 
                //      && !(receipient instanceof CloudStorageDevice)) {
                //      receipient.getFile(fileName).unzip()
                // }
                uploadFiles.remove(fileName);
            }
        }
    }

    private void downloadBytes(FileSharer receipient, Map<String, FileHandler> downloadFiles) {
        int downloadBandwidth = Math.floorDiv(receipient.getDownloadSpeed(), downloadFiles.size());

        for (Map.Entry<String, FileHandler> fileEntry : downloadFiles.entrySet()) {
            String fileName = fileEntry.getKey();
            FileHandler sender = fileEntry.getValue();
            File file = sender.getFile(fileEntry.getKey());
            File downloadedFile = receipient.getFile(fileName);

            // Connection lost with Connectable Object
            if (!inRange((Connectable) sender, (Connectable) receipient)) {
                if (receipient instanceof ElephantSatellite) {
                    downloadedFile.setTransient(true);
                } else { 
                    receipient.removeFile(fileName);
                }
                continue;
            } 
            
            if (downloadedFile.isTransient()) downloadedFile.setTransient(false);

            int originalFileSize = file.getFileSize();
            int currentSize = receipient.getFile(fileName).getFileSize();
            int upToIndex = ((currentSize + downloadBandwidth) <= originalFileSize) ? 
                            (currentSize + downloadBandwidth) : originalFileSize;

            receipient.addToExistingFile(fileName, file.getFileContent().substring(currentSize, upToIndex));

            if (receipient.getFile(fileName).transferComplete()) {
                downloadFiles.remove(fileName);
                receipient.getFile(fileName).setTransient(false);
            }          
        }
    }
}
