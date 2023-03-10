package unsw.blackout;

import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import unsw.blackout.FileTransferException.VirtualFileAlreadyExistsException;
import unsw.blackout.FileTransferException.VirtualFileNoBandwidthException;
import unsw.blackout.FileTransferException.VirtualFileNoStorageSpaceException;
import unsw.blackout.FileTransferException.VirtualFileNotFoundException;
import unsw.response.models.EntityInfoResponse;
import unsw.utils.Angle;

public class BlackoutController {

    private Map<String, Device> Devices = new HashMap<>();
    private Map<String, Satellite> Satellites = new HashMap<>();

    public void createDevice(String deviceId, String type, Angle position) {
        Device newDevice;
        
        // do not need to account for invalid inputs
        if (type.equals("HandheldDevice")) {            
            newDevice = new HandheldDevice(deviceId, position);
        }
        else if (type.equals("LaptopDevice")) {            
            newDevice = new LaptopDevice(deviceId, position);
        }
        else { // if (type.equals("DesktopDevice"))
            newDevice = new DesktopDevice(deviceId, position);
        }

        Devices.put(deviceId, newDevice);
    }

    public void removeDevice(String deviceId) {
        Devices.remove(deviceId);
    }

    public void createSatellite(String satelliteId, String type, double height, Angle position) {
        Satellite newSatellite;

        // do not need to account for invalid inputs
        if (type.equals("StandardSatellite")) {
            newSatellite = new StandardSatellite(satelliteId, height, position);
        }
        else if (type.equals("TeleportingSatellite")) {
            newSatellite = new TeleportingSatellite(satelliteId, height, position);
        }
        else { // if (type.equals("RelaySatellite"))
            newSatellite = new RelaySatellite(satelliteId, height, position);
        }

        Satellites.put(satelliteId, newSatellite);
    }

    public void removeSatellite(String satelliteId) {
        Satellites.remove(satelliteId);
    }

    public List<String> listDeviceIds() {
        return new ArrayList<>(Devices.keySet());
    }

    public List<String> listSatelliteIds() {
        return new ArrayList<>(Satellites.keySet());
    }

    public void addFileToDevice(String deviceId, String filename, String content) {
        File newFile = new File(filename, content, content.length());
        Devices.get(deviceId).addFile(newFile);
    }

    public EntityInfoResponse getInfo(String id) {
        if (Devices.containsKey(id)) { // if Entity is a device
            Device entity = Devices.get(id);
            return new EntityInfoResponse(id, entity.getPosition(), RADIUS_OF_JUPITER, entity.getType(), ((StorageEntity) entity).getFiles());
        }
        else { // if Entity is a Satellite
            Satellite entity = Satellites.get(id);

            // if Entity store files
            if (entity instanceof StorageEntity) return new EntityInfoResponse(id, entity.getPosition(), entity.getHeight(), entity.getType(), ((StorageEntity) entity).getFiles());
            
            // if Entity cannot store files (TeleportingSatallite)
            return new EntityInfoResponse(id, entity.getPosition(), entity.getHeight(), entity.getType());
        } 
    }

    public void simulate(int numberOfMinutes) {
        for (int i = 0; i < numberOfMinutes; i++) {
            simulate();
        }
    }

    public void simulate() {
        for (Satellite satellite : Satellites.values()) {
            satellite.moveSatellite(); // move satellites

            // transfer files
            for (Satellite s : Satellites.values()) {
                if (s instanceof StorageSatellite) { // if not RelaySatellite
                    StorageSatellite sat = (StorageSatellite) s;
                    if (sat.getUploadQueue().size() > 0) upload(sat, sat.getUploadQueue());
                    if (sat.getDownloadQueue().size() > 0) download(sat, sat.getDownloadQueue());
                }
            }
        }
    }

    private boolean inRange(Entity a, Entity b) {
        return (a.connectable(b) && b.connectable(a));
    }

    private void upload(StorageSatellite sender, Map<String, StorageEntity> Files) {
        int Bandwidth = Math.floorDiv(sender.getUploadSpeed(), Files.size());

        for (Map.Entry<String, StorageEntity> fileEntry : Files.entrySet()) {
            File file = sender.getFile(fileEntry.getKey());
            String fileName = file.getFileName();
            StorageEntity receiver = fileEntry.getValue();
            
            if (!(inRange((Entity) sender, (Entity) receiver))) { // Connection lost
                // Connection lost due to teleporting
                if (sender instanceof TeleportingSatellite || receiver instanceof TeleportingSatellite) {
                    
                    // If a file transfer from a satellite to a device or a satellite to another satellite
                    if (sender instanceof Satellite) {
                        // the rest of the file is instantly downloaded
                        receiver.getFile(fileName).setFileContent(file.getFileContent());
                        Files.remove(fileName);
                        // all "t" bytes are removed from the remaining bytes to be sent
                        receiver.getFile(fileName).removetBytes();
                        receiver.getFile(fileName).resetFileSizeFull();
                    }

                    // If a file transfer from a device to a satellite
                    if (!(sender instanceof Satellite)) {
                        // the download fails and the partially uploaded file is removed from the satellite
                        receiver.removeFile(fileName);
                        Files.remove(fileName);
                        // and all "t" bytes are removed from the file on the device
                        sender.getFile(fileName).removetBytes();
                        sender.getFile(fileName).resetFileSizeFull();
                    }
                    continue;
                }
                else {
                    receiver.removeFile(fileName);
                    Files.remove(fileName);
                    continue;
                }        
            } 

            int currentSize = receiver.getFile(fileName).getFileSize();
            int upTo = ((currentSize + Bandwidth) <= file.getFileSize()) ? (currentSize + Bandwidth) : file.getFileSize();
            receiver.updateFile(fileName, file.getFileContent().substring(currentSize, upTo));
            
            if (receiver.getFile(fileName).transferComplete()) Files.remove(fileName);
        }
    }

    private void download(StorageSatellite receiver, Map<String, StorageEntity> Files) {
        int Bandwidth = Math.floorDiv(receiver.getDownloadSpeed(), Files.size());

        for (Map.Entry<String, StorageEntity> fileEntry : Files.entrySet()) {
            String fileName = fileEntry.getKey();
            StorageEntity sender = fileEntry.getValue();
            File file = sender.getFile(fileEntry.getKey());

            if (!inRange((Entity) sender, (Entity) receiver)) { // Connection lost
                // Connection lost due to teleporting
                if (sender instanceof TeleportingSatellite || receiver instanceof TeleportingSatellite) {
                    
                    // If a file transfer from a satellite to a device or a satellite to another satellite
                    if (sender instanceof Satellite) {
                        // the rest of the file is instantly downloaded
                        receiver.getFile(fileName).setFileContent(file.getFileContent());
                        Files.remove(fileName);
                        // all "t" bytes are removed from the remaining bytes to be sent
                        receiver.getFile(fileName).removetBytes();
                        receiver.getFile(fileName).resetFileSizeFull();
                    }

                    // If a file transfer from a device to a satellite
                    if (!(sender instanceof Satellite)) {
                        // the download fails and the partially uploaded file is removed from the satellite
                        receiver.removeFile(fileName);
                        Files.remove(fileName);
                        // and all "t" bytes are removed from the file on the device
                        sender.getFile(fileName).removetBytes();
                        sender.getFile(fileName).resetFileSizeFull();
                    }
                    continue;
                }
                else {
                    receiver.removeFile(fileName);
                    Files.remove(fileName);
                    continue;
                }              
            }

            int currentSize = receiver.getFile(fileName).getFileSize();
            int upTo = ((currentSize + Bandwidth) <= file.getFileSize()) ? (currentSize + Bandwidth) : file.getFileSize();
            receiver.updateFile(fileName, file.getFileContent().substring(currentSize, upTo));
            
            if (receiver.getFile(fileName).transferComplete()) Files.remove(fileName);         
        }
    }

    private void findCommunicableEntitiesInRange(Entity entity, List<String> communicableEntitiesList) {
        
        if (entity instanceof Device) {
            // loop through Satellites
            for (Satellite target : Satellites.values()) {
                if ((inRange(entity, (Entity) target)) && (!communicableEntitiesList.contains(target.getSatelliteId()))) {
                    communicableEntitiesList.add(target.getSatelliteId());
                    // recursion to findCommunicableEntitiesInRange through RelaySatellite
                    if (target instanceof RelaySatellite) findCommunicableEntitiesInRange(target, communicableEntitiesList);
                }
            }
        }

        if (entity instanceof Satellite) {
            // loop through all Entities
            for (Satellite target : Satellites.values()) {
                if ((!target.equals(entity) && inRange(entity, (Entity) target)) && (!communicableEntitiesList.contains(target.getSatelliteId()))) {
                    communicableEntitiesList.add(target.getSatelliteId());
                    // recursion to findCommunicableEntitiesInRange through RelaySatellite
                    if (target instanceof RelaySatellite) findCommunicableEntitiesInRange(target, communicableEntitiesList);
                }
            }
            for (Device target : Devices.values()) {
                if ((inRange(entity, (Entity) target)) && (!communicableEntitiesList.contains(target.getDeviceId()))) communicableEntitiesList.add(target.getDeviceId());
            }
        }    
    }

    public List<String> communicableEntitiesInRange(String id) {
        Entity entity = Devices.containsKey(id) ? Devices.get(id) : Satellites.get(id);
        
        List<String> communicableEntitiesList = new ArrayList<>();
        findCommunicableEntitiesInRange(entity, communicableEntitiesList);

        // no communicableEntitiesInRange
        if (communicableEntitiesList.size() == 0) return communicableEntitiesList; 

        // if contains target itself
        if (communicableEntitiesList.contains(id)) communicableEntitiesList.remove(id); 

        Iterator<String> i = communicableEntitiesList.iterator();
        while (i.hasNext()) {
            String target = i.next();
            Entity targetEntity = Devices.containsKey(target) ? Devices.get(target) : Satellites.get(target);
            if (!(entity.allowedToConnect(targetEntity) && targetEntity.allowedToConnect(entity))) i.remove();
        }

        return communicableEntitiesList;
    }

    public void sendFile(String fileName, String fromId, String toId) throws FileTransferException {
        if ((Satellites.containsKey(fromId)) || Devices.containsKey(fromId)) {
            
            // RelaySatellite has no storage
            if (Satellites.containsKey(toId) && !(Satellites.get(toId) instanceof StorageEntity))  throw new VirtualFileNoStorageSpaceException("Max Files Reached"); 

            StorageEntity from = (Satellites.containsKey(fromId)) ? (StorageEntity) Satellites.get(fromId) : (StorageEntity) Devices.get(fromId);
            StorageEntity to = (Satellites.containsKey(toId)) ? (StorageEntity) Satellites.get(toId) : (StorageEntity) Devices.get(toId);

            // sender can upload file
            if (!from.hasFile(fileName) || !from.getFile(fileName).transferComplete()) throw new VirtualFileNotFoundException(fileName); 
            
            // receiver is downloading file
            if (to.hasFile(fileName)) throw new VirtualFileAlreadyExistsException(fileName); 
                        
            if (from instanceof StorageSatellite) {
                StorageSatellite sender = (StorageSatellite) from;
                if (!sender.canUpload()) throw new VirtualFileNoBandwidthException(fromId);
            }

            if (to instanceof StorageSatellite) {
                StorageSatellite receiver = (StorageSatellite) to;
                if (!receiver.canDownload()) throw new VirtualFileNoBandwidthException(fromId);
                if (receiver.availableFileSpace() == 0) throw new VirtualFileNoStorageSpaceException("Max Files Reached");
                if (receiver.availableStorage() < from.getFile(fileName).getFileSize()) throw new VirtualFileNoStorageSpaceException("Max Storage Reached");
            }
            
            if (from instanceof StorageSatellite) { 
                StorageSatellite uploadingEntity = (StorageSatellite) from;
                
                // Satellite to Device
                if (to instanceof Device) uploadingEntity.addToUploadQueue(fileName, to); 
                
                // Satellite to Satellite
                if (to instanceof StorageSatellite) { 
                    StorageSatellite downloadingEntity = (StorageSatellite) to;
                    if (uploadingEntity.getUploadSpeed() < downloadingEntity.getDownloadSpeed()) uploadingEntity.addToUploadQueue(fileName, to);
                    if (uploadingEntity.getUploadSpeed() >= downloadingEntity.getDownloadSpeed()) downloadingEntity.addToDownloadQueue(fileName, from);
                }
            }

            // Device to Satellite
            if ((from instanceof Device) && (to instanceof StorageSatellite)) { 
                StorageSatellite downloadingEntity = (StorageSatellite) to;
                downloadingEntity.addToDownloadQueue(fileName, from);
            } 
            to.addFile(new File(fileName, "", from.getFile(fileName).getFileSize()));
        } 
    }

    public void createDevice(String deviceId, String type, Angle position, boolean isMoving) {
        createDevice(deviceId, type, position);
        // TODO: Task 3
    }

    public void createSlope(int startAngle, int endAngle, int gradient) {
        // TODO: Task 3
        // If you are not completing Task 3 you can leave this method blank :)
    }

}
