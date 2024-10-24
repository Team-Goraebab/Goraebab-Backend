package api.goraebab.domain.blueprint.dockerObject;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CustomMount {

  private String type;

  private String name;

  private String source;

  private String destination;

  private String driver;

  @Builder
  public CustomMount(String type, String name, String source, String destination, String driver) {
    this.type = type;
    this.name = name;
    this.source = source;
    this.destination = destination;
    this.driver = driver;
  }
}
