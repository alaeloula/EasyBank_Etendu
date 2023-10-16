package ma.alae.eloula.classes;

import lombok.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Demande {
    private int numero;
    @NonNull private DemandeEtat etat;
    @NonNull private Simulation simulation;
    @NonNull private String remarques;
    @NonNull private LocalDate date;
    @NonNull private Client client;
}