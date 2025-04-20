package org.traccar.api.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.traccar.api.BaseObjectResource;
import org.traccar.model.Device;
import org.traccar.model.Group;
import org.traccar.storage.StorageException;
import org.traccar.storage.query.Columns;
import org.traccar.storage.query.Condition;
import org.traccar.storage.query.Request;

/**
 * This is an open endpoint, secured by uniqueId. A device can query their configuration in the server.
 */
@Path("my-config")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MyConfigResource extends BaseObjectResource<Device> {

    public MyConfigResource() {
        super(Device.class);
    }

    @GET
    public DeviceAndGroup get(@QueryParam(value = "uniqueId") String uniqueId) throws StorageException {

        if (uniqueId == null) {
            throw new BadRequestException("uniqueId query parameter is required");
        }

        var device = storage.getObject(Device.class, new Request(
                new Columns.All(),
                new Condition.Equals("uniqueId", uniqueId)));

        if (device == null) {
            throw new NotFoundException("no such device with uniqueId: " + uniqueId);
        }

        Group group = storage.getObject(Group.class, new Request(
                new Columns.All(),
                new Condition.Equals("id", device.getGroupId())));

        return new DeviceAndGroup(device, group);
    }

    public record DeviceAndGroup(Device device, Group group) {
    }
}
