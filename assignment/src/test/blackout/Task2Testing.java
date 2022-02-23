package blackout;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import unsw.blackout.BlackoutController;
import unsw.blackout.FileTransferException;
import unsw.blackout.FileTransferException.VirtualFileNoBandwidthException;
import unsw.blackout.FileTransferException.VirtualFileNoStorageSpaceException;
import unsw.response.models.FileInfoResponse;
import unsw.response.models.EntityInfoResponse;
import unsw.utils.Angle;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

import java.util.Arrays;

import static blackout.TestHelpers.assertListAreEqualIgnoringOrder;

@TestInstance(value = Lifecycle.PER_CLASS)

public class Task2Testing {

    /**
     * MY TESTS
     */
    
    @Test
    public void testRelaySatelliteMovement() {
        // Task 2 (and a bit of Task 3)
        BlackoutController controller = new BlackoutController();

        // Creates a relay satellite 
        // Ensures that the movement of a relay satellite is restricted between 140 and 190 degrees
        controller.createSatellite("Satellite", "RelaySatellite", 100 + RADIUS_OF_JUPITER, Angle.fromDegrees(0));
        assertEquals(new EntityInfoResponse("Satellite", Angle.fromDegrees(0), 100 + RADIUS_OF_JUPITER, "RelaySatellite"), controller.getInfo("Satellite"));
        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite", Angle.fromDegrees(1), 100 + RADIUS_OF_JUPITER, "RelaySatellite"), controller.getInfo("Satellite"));
        controller.simulate(154);
        assertEquals(new EntityInfoResponse("Satellite", Angle.fromDegrees(190), 100 + RADIUS_OF_JUPITER, "RelaySatellite"), controller.getInfo("Satellite"));
        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite", Angle.fromDegrees(189), 100 + RADIUS_OF_JUPITER, "RelaySatellite"), controller.getInfo("Satellite"));
        controller.simulate(40);
        assertEquals(new EntityInfoResponse("Satellite", Angle.fromDegrees(140), 100 + RADIUS_OF_JUPITER, "RelaySatellite"), controller.getInfo("Satellite"));
        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite", Angle.fromDegrees(141), 100 + RADIUS_OF_JUPITER, "RelaySatellite"), controller.getInfo("Satellite"));
    }

    @Test
    public void testRelaySatelliteConnection() {
        // Task 2 (and a bit of Task 3)
        BlackoutController controller = new BlackoutController();

        // Creates 4 satellites and 3 devices
        // Tests communicability using relay satellites as an extension for the connection
        controller.createSatellite("Satellite1", "RelaySatellite", 90000, Angle.fromDegrees(0));
        controller.createSatellite("Satellite2", "RelaySatellite", 90000, Angle.fromDegrees(50));
        controller.createSatellite("Satellite3", "StandardSatellite", 90000, Angle.fromDegrees(90));
        controller.createSatellite("Satellite4", "ElephantSatellite", 90000, Angle.fromDegrees(70));

        controller.createDevice("iPhone", "HandheldDevice", Angle.fromDegrees(0));
        controller.createDevice("iMac", "DesktopDevice", Angle.fromDegrees(45));
        controller.createDevice("Surface Book", "LaptopDevice", Angle.fromDegrees(80));

        assertListAreEqualIgnoringOrder(Arrays.asList("Satellite1", "Satellite2", "Satellite3"), controller.communicableEntitiesInRange("iPhone"));
        assertListAreEqualIgnoringOrder(Arrays.asList("Satellite1", "Satellite2", "Satellite4"), controller.communicableEntitiesInRange("iMac"));
        assertListAreEqualIgnoringOrder(Arrays.asList("Satellite1", "Satellite2", "Satellite3", "Satellite4"), controller.communicableEntitiesInRange("Surface Book"));

        assertListAreEqualIgnoringOrder(Arrays.asList("iPhone", "Surface Book", "iMac", "Satellite2", "Satellite3", "Satellite4"), controller.communicableEntitiesInRange("Satellite1"));
        assertListAreEqualIgnoringOrder(Arrays.asList("iPhone", "Surface Book", "iMac", "Satellite1", "Satellite3", "Satellite4"), controller.communicableEntitiesInRange("Satellite2"));
        assertListAreEqualIgnoringOrder(Arrays.asList("iPhone", "Surface Book", "Satellite1", "Satellite2", "Satellite4"), controller.communicableEntitiesInRange("Satellite3"));
        assertListAreEqualIgnoringOrder(Arrays.asList("iMac", "Surface Book", "Satellite1", "Satellite2", "Satellite3"), controller.communicableEntitiesInRange("Satellite4"));

    }

    @Test
    public void testMaxBandwidth() {
        // Task 2
        BlackoutController controller = new BlackoutController();

        // Creates 1 satellite and 1 device that are out of range
        // Gets a device to send a file to a satellites and gets another device to download it.
        // StandardSatellites are slow and transfer 1 byte per minute.
        controller.createSatellite("Satellite", "StandardSatellite", 10000 + RADIUS_OF_JUPITER, Angle.fromDegrees(0));
        controller.createDevice("DeviceA", "LaptopDevice", Angle.fromDegrees(0));
        controller.createDevice("DeviceB", "HandheldDevice", Angle.fromDegrees(0));

        controller.addFileToDevice("DeviceA", "File1", "a");
        controller.addFileToDevice("DeviceA", "File2", "b");
        
        assertDoesNotThrow(() -> controller.sendFile("File1", "DeviceA", "Satellite"));
        assertThrows(VirtualFileNoBandwidthException.class, () -> controller.sendFile("File2", "DeviceA", "Satellite"));
        controller.simulate();
        assertDoesNotThrow(() -> controller.sendFile("File2", "DeviceA", "Satellite"));
        controller.simulate();

        assertDoesNotThrow(() -> controller.sendFile("File1", "Satellite", "DeviceB"));
        assertThrows(VirtualFileNoBandwidthException.class, () -> controller.sendFile("File2", "Satellite", "DeviceB"));
        controller.simulate();
        assertDoesNotThrow(() -> controller.sendFile("File2", "Satellite", "DeviceB"));
        controller.simulate();
    }

    @Test
    public void testMaxFilesReached() {
        // Task 2
        BlackoutController controller = new BlackoutController();

        // Creates 1 satellite and 1 device that are out of range
        // Gets a device to send a file to a satellites and gets another device to download it.
        controller.createSatellite("Satellite", "StandardSatellite", 10000 + RADIUS_OF_JUPITER, Angle.fromDegrees(0));
        controller.createDevice("DeviceA", "LaptopDevice", Angle.fromDegrees(0));
        
        controller.addFileToDevice("DeviceA", "File1", "a");
        controller.addFileToDevice("DeviceA", "File2", "b");
        controller.addFileToDevice("DeviceA", "File3", "c");
        controller.addFileToDevice("DeviceA", "File4", "d");
        
        assertDoesNotThrow(() -> controller.sendFile("File1", "DeviceA", "Satellite"));
        controller.simulate();
        assertDoesNotThrow(() -> controller.sendFile("File2", "DeviceA", "Satellite"));
        controller.simulate();
        assertDoesNotThrow(() -> controller.sendFile("File3", "DeviceA", "Satellite"));
        controller.simulate();

        assertThrows(VirtualFileNoStorageSpaceException.class, () -> controller.sendFile("File4", "DeviceA", "Satellite"));
    }

    @Test
    public void testMaxStorageReached() {
        // Task 2
        BlackoutController controller = new BlackoutController();

        // Creates 1 satellite and 1 device that are out of range
        // Gets a device to send a file to a satellites and gets another device to download it.
        controller.createSatellite("Satellite", "StandardSatellite", 10000 + RADIUS_OF_JUPITER, Angle.fromDegrees(0));
        controller.createDevice("DeviceA", "LaptopDevice", Angle.fromDegrees(0));
        
        String msg = "Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit," + 
        "sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem";

        controller.addFileToDevice("DeviceA", "File1", msg);
        assertThrows(VirtualFileNoStorageSpaceException.class, () -> controller.sendFile("File1", "DeviceA", "Satellite"));
        
    }

    @Test
    public void testConnectionLost() {
        // Task 2
        BlackoutController controller = new BlackoutController();

        // Creates 2 satellite and 1 device that are out of range
        // Gets a device to send a file to a satellites and gets another device to download it.
        controller.createSatellite("Satellite1", "StandardSatellite", 10000 + RADIUS_OF_JUPITER, Angle.fromDegrees(0));
        controller.createSatellite("Satellite2", "ElephantSatellite", 10000 + RADIUS_OF_JUPITER, Angle.fromDegrees(0));
        controller.createDevice("DeviceA", "LaptopDevice", Angle.fromDegrees(180));

        String msg = "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium";
        controller.addFileToDevice("DeviceA", "RandomFile", msg);
        assertDoesNotThrow(() -> controller.sendFile("RandomFile", "DeviceA", "Satellite1"));
        assertEquals(new FileInfoResponse("RandomFile", "", msg.length(), false), controller.getInfo("Satellite1").getFiles().get("RandomFile"));

        assertDoesNotThrow(() -> controller.sendFile("RandomFile", "DeviceA", "Satellite2"));
        assertEquals(new FileInfoResponse("RandomFile", "", msg.length(), false), controller.getInfo("Satellite2").getFiles().get("RandomFile"));

        controller.simulate(msg.length());
        assertEquals(null, controller.getInfo("Satellite1").getFiles().get("RandomFile"));
        // file marked as transient so it still exists on the satellite
        assertEquals(new FileInfoResponse("RandomFile", "", msg.length(), false), controller.getInfo("Satellite2").getFiles().get("RandomFile"));

    }

}
