package com.alejo2075.webflow.dtos;

import com.alejo2075.webflow.datastrutures.Graph.Vertex;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GraphResponse {

    private Vertex vertex;
    private int delay;

}
