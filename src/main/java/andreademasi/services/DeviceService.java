package andreademasi.services;

import andreademasi.entities.Device;
import andreademasi.entities.DeviceStatus;
import andreademasi.entities.User;
import andreademasi.exceptions.NotFoundException;
import andreademasi.payloads.devices.NewDeviceDTO;
import andreademasi.repositories.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DeviceService {
    @Autowired
    private DeviceRepository deviceRepo;

    @Autowired
    private UserService userService;

    public Device findDeviceById(long id) {
        return deviceRepo.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Page<Device> getAllDevices(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return deviceRepo.findAll(pageable);
    }

    public Device saveDevice(NewDeviceDTO deviceDTO) throws IOException {
        User foundUser = userService.findUserById(deviceDTO.userId());
        Device newDevice = new Device();
        newDevice.setDeviceName(deviceDTO.deviceName());
        newDevice.setDeviceStatus(deviceDTO.deviceStatus());
        newDevice.setUser(foundUser);
        if (newDevice.getDeviceStatus() == DeviceStatus.DISPONIBILE) {
            //newDevice.setDeviceStatus(DeviceStatus.ASSEGANTO);
            return deviceRepo.save(newDevice);
        } else if (newDevice.getDeviceStatus() == DeviceStatus.ASSEGANTO) {

            return deviceRepo.save(newDevice);
        } else {
            newDevice.setUser(null);
            return deviceRepo.save(newDevice);
        }

    }


    public Device findDeviceByIdAndUpdate(long id, NewDeviceDTO deviceDTO) {
        User foundUser = userService.findUserById(deviceDTO.userId());
        Device foundDevice = this.findDeviceById(id);
        foundDevice.setDeviceName(deviceDTO.deviceName());
        foundDevice.setDeviceStatus(deviceDTO.deviceStatus());
        foundDevice.setUser(foundUser);
        return deviceRepo.save(foundDevice);
    }

    public void findDeviceByIdAndDelete(long id) {
        Device foundDevice = this.findDeviceById(id);
        deviceRepo.delete(foundDevice);
    }

}
