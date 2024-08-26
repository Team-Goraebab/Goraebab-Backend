package api.goraebab.domain.blueprint.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class HostInfoDto {

    private String hostName;

    private String hostIpAddress;

    private String hostType;

    private List<BridgeInfoDto> bridges;

}
