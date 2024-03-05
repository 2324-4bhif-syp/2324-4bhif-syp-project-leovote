package at.htlleonding.entity.dto;

import java.util.UUID;

public record VoterDTO(UUID generatedId, Long participatingIn, boolean voted) {
}
