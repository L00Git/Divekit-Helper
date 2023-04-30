package thkoeln.divekithelper.mock.repo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.util.UUID;

@Embeddable
@Getter
@EqualsAndHashCode
//@Entity
public class Wall{
    //@Id
    @Setter
    @Getter
    private UUID roomId = UUID.randomUUID();



}
