package andreademasi.controllers;

import andreademasi.entities.Event;
import andreademasi.exceptions.BadRequestException;
import andreademasi.payloads.events.NewEventDTO;
import andreademasi.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/events")
public class EventController {
    @Autowired
    private EventService eventService;

    @GetMapping("/{id}")
    Event findEventById(@PathVariable long id) {
        return eventService.findEventById(id);
    }

    @GetMapping
    Page<Event> getAllEvents(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "2") int size) {
        return eventService.getAllEvents(page, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ORGANIZZATORE_EVENTI')")
    Event save(@RequestBody @Validated NewEventDTO eventDTO, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        } else {
            try {
                return eventService.saveEvent(eventDTO);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @PreAuthorize("hasAuthority('ORGANIZZATORE_EVENTI')")
    void findEventByIdAndDelete(@PathVariable long id) {
        eventService.findEventByIdAndDelete(id);
    }

    @PostMapping("/upload/{id}")
    public Event uploadImg(@RequestParam("img") MultipartFile body, @PathVariable long id) throws IOException {
        System.out.println(body.getSize());
        System.out.println(body.getContentType());
        return eventService.uploadPicture(body, id);
    }
}
