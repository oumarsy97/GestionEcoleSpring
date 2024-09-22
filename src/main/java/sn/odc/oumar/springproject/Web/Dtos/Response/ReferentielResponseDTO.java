package sn.odc.oumar.springproject.Web.Dtos.Response;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Data
public class ReferentielResponseDTO {
    private Long id;
    private String libelle;
    private String code;
    private String description;
    private String photoCouverture;
    private String etat; // Vous pouvez utiliser un Enum si vous le souhaitez
    private Set<CompetenceResponseDTO> competences;



    public ReferentielResponseDTO(Long id, String libelle, String code, String description, String photoCouverture, Stream<CompetenceResponseDTO> stream) {
        this.id = id;
        this.libelle = libelle;
        this.code = code;
        this.description = description;
        this.photoCouverture = photoCouverture;
        this.etat = etat;
        this.competences = stream.collect(Collectors.toSet());
    }
}