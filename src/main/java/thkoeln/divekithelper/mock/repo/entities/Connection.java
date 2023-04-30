package thkoeln.divekithelper.mock.repo.entities;






import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Connection {
    @Id
    private UUID id = UUID.randomUUID();

    //public Room room = new Room();

}



