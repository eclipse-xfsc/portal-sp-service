package eu.gaia_x.spservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SelectedItems {
    @JsonProperty(value = "slot_id", required = true)
    private String slotId;
    @JsonProperty(value = "service_id", required = true)
    private String serviceId;
}
