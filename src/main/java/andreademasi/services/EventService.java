package andreademasi.services;

import andreademasi.entities.Event;
import andreademasi.exceptions.NotFoundException;
import andreademasi.payloads.events.NewEventDTO;
import andreademasi.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepo;

    @Autowired
    private UserService userService;

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

}
