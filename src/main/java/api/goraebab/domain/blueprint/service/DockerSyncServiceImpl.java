package api.goraebab.domain.blueprint.service;

import api.goraebab.domain.blueprint.dto.BridgeInfoDto;
import api.goraebab.domain.blueprint.dto.ContainerInfoDto;
import api.goraebab.domain.blueprint.dto.HostInfoDto;
import api.goraebab.domain.blueprint.dto.ParsedDataDto;
import api.goraebab.domain.blueprint.entity.Blueprint;
import api.goraebab.domain.blueprint.repository.BlueprintRepository;
import api.goraebab.global.exception.CustomException;
import api.goraebab.global.exception.ErrorCode;
import api.goraebab.global.util.DockerClientUtil;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.HostConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DockerSyncServiceImpl implements DockerSyncService {

    private final BlueprintRepository blueprintRepository;
    private final HtmlParserServiceImpl htmlParserService;
    private final DockerClientUtil dockerClientFactory;

    @Override
    public void syncDockerWithBlueprint(Long blueprintId) {
        DockerClient dockerClient;
        Blueprint blueprint = blueprintRepository.findById(blueprintId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_VALUE));

        String htmlContent = blueprint.getData();
        ParsedDataDto parsedData = htmlParserService.parseHtml(htmlContent);

        boolean isDockerRemote = blueprint.getIsDockerRemote();
        String remoteUrl = isDockerRemote ? blueprint.getDockerRemoteUrl() : null;

        if(isDockerRemote) {
            dockerClient = dockerClientFactory.createRemoteDockerClient(remoteUrl);
        } else {
            dockerClient = dockerClientFactory.createLocalDockerClient();
        }

        syncContainers(dockerClient, parsedData);
    }

    private void syncContainers(DockerClient dockerClient, ParsedDataDto parsedData) {
        List<Container> runningContainers = dockerClient.listContainersCmd().exec();

        removeAllContainers(dockerClient, runningContainers);
        createAndStartContainers(dockerClient, parsedData);
    }

    private void removeAllContainers(DockerClient dockerClient, List<Container> runningContainers) {
        for (Container container : runningContainers) {
            dockerClient.stopContainerCmd(container.getId()).exec();
            dockerClient.removeContainerCmd(container.getId()).exec();
        }
    }

    private void createAndStartContainers(DockerClient dockerClient, ParsedDataDto parsedData) {
        for(HostInfoDto host : parsedData.getHosts()) {
            for(BridgeInfoDto bridge : host.getBridges()) {
                for(ContainerInfoDto container : bridge.getContainers()) {
                    if(container.getContainerStatus().equals("Online")) {
                        try {
                            CreateContainerResponse response = createContainer(dockerClient, host, container);
                            startContainer(dockerClient, response.getId());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private CreateContainerResponse createContainer(DockerClient dockerClient, HostInfoDto host, ContainerInfoDto container) throws DockerException {
        return dockerClient.createContainerCmd("image-name")
                .withHostName(host.getHostName())
                .withName(container.getContainerName())
                .withHostConfig(new HostConfig().withNetworkMode("bridge"))
                .exec();
    }

    private void startContainer(DockerClient dockerClient, String containerId) throws DockerException {
        dockerClient.startContainerCmd(containerId).exec();
    }

}

