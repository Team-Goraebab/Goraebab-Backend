package api.goraebab.domain.blueprint.dto;

import lombok.Getter;

@Getter
public class ContainerInfoDto {

    private String containerName;

    private String containerIpAddress;

    private String containerStatus;

    public ContainerInfoDto(String containerName, String containerIpAddress, String containerStatus) {
        this.containerName = containerName;
        this.containerIpAddress = containerIpAddress;
        this.containerStatus = containerStatus;
    }

}
