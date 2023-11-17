package andreademasi.services;

import andreademasi.entities.Event;
import andreademasi.exceptions.NotFoundException;
import andreademasi.payloads.events.NewEventDTO;
import andreademasi.repositories.EventRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private Cloudinary cloudinary;

    public Event findEventById(long id) {
        return eventRepo.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Page<Event> getAllEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return eventRepo.findAll(pageable);
    }

    public Event saveEvent(NewEventDTO eventDTO) throws IOException {
        //User foundUser = userService.findUserById(eventDTO.userId());
        Event newEvent = new Event();
        newEvent.setTitle(eventDTO.title());
        newEvent.setDescription(eventDTO.description());
        newEvent.setDate(eventDTO.date());
        newEvent.setPlace(eventDTO.place());
        newEvent.setSeats(eventDTO.seats());
        //newEvent.setUser(foundUser);
        return eventRepo.save(newEvent);
    }


    public void findEventByIdAndDelete(long id) {
        Event foundEvent = this.findEventById(id);
        eventRepo.delete(foundEvent);
    }

    public Event uploadPicture(MultipartFile file, @PathVariable long id) throws IOException {
        Event foundEvent = this.findEventById(id);
        String cloudinaryURL = (String) cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
        foundEvent.setImg(cloudinaryURL);
        return eventRepo.save(foundEvent);
    }


}
