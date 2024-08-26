package api.goraebab.domain.blueprint.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.util.List;

@Getter
public class BridgeInfoDto {

    private String bridgeName;

    private String bridgeGateway;

    private List<ContainerInfoDto> containers;

    @JsonIgnore
    public boolean isValid() {
        return !bridgeName.isEmpty() && !bridgeGateway.isEmpty();
    }

    public void setContainers(List<ContainerInfoDto> containers) {
        this.containers = containers;
    }

    public BridgeInfoDto(String bridgeName, String bridgeGateway) {
        this.bridgeName = bridgeName;
        this.bridgeGateway = bridgeGateway;
    }

}

