package andreademasi.payloads.users;

import java.util.Date;
import java.util.List;

public record ErrorsResponseWithListDTO(String message,
                                        Date timestamp,
                                        List<String> errorsList) {
}
