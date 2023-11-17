package andreademasi.payloads.events;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record NewEventDTO(
        @NotEmpty(message = "Il titolo e' un campo obbligatorio")
        String title,
        @NotEmpty(message = "La descrizione e' un campo obbligatorio")
        String description,

        LocalDate date,

        @NotEmpty(message = "Il luogo e' un campo obbligatorio")
        String place,

        @NotNull(message = "I posti a sedere sono un campo obbligatorio")
        long seats

) {
}
