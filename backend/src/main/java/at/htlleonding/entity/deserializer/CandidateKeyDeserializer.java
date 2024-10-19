package at.htlleonding.entity.deserializer;

import at.htlleonding.control.CandidateRepository;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import jakarta.inject.Inject;

import java.io.IOException;

public class CandidateKeyDeserializer extends KeyDeserializer {
    @Inject
    CandidateRepository candidateRepository;

    @Override
    public Object deserializeKey(String key, DeserializationContext deserializationContext) throws IOException {
        return candidateRepository.getCandidateBySchoolId(key); // Adjust as per your Candidate constructor
    }
}
