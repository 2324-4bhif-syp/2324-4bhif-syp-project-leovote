package at.htlleonding.entity.dto;

import java.util.UUID;

public record VoterDTO(String generatedId, Long participatingIn, boolean voted) {
}
