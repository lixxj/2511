## Design
### Communicable Entities
The recursive implementation to find all communicable entities ensures that for all
connected Relay Satellites, the recursive loop adds all of the satellite's
communicable entities as well. Since this does not account for the constraints
placed on an entity's ability to connect to another entity, an iterator is implemented
to remove all entities that cannot connect to the original entity (even when in range).

### File Handler and File Sharer Interface
Abstraction of the FileSharer and FileHandler properties into interfaces is probably
one of the main design features that had to be considered for this task. Whilst there
still remains some identical code for objects implementing these interfaces, it was
difficult to find a way around this. To minimise the repetition, however, the
FileSharingSatellite abstract class was created and this allowed me to collate 
similar FileSharer methods for any Satellite that was involved in the uploading and
downloading of files.

These two interfaces are included to account for all entities that are able to hold
files and those that are also able to share those files. Since devices cannot share
files, it was important for the FileSharer interface to be extended from the
FileHandler interface (a design strategy supported by the Interface segregation
principle). This allows all FileSharingSatellites to have a specific set
of methods that are required whilst sharing files.

The program makes use of upload queues and download queues- both of which are stored
in the FileSharingSatellite involved. This method ensures that during each simulation,
all files that are uploading/downloading can continue doing so.

The FileSharerSpecs class was later created to further improve code design 
and abstract the satellite specifications into a separate 'info' class.

## Task 3 - Elephant Satellite and Cloud Storage Device
While I was not able to complete Task 3 (under the deadline constraints), some
considerations were made in terms of how it would have been completed. The current
implementation completes the transient status of an Elephant Satellite and all that
remains is removing transient files until the required number of bytes is less than
or equal to 0. Possible considerations included (but were not limited to):
    - assigning files with priority levels when they were created and then removing
    transient files based on their priority level
    - removing files that would take the most time to get in range first
    - removing files based on their file size

The implementation of a Cloud Storage Device appeared much simpler in design since all
that would be required was to zip all files that were added to the Cloud Storage
Device and unzip any zipped files that were received by other types of devices. Refer to
the code snippets included in BlackoutController.java:176 and BlackoutController.java:266-269.