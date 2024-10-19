package at.htlleonding.entity.deserializer;

import at.htlleonding.control.CandidateRepository;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import jakarta.enterprise.inject.spi.CDI;

import java.io.IOException;

public class CandidateKeyDeserializer extends KeyDeserializer {
    CandidateRepository candidateRepository;

    @Override
    public Object deserializeKey(String key, DeserializationContext deserializationContext) throws IOException {
        // Lazy load the CandidateRepository via CDI if it's not yet initialized
        if (candidateRepository == null) {
            candidateRepository = CDI.current().select(CandidateRepository.class).get();
        }

        return candidateRepository.getCandidateBySchoolId(key); // Fetch the candidate by schoolId
    }
}
