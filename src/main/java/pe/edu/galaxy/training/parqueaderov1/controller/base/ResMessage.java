package pe.edu.galaxy.training.parqueaderov1.controller.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class ResMessage {
    private String message;
    private Boolean success = false;
    private LinkedHashMap<String, Object> data;
}
