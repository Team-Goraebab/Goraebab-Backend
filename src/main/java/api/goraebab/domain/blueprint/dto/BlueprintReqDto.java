package api.goraebab.domain.blueprint.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BlueprintReqDto {

    @NotBlank(message = "Name must not be blank")
    @Size(max = 255, message = "Name must be less than 255 characters")
    private String name;

    @NotBlank(message = "Data must not be blank")
    private MultipartFile data;

    @NotBlank(message = "IsDockerRemote must not be blank")
    private Boolean isDockerRemote;

    private String remoteUrl;

    public void setData(MultipartFile data) {
        this.data = data;
    }

}
