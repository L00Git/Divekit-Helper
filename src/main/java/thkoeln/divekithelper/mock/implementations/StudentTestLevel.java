package thkoeln.divekithelper.mock.implementations;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.util.Pair;


import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;

import java.util.HashMap;


@NoArgsConstructor
public class StudentTestLevel implements Serializable {


    @Getter
    @Setter
    private HashMap< String, Pair< Instant, Integer > > messageLevel = new HashMap<>();


    public int triggerTest( String testName ){
        if( !messageLevel.containsKey( testName ) ){
            messageLevel.put( testName, Pair.of( Instant.now(), 1 ) );
        }
        else {
            if( Duration.between( messageLevel.get( testName ).getFirst() , Instant.now() ).toSeconds() > 10 ){
                int oldLevel = messageLevel.get( testName ).getSecond();
                messageLevel.put( testName, Pair.of( Instant.now(), ++oldLevel ) );
            }
        }

        return messageLevel.get( testName ).getSecond();
    }

}


