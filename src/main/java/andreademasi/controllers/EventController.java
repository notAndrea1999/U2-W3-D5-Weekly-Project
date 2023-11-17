package andreademasi.controllers;

import andreademasi.entities.Event;
import andreademasi.exceptions.BadRequestException;
import andreademasi.payloads.events.NewEventDTO;
import andreademasi.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/devices")
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
    void findEventByIdAndDelete(@PathVariable long id) {
        eventService.findEventByIdAndDelete(id);
    }
}
