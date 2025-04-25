package eu.gaia_x.spservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SaveRequest {
    private String name;
    @JsonProperty(value = "service_id", required = true)
    private String serviceId;
    private String action;

    @JsonProperty(value = "selected_items", required = true)
    private List<SelectedItems> selectedItems;
}
