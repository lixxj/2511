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
import java.util.HashMap;
import java.util.Map;

import static blackout.TestHelpers.assertListAreEqualIgnoringOrder;

@TestInstance(value = Lifecycle.PER_CLASS)

public class Task3Testing {

    /**
     * MY TESTS
     */
    
    @Test
    public void testElephantSatelliteFreeStorage() { 
        // Task 2
        BlackoutController controller = new BlackoutController();

        // Creates 2 satellite and 1 device that are out of range
        // Gets a device to send a file to a satellites and gets another device to download it.
        controller.createSatellite("Satellite", "ElephantSatellite", 10000 + RADIUS_OF_JUPITER, Angle.fromDegrees(0));
        controller.createDevice("DeviceA", "LaptopDevice", Angle.fromDegrees(0));

        String msg = "Sed ut perspiciatis unde omnis iste natus error sit voluptatem";
        String smallMsg = "Sed ut";

        controller.addFileToDevice("DeviceA", "File1", msg);
        controller.addFileToDevice("DeviceA", "File2", smallMsg);

        assertDoesNotThrow(() -> controller.sendFile("File1", "DeviceA", "Satellite"));
        assertEquals(new FileInfoResponse("File1", "", msg.length(), false), controller.getInfo("Satellite").getFiles().get("File1"));
        controller.simulate(msg.length());
        assertEquals(new FileInfoResponse("File1", "Sed ut perspiciatis unde omnis iste natus error sit voluptatem", msg.length(), true), controller.getInfo("Satellite").getFiles().get("File1"));
        controller.addFileToDevice("DeviceA", "File2", msg);

        // Map<String, FileInfoResponse> files = new HashMap<>();
        // files.put("File1", new FileInfoResponse("File1", "Sed ut perspiciatis unde omnis iste natus error sit voluptatem", msg.length(), true));
        // assertEquals(new EntityInfoResponse("Satellite", Angle.fromDegrees(111), 10000 + RADIUS_OF_JUPITER, "ElephantSatellite", files), controller.getInfo("Satellite"));


        // // Create a transient file
        // assertDoesNotThrow(() -> controller.sendFile("File2", "DeviceA", "Satellite"));
        // assertEquals(new FileInfoResponse("File2", "", smallMsg.length(), false), controller.getInfo("Satellite").getFiles().get("File2"));
        // controller.simulate();

        // // Send another file (and thereby remove the previous transient file to free storage)
        // assertDoesNotThrow(() -> controller.sendFile("File3", "DeviceA", "Satellite"));
        // assertEquals(new FileInfoResponse("File3", "", msg.length(), false), controller.getInfo("Satellite").getFiles().get("File3"));


    }

}
